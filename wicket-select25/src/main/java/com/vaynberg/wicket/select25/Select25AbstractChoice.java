/*
 * Copyright 2012 Igor Vaynberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.vaynberg.wicket.select25;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.wicket.IRequestListener;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;
import org.json.JSONException;
import org.json.JSONWriter;

/**
 * Base class for Select2 components
 * @param <S>
 *  type of settings object
 * @param <T>
 * 	type of choice object
 * @param <M>
 * 	type of model object
 * @author igor
 */
abstract class Select25AbstractChoice<S extends Settings, T, M> extends FormComponent<M> implements IRequestListener {

	private static final ResourceReference JS = new JavaScriptResourceReference(Select25AbstractChoice.class, "res/select25.js");
	private static final ResourceReference CSS = new CssResourceReference(Select25AbstractChoice.class, "res/select25.css");

	protected final ChoiceProvider<T> provider;

	/**
	 * Constructor
	 *
	 * @param id
	 * 	component id
	 * @param model
	 * 	component model
	 * @param provider
	 * 	choice provider
	 */
	public Select25AbstractChoice(String id, IModel<M> model, ChoiceProvider<T> provider) {
		super(id, model);
		Args.notNull(provider, "provider");
		this.provider = provider;
		setOutputMarkupId(true);
	}


	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(JavaScriptHeaderItem.forReference(JS));
		response.render(CssHeaderItem.forReference(CSS));

		S settings = newSettings();

		String url = urlForListener( null).toString();

		Settings.Ajax ajax = new Settings.Ajax();
		ajax.setUrl(url);
		settings.setAjax(ajax);

		renderInitializationScript(response, settings);
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		checkComponentTag(tag, "input");
		checkComponentTagAttribute(tag, "type", "hidden");
		tag.put("name", getInputName());
		// we do not have to write out the value because it will be set by the javascript component
	}

	protected abstract void renderInitializationScript(IHeaderResponse response, S settings);


	protected abstract S newSettings();

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);

		if (event.getPayload() instanceof AjaxRequestTarget) {

			AjaxRequestTarget target = (AjaxRequestTarget) event.getPayload();

			if (target.getComponents().contains(this)) {

				// if this component is being repainted by ajax, directly, we must destroy Select2 so it removes
				// its elements from DOM

				target.prependJavaScript(getDestroyJavascript());
			}
		}
	}

	private CharSequence getDestroyJavascript() {
		StringBuilder script = new StringBuilder();
		script.append("window.select25.destroy(document.getElementById('").append(getMarkupId()).append("'));\n");
		return script;
	}

	@Override
	public boolean rendersPage() {
		return false;
	}

	@Override
	public void onRequest() {

		// this is the callback that retrieves matching choices used to populate the dropdown

		Request request = getRequestCycle().getRequest();
		IRequestParameters params = request.getRequestParameters();

		// retrieve choices matching the search query

		String term = params.getParameterValue("term").toOptionalString();

		int page = params.getParameterValue("page").toInt(1);

		Response<T> response = new Response<T>();
		provider.query(term, page, response);

		// jsonize and write out the choices to the response

		WebResponse webResponse = (WebResponse) getRequestCycle().getResponse();
		webResponse.setContentType("application/json");

		OutputStreamWriter out = new OutputStreamWriter(webResponse.getOutputStream(), getRequest().getCharset());
		JSONWriter json = new JSONWriter(out);

		try {
			json.object().key("values").array();
			addValues(json, response);
			json.endArray().key("more").value(response.getHasMore()).endObject();
		} catch (JSONException e) {
			throw new RuntimeException("Could not write Json response", e);
		}

		try {
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException("Could not write Json to servlet response", e);
		}
	}


	@Override
	protected void onRemove() {
		super.onRemove();

		AjaxRequestTarget target = getRequestCycle().find(AjaxRequestTarget.class).orElse(null);

		if (target != null) {
			// ensure the select2 is closed so we do not leave an orphaned dropdown component in the dom
			target.prependJavaScript(getDestroyJavascript());
		}

	}

	@Override
	protected void onDetach() {
		provider.detach();
		super.onDetach();
	}

	@Override
	protected boolean getStatelessHint() {
		return false;
	}


	protected void addValues(final JSONWriter json, final Iterable<T> response) throws JSONException {
		for (T item : response) {
			json.object();
			provider.toJson(item, json);
			json.endObject();
		}
	}
}

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.StringValue;
import org.json.JSONException;

import com.vaynberg.wicket.select25.json.JsonBuilder;

/**
 * Multi-select Select2 component. Should be attached to a {@code <input type='hidden'/>} element.
 *
 * @param <T>
 * 	type of choice object
 * @author igor
 */
public class Select25MultiChoice<T> extends AbstractSelect2Choice<T, Collection<T>> {


	public Select25MultiChoice(String id, IModel<Collection<T>> model, ChoiceProvider<T> provider) {
		super(id, model, provider);
	}


	@Override
	public void convertInput() {

		List<StringValue> input = getWebRequest().getRequestParameters().getParameterValues(getInputName());

		final Collection<T> choices;

		if (input == null || input.isEmpty()) {
			choices = new ArrayList<T>();
		} else {
			choices = provider.toChoices(input);
		}

		setConvertedInput(choices);
	}

	@Override
	public void updateModel() {
		FormComponent.updateCollectionModel(this);
	}

	@Override
	protected void renderInitializationScript(IHeaderResponse response, Settings settings) {

		String values="[]";

		Collection<? extends T> choices;
		if (getWebRequest().getRequestParameters().getParameterNames().contains(getInputName())) {
			convertInput();
			choices = getConvertedInput();
		} else {
			choices = getModelObject();
		}

		if (choices != null && !choices.isEmpty()) {

			JsonBuilder selection = new JsonBuilder();

			try {
				selection.array();
				for (T choice : choices) {
					selection.object();
					provider.toJson(choice, selection);
					selection.endObject();
				}
				selection.endArray();
			} catch (JSONException e) {
				throw new RuntimeException("Error converting model object to Json", e);
			}

			values=selection.toJson().toString();
		}


		settings.setValues(values);

		StringBuilder init = new StringBuilder();
		init.append("var container = document.createElement('div');\n");
		init.append("var opts=").append(settings.toJson()).append(";");
		init.append("var target = $('#").append(getMarkupId()).append("');\n");
		init.append("target.data('s25container', container);");
		init.append("target.attr('disabled', 'disabled');\n");
		init.append("target.after(container);\n");
		init.append("ReactDOM.render(React.createElement(select25.MultiSelect, opts), container);\n");

		response.render(OnDomReadyHeaderItem.forScript(init));
	}
}

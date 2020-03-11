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

import java.util.Collection;
import java.util.Collections;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;
import org.json.JSONException;

import com.vaynberg.wicket.select25.json.JsonBuilder;

/**
 * Multi-select Select2 component. Should be attached to a {@code <input type='hidden'/>} element.
 *
 * @param <T>
 * 	type of choice object
 * @author igor
 */
public class Select25MultiChoice<T> extends Select25AbstractChoice<MultiSettings, T, Collection<T>> {

	private final IModel<String> valuesLabel;
	private final IModel<String> comboboxLabel;

	public Select25MultiChoice(String id, IModel<Collection<T>> model, ChoiceProvider<T> provider,IModel<String> valuesLabel,IModel<String> comboboxLabel) {
		super(id, model, provider);
		this.valuesLabel=wrap(valuesLabel);
		this.comboboxLabel=wrap(comboboxLabel);
	}

	@Override
	public void convertInput() {

		String input = getWebRequest().getRequestParameters().getParameterValue(getInputName()).toString();
		var choices=convertInput(input);
		setConvertedInput(choices);
	}


	private Collection<T> convertInput(String input) {

		if (Strings.isEmpty(input)) {
			return Collections.emptyList();
		} else {
			var values = input.split(",");
			var choices = provider.toChoices(values);
			return choices;
		}
	}


	@Override
	public void updateModel() {
		FormComponent.updateCollectionModel(this);
	}

	@Override
	protected void renderInitializationScript(IHeaderResponse response, MultiSettings settings) {

		final Collection<? extends T> choices;
		if (hasRawInput()) {
			choices=convertInput(getRawInput());
		} else {
			choices=getModelObject();
		}

		if (choices!=null && !choices.isEmpty()) {
			JsonBuilder writer = new JsonBuilder();

			try {
				writer.array();
				for (T choice : choices) {
					writer.object();
					provider.toJson(choice, writer);
					writer.endObject();
				}
				writer.endArray();
				settings.setValues(writer.toJson().toString());
			} catch (JSONException e) {
				throw new RuntimeException("Error converting model object to Json", e);
			}
		} else {
			settings.setValues("[]");
		}

		StringBuilder init = new StringBuilder();
		init.append("window.select25.createMultiSelect(document.getElementById('" + getMarkupId() + "'), " + settings.toJson() + ");");
		response.render(OnDomReadyHeaderItem.forScript(init));
	}

	@Override
	protected MultiSettings newSettings() {
		var settings= new MultiSettings();
		settings.setValuesLabel(valuesLabel.getObject());
		settings.setComboboxLabel(comboboxLabel.getObject());
		addDictionaryToSettings(settings);
		return settings;
	}
}

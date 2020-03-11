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

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.Strings;
import org.json.JSONException;

import com.vaynberg.wicket.select25.json.JsonBuilder;

/**
 * Single-select Select2 component. Should be attached to a {@code <input type='hidden'/>} element.
 *
 * @param <T>
 * 	type of choice object
 * @author igor
 */
public class Select25SingleChoice<T> extends Select25AbstractChoice<SingleSettings, T, T> {
	private final IModel<String> label;

	public Select25SingleChoice(String id, IModel<T> model, ChoiceProvider<T> provider, IModel<String> label) {
		super(id, model, provider);
		this.label=label;
	}

	@Override
	public void convertInput() {
		StringValue input = getWebRequest().getRequestParameters().getParameterValue(getInputName());
		T choice = convertInput(input.toString());
		setConvertedInput(choice);
	}

	private T convertInput(String input) {
		if (Strings.isEmpty(input)) {
			return null;
		} else {
			var choices = provider.toChoices(new String[] { input });
			if (choices.isEmpty()) {
				return null;
			} else {
				return choices.iterator().next();
			}
		}
	}

	@Override
	protected void renderInitializationScript(IHeaderResponse response, SingleSettings settings) {

		final T value;

		if (hasRawInput()) {
			value = convertInput(getRawInput());
		} else {
			value = getModelObject();
		}

		if (value != null) {
			try {
				JsonBuilder writer = new JsonBuilder();
				writer.object();
				provider.toJson(value, writer);
				writer.endObject();
				settings.setValue(writer.toJson().toString());
			} catch (JSONException e) {
				throw new RuntimeException("Error converting model object to Json", e);
			}
		}

		StringBuilder init = new StringBuilder();
		init.append("window.select25.createSingleSelect(document.getElementById('" + getMarkupId() + "'), " + settings.toJson() + ");");
		response.render(OnDomReadyHeaderItem.forScript(init));
	}

	@Override
	protected SingleSettings newSettings() {
		var settings= new SingleSettings();
		settings.setLabel(label.getObject());
		addDictionaryToSettings(settings);
		return settings;
	}
}

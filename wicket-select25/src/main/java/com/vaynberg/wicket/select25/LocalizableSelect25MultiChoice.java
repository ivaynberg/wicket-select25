package com.vaynberg.wicket.select25;

import java.util.Collection;

import org.apache.wicket.model.IModel;

public abstract class LocalizableSelect25MultiChoice<T> extends Select25MultiChoice<T>
	implements Select25DictionaryProducer {

	public LocalizableSelect25MultiChoice(String id, IModel<Collection<T>> model, ChoiceProvider<T> provider,
		IModel<String> valuesLabel, IModel<String> comboboxLabel) {
		super(id, model, provider, valuesLabel, comboboxLabel);
	}

	@Override
	protected MultiSettings newSettings() {
		final MultiSettings settings = super.newSettings();
		settings.setDictionary(createDictionary(this));
		return settings;
	}
}

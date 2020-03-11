package com.vaynberg.wicket.select25;

import org.apache.wicket.model.IModel;

public abstract class LocalizableSelect25SingleChoice<T> extends Select25SingleChoice<T>
	implements Select25DictionaryProducer {

	public LocalizableSelect25SingleChoice(String id, IModel<T> model, ChoiceProvider<T> provider,
		IModel<String> label) {
		super(id, model, provider, label);
	}

	@Override
	protected SingleSettings newSettings() {
		final SingleSettings settings = super.newSettings();
		settings.setDictionary(createDictionary(this));
		return settings;
	}
}

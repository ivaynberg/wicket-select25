package com.vaynberg.wicket.select25;

import org.apache.wicket.util.string.Strings;
import org.json.JSONException;
import org.json.JSONStringer;

import com.vaynberg.wicket.select25.json.Json;

public class MultiSettings extends Settings {
	private String valuesLabel;
	private String comboboxLabel;
	private String values;
	private boolean allowDuplicates;


	public String toJson() {
		if (Strings.isEmpty(valuesLabel)) {
			throw new IllegalStateException("Setting valuesLabel cannot be empty");
		}
		if (Strings.isEmpty(comboboxLabel)) {
			throw new IllegalStateException("Setting comboboxLabel cannot be empty");
		}

		try {
			JSONStringer writer = new JSONStringer();
			writer.object();

			toJson(writer);

			Json.writeValue(writer, "multiple", true);
			Json.writeValue(writer, "valuesLabel", valuesLabel);
			Json.writeValue(writer, "comboboxLabel", comboboxLabel);
			Json.writeValue(writer, "allowDuplicates", allowDuplicates);
			Json.writeFunction(writer, "values", values);

			writer.endObject();

			return writer.toString();
		} catch (JSONException e) {
			throw new RuntimeException("Could not convert Settings object to Json", e);
		}
	}

	public boolean isAllowDuplicates() {
		return allowDuplicates;
	}

	public void setAllowDuplicates(boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}

	public String getValuesLabel() {
		return valuesLabel;
	}

	public void setValuesLabel(String valuesLabel) {
		this.valuesLabel = valuesLabel;
	}

	public String getComboboxLabel() {
		return comboboxLabel;
	}

	public void setComboboxLabel(String comboboxLabel) {
		this.comboboxLabel = comboboxLabel;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public String getValues() {
		return values;
	}
}

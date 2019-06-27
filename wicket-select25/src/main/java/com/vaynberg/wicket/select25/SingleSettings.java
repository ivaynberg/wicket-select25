package com.vaynberg.wicket.select25;

import org.apache.wicket.util.string.Strings;
import org.json.JSONException;
import org.json.JSONStringer;

import com.vaynberg.wicket.select25.json.Json;


public class SingleSettings extends Settings {
	private String label;
	private String value;
	private boolean allowClear;


	public String toJson() {
		if (Strings.isEmpty(label)) {
			throw new IllegalStateException("Setting label cannot be empty");
		}

		try {
			JSONStringer writer = new JSONStringer();
			writer.object();

			toJson(writer);

			Json.writeValue(writer, "multiple", false);
			Json.writeValue(writer, "label", label);
			Json.writeValue(writer, "allowClear", allowClear);
			Json.writeFunction(writer, "value", value);

			writer.endObject();

			return writer.toString();
		} catch (JSONException e) {
			throw new RuntimeException("Could not convert Settings object to Json", e);
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isAllowClear() {
		return allowClear;
	}

	public void setAllowClear(boolean allowClear) {
		this.allowClear = allowClear;
	}
}

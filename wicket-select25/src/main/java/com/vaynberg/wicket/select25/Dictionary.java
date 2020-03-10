package com.vaynberg.wicket.select25;

import java.io.Serializable;

import org.json.JSONStringer;

import com.vaynberg.wicket.select25.json.Json;

public interface Dictionary extends Serializable {
	void writeToJson(JSONStringer writer);
	
	public static class DictionaryName implements Dictionary {
		private final String name;

		public DictionaryName(final String name) {
			this.name = name;
		}

		@Override
		public void writeToJson(JSONStringer writer) {
			Json.writeValue(writer, "dictionary", name);
		}

		public String getName() {
			return name;
		}
	}

	public static class DictionaryImplementation implements Dictionary {
		private final String javaScriptObject;

		public DictionaryImplementation(final String javaScriptObject) {
			this.javaScriptObject = javaScriptObject;
		}

		@Override
		public void writeToJson(JSONStringer writer) {
			Json.writeFunction(writer, "dictionary", javaScriptObject);
		}

		public String getJavaScriptObject() {
			return javaScriptObject;
		}
	}
}
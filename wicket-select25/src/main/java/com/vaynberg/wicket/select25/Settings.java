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

import java.io.Serializable;

import org.apache.wicket.util.string.Strings;
import org.json.JSONException;
import org.json.JSONStringer;

import com.vaynberg.wicket.select25.json.Json;

/**
 * Select2 settings. Refer to the Select2 documentation for what these options mean.
 *
 * @author igor
 */
abstract class Settings implements Serializable {

	private String name;

	private String containerStyle;
	private String cssClass;
	private boolean openOnFocus;

	private String itemId = "id";
	private String valueContent = "\"text\"";
	private String itemLabel = "\"text\"";
	private String resultContent = "\"text\"";

	private int minimumCharacters;

	private String placeholder;

	private Dictionary dictionary;

	private Ajax ajax;


	public abstract String toJson();

	protected void toJson(JSONStringer writer) throws JSONException {

		Json.writeValue(writer, "containerStyle", containerStyle);
		Json.writeValue(writer, "cssClass", cssClass);
		Json.writeValue(writer, "openOnFocus", openOnFocus);

		Json.writeValue(writer, "itemId", itemId);
		Json.writeFunction(writer, "valueContent", valueContent);
		Json.writeFunction(writer, "itemLabel", itemLabel);
		Json.writeFunction(writer, "resultContent", resultContent);

		Json.writeValue(writer, "minimumCharacters", minimumCharacters);

		if (!Strings.isEmpty(placeholder)) {
			Json.writeValue(writer, "placeholder", placeholder);
		}

		if (dictionary != null) {
			dictionary.writeToJson(writer);
		}

		Json.writeFunction(writer, "ajax", ajax.toJson());
	}

	public void setOpenOnFocus(boolean openOnFocus) {
		this.openOnFocus = openOnFocus;
	}

	public boolean isOpenOnFocus() {
		return openOnFocus;
	}

	public String getContainerStyle() {
		return containerStyle;
	}

	public void setContainerStyle(String containerStyle) {
		this.containerStyle = containerStyle;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @deprecated Use {@link #getCssClass()} instead.
	 */
	@Deprecated
	public String getContainerCss() {
		return getCssClass();
	}

	/**
	 * @deprecated Use {@link #setCssClass(String)} instead.
	 */
	@Deprecated
	public void setContainerCss(String cssClass) {
		setCssClass(cssClass);
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getValueContent() {
		return valueContent;
	}

	public void setValueContent(String valueContent) {
		this.valueContent = valueContent;
	}

	public String getItemLabel() {
		return itemLabel;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public String getResultContent() {
		return resultContent;
	}

	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}



	public int getMinimumCharacters() {
		return minimumCharacters;
	}

	public void setMinimumCharacters(int minimumCharacters) {
		this.minimumCharacters = minimumCharacters;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public Ajax getAjax() {
		return ajax;
	}

	void setAjax(Ajax ajax) {
		this.ajax = ajax;
	}



	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}


	public static class Ajax implements Serializable {
		private String url;
		private String params;
		private String process;
		private String onError;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getParams() {
			return params;
		}

		public void setParams(String params) {
			this.params = params;
		}

		public String getProcess() {
			return process;
		}

		public void setProcess(String process) {
			this.process = process;
		}

		public String getOnError() {
			return onError;
		}

		/**
		 * Sets a JavaScript callback for when an error occurs when retrieving new choices.
		 *
		 * This callback will be called when there if the server can not be reached, or if the server responds with an
		 * HTTP status other than 200 OK. This method has the signature {@code (data?: string, status?:number) => void},
		 * where the response text and HTTP status code are provided if available.
		 *
		 * @param onError
		 */
		public void setOnError(String onError) {
			this.onError = onError;
		}

		public String toJson() {
			try {
				JSONStringer writer = new JSONStringer();
				writer.object();

				Json.writeValue(writer, "url", url);
				Json.writeValue(writer, "params", params);
				Json.writeValue(writer, "process", process);

				if (!Strings.isEmpty(onError)) {
					Json.writeFunction(writer, "onerror", onError);
				}

				writer.endObject();

				return writer.toString();
			} catch (JSONException e) {
				throw new RuntimeException("Could not convert Ajax object to Json", e);
			}
		}

	}
}

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

import org.json.JSONException;
import org.json.JSONStringer;

import com.vaynberg.wicket.select25.json.Json;

/**
 * Select2 settings. Refer to the Select2 documentation for what these options mean.
 *
 * @author igor
 */
public final class Settings implements Serializable {

	private String name;

	private String style;
	private String itemId = "id";
	private String valueContent = "\"text\"";
	private String valueLabel = "\"text\"";
	private String resultContent = "\"text\"";

	private String valuesLabel;
	private String searchLabel;

	private String values;

	private int minimumCharacters;

	private String query;

	public CharSequence toJson() {
		try {
			JSONStringer writer = new JSONStringer();
			writer.object();

			Json.writeValue(writer, "name", name);
			Json.writeValue(writer, "style", style);

			Json.writeFunction(writer, "values", values);


			Json.writeValue(writer, "itemId", itemId);
			Json.writeFunction(writer, "valueContent", valueContent);
			Json.writeValue(writer, "valueLabel", valueLabel);
			Json.writeFunction(writer, "resultContent", resultContent);

			Json.writeValue(writer, "minimumCharacters", minimumCharacters);

			Json.writeFunction(writer, "query", query);


			Json.writeValue(writer, "valuesLabel", valuesLabel);
			Json.writeValue(writer, "searchLabel", searchLabel);


			writer.endObject();

			return writer.toString();
		} catch (JSONException e) {
			throw new RuntimeException("Could not convert Select2 settings object to Json", e);
		}
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
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

	public String getValueLabel() {
		return valueLabel;
	}

	public void setValueLabel(String valueLabel) {
		this.valueLabel = valueLabel;
	}

	public String getResultContent() {
		return resultContent;
	}

	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}

	public String getValuesLabel() {
		return valuesLabel;
	}

	public void setValuesLabel(String valuesLabel) {
		this.valuesLabel = valuesLabel;
	}

	public String getSearchLabel() {
		return searchLabel;
	}

	public void setSearchLabel(String searchLabel) {
		this.searchLabel = searchLabel;
	}

	public int getMinimumCharacters() {
		return minimumCharacters;
	}

	public void setMinimumCharacters(int minimumCharacters) {
		this.minimumCharacters = minimumCharacters;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public String getValues() {
		return values;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

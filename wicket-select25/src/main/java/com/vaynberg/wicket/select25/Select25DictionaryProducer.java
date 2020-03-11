package com.vaynberg.wicket.select25;

import org.apache.wicket.model.StringResourceModel;

import com.github.openjson.JSONObject;
import com.vaynberg.wicket.select25.Dictionary.DictionaryImplementation;

/**
 * Generates a Select25 Dictionary JavaScript object using localized strings found in a wicket .properties file, using
 * {@link StringResourceModel}s. The following properties are used:
 * 
 * <ul>
 * <li>select25.noSearchResults</li>
 * <li>select25.searchResultsLoading</li>
 * <li>select25.removeButtonTitle</li>
 * <li>select25.clearButtonTitle</li>
 * <li>select25.valueAdded</li>
 * <li>select25.minimumCharactersMessage1</li>
 * <li>select25.minimumCharactersMessageX</li>
 * <li>select25.multiSelectInstructions</li>
 * <li>select25.expandButtonTitle</li>
 * </ul>
 * 
 * @author matthewgeer
 */
public interface Select25DictionaryProducer {
	public default DictionaryImplementation createDictionary(final Select25AbstractChoice<?, ?, ?> component) {
		// @formatter:off
		return new DictionaryImplementation(
			"{" +
				"valueAdded: function(itemLabel) {" +
					"return " + JSONObject.quote(new StringResourceModel("select25.valueAdded", component)
							.setParameters("$value$").getObject()) + ".replace('$value$', itemLabel);" +
				"}, " +
				"noSearchResults: function() {" +
					"return " + JSONObject.quote(new StringResourceModel("select25.noSearchResults", component).getObject()) + ";" +
				"}, " +
				"searchResultsLoading: function() {" +
					"return " + JSONObject.quote(new StringResourceModel("select25.searchResultsLoading", component).getObject()) + ";" +
				"}, " +
				"removeButtonTitle: function() {" +
					"return " + JSONObject.quote(new StringResourceModel("select25.removeButtonTitle", component).getObject()) + ";" +
				"}, " +
				"clearButtonTitle: function() {" +
					"return " + JSONObject.quote(new StringResourceModel("select25.clearButtonTitle", component).getObject()) + ";" +
				"}, " +
				"minimumCharactersMessage: function(len, min) {" +
					"var delta = min - len;" +
					"if(delta == 1) { " +
						"return " + JSONObject.quote(new StringResourceModel("select25.minimumCharactersMessage1", component).getObject()) + ";" +
					"} else {" +
						"return " + JSONObject.quote(new StringResourceModel("select25.minimumCharactersMessageX", component)
								.setParameters("$delta$").getObject()) + ".replace('$delta$', delta);" +
					"}" +
				"}, " +
				"multiSelectInstructions: function() {" +
					"return " + JSONObject.quote(new StringResourceModel("select25.multiSelectInstructions", component).getObject()) + ";" +
				"}, " +
				"expandButtonTitle: function () {" +
					"return " + JSONObject.quote(new StringResourceModel("select25.expandButtonTitle", component).getObject()) + ";" +
				"} " +
			"}"
		);
		// @formatter:on
	}
}

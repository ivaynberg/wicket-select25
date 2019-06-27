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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Example page.
 *
 * @author igor
 */
@SuppressWarnings("unused")
public class HomePage extends WebPage {
    private static final int PAGE_SIZE = 20;

    private Country country = Country.US;
    private List<Country> countries = new ArrayList<Country>(Arrays.asList(new Country[]{Country.US, Country.CA}));

    public HomePage() {

        queue(new Form<Void>("form"));
        // multi-select example


        var countriesModel = new PropertyModel<Collection<Country>>(this, "countries");

        Label countriesLabel = new Label("countriesLabel", countriesModel);
        countriesLabel.setOutputMarkupId(true);
        queue(countriesLabel);


        var countries=new Select25MultiChoice<Country>("countries",
            countriesModel, new CountriesProvider(), new Model("Selected Countries"), new Model("Add Country"));
        queue(countries);

        countries.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(countriesLabel);
            }
        });


        // single-select example

        var countryModel = new PropertyModel<Country>(this, "country");

        Label countryLabel = new Label("countryLabel", countryModel);
        countryLabel.setOutputMarkupId(true);
        queue(countryLabel);

        var country=new Select25SingleChoice<Country>("country", countryModel, new CountriesProvider(), new Model("Country")) {
            @Override
            protected SingleSettings newSettings() {
                var settings= super.newSettings();
                settings.setAllowClear(true);
                return settings;
            }
        };
        queue(country);

        country.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(countryLabel);
            }
        });

    }

    /**
     * Queries {@code pageSize} worth of countries from the {@link Country} enum, starting with {@code page * pageSize}
     * offset. Countries are matched on their {@code displayName} containing {@code term}
     *
     * @param term     search term
     * @param page     starting page
     * @param pageSize items per page
     * @return list of matches
     */
    private static List<Country> queryMatches(String term, int page, int pageSize) {

        List<Country> result = new ArrayList<Country>();

        if (term == null) {
            term = "";
        }
        term = term.toUpperCase();

        final int offset = page * pageSize;

        int matched = 0;
        for (Country country : Country.values()) {
            if (result.size() == pageSize) {
                break;
            }

            if (country.getDisplayName().toUpperCase().contains(term)) {
                matched++;
                if (matched > offset) {
                    result.add(country);
                }
            }
        }
        return result;
    }

    /**
     * {@link Country} based choice provider for Select2 components. Demonstrates integration between Select2 components
     * and a domain object (in this case an enum).
     *
     * @author igor
     */
    public class CountriesProvider extends TextChoiceProvider<Country> {

        @Override
        protected String getDisplayText(Country choice) {
            return choice.getDisplayName();
        }

        @Override
        protected Object getId(Country choice) {
            return choice.name();
        }

        @Override
        public void query(String query, int page, Response<Country> response) {
            response.addAll(queryMatches(query, page, 20));
            response.setHasMore(response.size() == 20);
        }

        @Override
        public Collection<Country> toChoices(String[] ids) {
            ArrayList<Country> countries = new ArrayList<Country>();
            for (String id : ids) {
                countries.add(Country.valueOf(id));
            }
            return countries;
        }
    }

}

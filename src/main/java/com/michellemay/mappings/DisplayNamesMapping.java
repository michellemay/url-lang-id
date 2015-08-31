/*
 * Copyright 2015 Michel Lemay
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.michellemay.mappings;

import org.apache.commons.lang3.LocaleUtils;

import java.util.HashMap;
import java.util.Locale;

/**
 * Human readable display names mappings.
 *
 * @author Michel Lemay
 */
public class DisplayNamesMapping extends Mapping {
    private Locale displayLocale;

    /**
     * Instantiates a new Display names mapping.
     *
     * @param name the name
     * @param displayLocale the display locale
     */
    public DisplayNamesMapping(String name, Locale displayLocale) {
        super(name);
        this.displayLocale = displayLocale;

        // Build reverse map
        HashMap<String, Locale> map = new HashMap<String, Locale>();
        for (Locale loc : LocaleUtils.availableLocaleList()) {
            String displayName = loc.getDisplayName(displayLocale).toLowerCase();
            String isoCode = loc.getLanguage().toLowerCase();
            if (isoCode.length() > 0 && !map.containsKey(displayName)) {
                map.put(displayName, LocaleUtils.toLocale(isoCode));
            }
        }
        this.withMapping(map).withCaseSensitive(false);
    }
}

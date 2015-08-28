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
 * @author Michel Lemay
 */
public class ISO639Alpha3Mapping extends Mapping {
    static public String NAME = "ISO-639-ALPHA-3";

    public ISO639Alpha3Mapping() {
        super(NAME);

        // Build reverse map
        HashMap<String, Locale> map = new HashMap<String, Locale>();
        for (Locale loc : LocaleUtils.availableLocaleList()) {
            String isoCode = loc.getLanguage();
            if (isoCode.length() > 0) {
                String displayValue = loc.getISO3Language().toLowerCase();
                if (!map.containsKey(displayValue)) {
                    map.put(displayValue, LocaleUtils.toLocale(isoCode));
                }
            }
        }
        this.withMapping(map).withCaseSensitive(false);
    }
}

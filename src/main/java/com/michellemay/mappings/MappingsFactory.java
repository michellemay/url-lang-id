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
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Mappings factory.
 *
 * @author Michel Lemay
 */
public class MappingsFactory {
    private HashMap<String, Mapping> mappings;

    /**
     * Gets mappings.
     *
     * @return the mappings
     */
    public Map<String, Mapping> getMappings() { return mappings; }

    /**
     * Instantiates a new Mappings factory.
     *
     * @param mappingsConfig the mappings config
     */
    public MappingsFactory(List<MappingConfig> mappingsConfig) {
        mappings = new HashMap<String, Mapping>();

        addDefaultMappings();

        if (mappingsConfig != null) {
            for (MappingConfig mappingConfig : mappingsConfig) {
                addMapping(createCustomMapping(mappingConfig));
            }
        }
    }

    private void addMapping(Mapping mapping) {
        if (mappings.containsKey(mapping.getName())) {
            throw new IllegalStateException("A mapping name '" + mapping.getName() + "' already exists!");
        }
        mappings.put(mapping.getName(), mapping);
    }

    private void addDefaultMappings() {
        addMapping(new ISO639Alpha2Mapping());
        addMapping(new ISO639Alpha3Mapping());
        addMapping(new LanguageTagsMapping());
        addMapping(new EnglishNamesMapping());
    }

    private Mapping createCustomMapping(MappingConfig mappingConfig) {
        if (StringUtils.isBlank(mappingConfig.name)) {
            throw new IllegalArgumentException("Blank mapping name!");
        }

        Map<String, Locale> curMap = new TreeMap<String, Locale>(mappingConfig.casesensitive ? null : String.CASE_INSENSITIVE_ORDER);

        // Inherit all mappings from bases
        if (mappingConfig.extend != null) {
            for (String baseMappingName : mappingConfig.extend) {
                if (StringUtils.isBlank(baseMappingName) || !mappings.containsKey(baseMappingName)) {
                    throw new IllegalStateException("Base mapping name '" + baseMappingName + "' does not exists!");
                }
                Mapping baseMapping = mappings.get(baseMappingName);
                baseMapping.getMapping().forEach(curMap::putIfAbsent);
            }
        }

        // Filter out unwanted languages
        if (StringUtils.isNotBlank(mappingConfig.filter)) {
            List<Locale.LanguageRange> priorityList = Locale.LanguageRange.parse(mappingConfig.filter);
            List<Locale> toKeep = Locale.filter(priorityList, curMap.values());
            curMap.entrySet().removeIf(e -> !toKeep.contains(e.getValue()));
        }

        // Add new values
        if (mappingConfig.add != null) {
            mappingConfig.add.forEach((lang, values) -> {
                Locale langLocale = LocaleUtils.toLocale(lang);
                String[] displayValues = values.split(",");
                for (String value : displayValues) {
                    String cleanedValue = value.trim();
                    if (!cleanedValue.isEmpty()) {
                        curMap.putIfAbsent(cleanedValue, langLocale);
                    }
                }
            });
        }

        // Override values
        if (mappingConfig.override != null) {
            mappingConfig.override.forEach((lang, values) -> {
                Locale langLocale = LocaleUtils.toLocale(lang);

                // Remove all existing mappings
                String langTag = langLocale.toLanguageTag();
                curMap.entrySet().removeIf(e -> e.getValue().toLanguageTag().equals(langTag));

                // Add new mappings.
                String[] displayValues = values.split(",");
                for (String value : displayValues) {
                    String cleanedValue = value.trim();
                    if (!cleanedValue.isEmpty()) {
                        curMap.put(cleanedValue, langLocale);
                    }
                }
            });
        }

        return new CustomMapping(mappingConfig.name.trim()).withCaseSensitive(mappingConfig.casesensitive).withMapping(curMap);
    }
}

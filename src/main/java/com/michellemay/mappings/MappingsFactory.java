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

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author Michel Lemay
 */
public class MappingsFactory {
    private HashMap<String, Mapping> mappings;

    public Map<String, Mapping> getMappings() { return mappings; }

    public MappingsFactory(List<MappingConfig> mappingsConfig) {
        mappings = new HashMap<String, Mapping>();

        addDefaultMappings();
        for (MappingConfig mappingConfig : mappingsConfig) {
            addMapping(createMapping(mappingConfig));
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
    }

    private Mapping createMapping(MappingConfig mappingConfig) {
        if (StringUtils.isBlank(mappingConfig.name)) {
            throw new IllegalArgumentException("Blank mapping name!");
        }

        HashMap<String, String> curMap = new HashMap<String, String>();

        // Inherit all mappings from bases
        if (mappingConfig.extend != null) {
            for (String baseMappingName : mappingConfig.extend) {
                if (StringUtils.isBlank(baseMappingName)) {
                    throw new IllegalArgumentException("Blank base mapping name!");
                }
                baseMappingName = baseMappingName.trim();
                if (!mappings.containsKey(baseMappingName)) {
                    throw new IllegalStateException("Base mapping name '" + baseMappingName + "' does not exists!");
                }
                Mapping baseMapping = mappings.get(baseMappingName);
                baseMapping.getMapping().forEach(curMap::putIfAbsent);
            }
        }

        // Filter unwanted languages
        if (mappingConfig.filter != null) {
            Set<String> toKeep = new HashSet<String>(Lists.transform(mappingConfig.filter, (x) -> x.toLowerCase().trim()));
            curMap.entrySet().removeIf(e -> !toKeep.contains(e.getValue()));
        }

        // Add new values
        if (mappingConfig.add != null) {
            mappingConfig.add.forEach((k, v) -> {
                String lang = k.toLowerCase().trim();
                String[] values = v.split(",");
                for (String value : values) {
                    String cleanedValue = value.trim();
                    if (!cleanedValue.isEmpty()) {
                        curMap.putIfAbsent(cleanedValue, lang);
                    }
                }
            });
        }

        // Override values
        if (mappingConfig.override != null) {
            mappingConfig.override.forEach((k, v) -> {
                // Remove all mappings to given language
                String lang = k.toLowerCase().trim();
                curMap.entrySet().removeIf(e -> e.getValue().equals(lang));

                // Add new mappings.
                String[] values = v.split(",");
                for (String value : values) {
                    String cleanedValue = value.trim();
                    if (!cleanedValue.isEmpty()) {
                        curMap.put(cleanedValue, lang);
                    }
                }
            });
        }

        return new CustomMapping(mappingConfig.name.trim()).withCaseSensitive(mappingConfig.casesensitive).withMapping(curMap);
    }

}

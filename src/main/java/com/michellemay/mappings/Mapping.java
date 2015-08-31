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

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Base Mapping class.
 *
 * @author Michel Lemay
 */
public class Mapping {
    private String name;
    private Map<String, Locale> mapping;
    private boolean caseSensitive;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Gets mapping.
     *
     * @return the mapping
     */
    public Map<String, Locale> getMapping() { return mapping; }

    /**
     * With mapping.
     *
     * @param mapping the mapping
     * @return the mapping
     */
    public Mapping withMapping(Map<String, Locale> mapping) { this.mapping = mapping; return this; }

    /**
     * Gets case sensitive.
     *
     * @return the case sensitive
     */
    public boolean getCaseSensitive() { return caseSensitive; }

    /**
     * With case sensitive.
     *
     * @param caseSensitive the case sensitive
     * @return the mapping
     */
    public Mapping withCaseSensitive(boolean caseSensitive) { this.caseSensitive = caseSensitive; return this; }

    /**
     * Instantiates a new Mapping.
     *
     * @param name the name
     */
    protected Mapping(String name) {
        this.name = name;
    }

    /**
     * Detect locale for rawValue
     *
     * @param rawValue the raw value
     * @return the optional
     */
    public Optional<Locale> detect(String rawValue) {
        String value = caseSensitive ? rawValue : rawValue.toLowerCase();
        return Optional.ofNullable(mapping.get(value));
    }
}

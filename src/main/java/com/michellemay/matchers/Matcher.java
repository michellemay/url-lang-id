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

package com.michellemay.matchers;

import com.michellemay.mappings.Mapping;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author Michel Lemay
 */
public abstract class Matcher {
    private static String LANG_GROUP_NAME = "lang";
    public enum UrlPart { hostname, path, querystring };

    private String name;
    private Matcher.UrlPart urlPart;
    private List<Pattern> patterns;
    private Optional<Mapping> mapping;
    private boolean caseSensitive;

    public String getName() { return name; }

    public UrlPart getUrlPart() { return urlPart; }

    public List<Pattern> getPatterns() { return patterns; }
    public Matcher withPatterns(List<Pattern> patterns) { this.patterns = patterns; return this; }

    public Optional<Mapping> getMapping() { return mapping; }
    public Matcher withMapping(Optional<Mapping> mapping) { this.mapping = mapping; return this; }

    public boolean getCaseSensitive() { return caseSensitive; }
    public Matcher withCaseSensitive(boolean caseSensitive) { this.caseSensitive = caseSensitive; return this; }

    protected Matcher(String name, UrlPart urlpart) {
        this.name = name;
        this.urlPart = urlpart;
    }

    public Optional<Locale> detect(URL url) {
        Optional<Locale> lang = Optional.empty();

        search: {
            List<String> parts = getParts(url);
            for (String part : parts) {
                for (Pattern p : getPatterns()) {
                    java.util.regex.Matcher regexMatcher = p.matcher(part);
                    if (regexMatcher.matches()) {
                        lang = mapping.get().detect(regexMatcher.group(LANG_GROUP_NAME));
                        if (lang.isPresent()) {
                            break search;
                        }
                    }
                }
            }
        }
        return lang;
    }

    protected abstract List<String> getParts(URL url);

    // So I've heard Cloneable is broken..
    public Matcher shallowCopyWithMapping(Optional<Mapping> newMapping) {
        return this.cloneInstance()
                .withPatterns(this.patterns)
                .withCaseSensitive(this.caseSensitive)
                .withMapping(newMapping);
    }
    protected abstract Matcher cloneInstance();
}

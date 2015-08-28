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
public class Matcher {
    public enum UrlPart { hostname, path, querystring };

    private String name;
    private Matcher.UrlPart urlpart;
    private List<Pattern> patterns;
    private Optional<Mapping> mapping;
    private Boolean caseSensitive;

    public String getName() { return name; }

    public UrlPart getUrlPart() { return urlpart; }

    public List<Pattern> getPatterns() { return patterns; }
    public Matcher withPatterns(List<Pattern> patterns) { this.patterns = patterns; return this; }

    public Optional<Mapping> getMapping() { return mapping; }
    public Matcher withMapping(Optional<Mapping> mapping) { this.mapping = mapping; return this; }

    public Boolean getCaseSensitive() { return caseSensitive; }
    public Matcher withCaseSensitive(Boolean caseSensitive) { this.caseSensitive = caseSensitive; return this; }

    protected Matcher(String name, UrlPart urlpart) {
        this.name = name;
        this.urlpart = urlpart;
    }

    public Optional<Locale> detect(URL url) {
        return Optional.empty();
    }

    // So I've heard Cloneable is broken..
    public Matcher shallowCopyWithMapping(Optional<Mapping> newMapping) {
        return new Matcher(this.name, this.urlpart)
                .withPatterns(this.patterns)
                .withCaseSensitive(this.caseSensitive)
                .withMapping(newMapping);
    }

}

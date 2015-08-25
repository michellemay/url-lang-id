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

import com.google.common.collect.Lists;
import com.michellemay.mappings.Mapping;
import com.michellemay.mappings.MappingsFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Michel Lemay
 */
public class MatchersFactory {
    private MappingsFactory mappingsFactory;
    private HashMap<String, Matcher> matchers;

    public Map<String, Matcher> getMatchers() { return matchers; }

    public MatchersFactory(List<MatcherConfig> matchersConfig, MappingsFactory mappingsFactory) {
        this.mappingsFactory = mappingsFactory;
        this.matchers = new HashMap<String, Matcher>();

        for (MatcherConfig matcherConfig : matchersConfig) {
            addMatcher(createMacher(matcherConfig));
        }
    }

    private void addMatcher(Matcher matcher) {
        if (matchers.containsKey(matcher.getName())) {
            throw new IllegalStateException("A matcher name '" + matcher.getName() + "' already exists!");
        }
        matchers.put(matcher.getName(), matcher);
    }

    private Matcher createMacher(MatcherConfig matcherConfig) {
        if (StringUtils.isBlank(matcherConfig.name)) {
            throw new IllegalArgumentException("Blank matcher name!");
        }

        if (matcherConfig.urlpart == null) {
            throw new IllegalArgumentException("Matcher must apply to an urlpart!");
        }

        Optional<Mapping> mapping = Optional.empty();
        if (matcherConfig.mapping != null) {
            if (StringUtils.isBlank(matcherConfig.mapping) || !mappingsFactory.getMappings().containsKey(matcherConfig.mapping)) {
                throw new IllegalStateException("Mapping '" + matcherConfig.mapping + "' does not exists!");
            }
            mapping = Optional.of(mappingsFactory.getMappings().get(matcherConfig.mapping));
        }

        if (matcherConfig.patterns == null || matcherConfig.patterns.isEmpty()) {
            throw new IllegalArgumentException("Matcher must have non-empty patterns list!");
        }

        int flags = matcherConfig.casesensitive ? 0 : Pattern.CASE_INSENSITIVE;
        ArrayList<Pattern> patterns = Lists.newArrayList();
        matcherConfig.patterns.forEach((patternStr) -> {
            // Must contain a capturing group named 'lang'. ex: (?<lang>\w+)
            if (!patternStr.contains("(?<lang>")) {
                throw new IllegalArgumentException("Matcher pattern '" + patternStr + "' must have a capturing group named 'lang'!");
            }
            patterns.add(Pattern.compile(patternStr, flags));
        });

        return new Matcher(matcherConfig.name, matcherConfig.urlpart)
                .withPatterns(patterns)
                .withCaseSensitive(matcherConfig.casesensitive)
                .withMapping(mapping);
    }
}

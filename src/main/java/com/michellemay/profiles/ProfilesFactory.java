/*
 * Copyright 2011 Michel Lemay
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

package com.michellemay.profiles;

import com.google.common.collect.Iterables;
import com.michellemay.mappings.Mapping;
import com.michellemay.mappings.MappingsFactory;
import com.michellemay.matchers.Matcher;
import com.michellemay.matchers.MatchersFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Michel Lemay
 */
public class ProfilesFactory {
    private MappingsFactory mappingsFactory;
    private MatchersFactory matchersFactory;

    private HashMap<String, Profile> profiles;

    public Map<String, Profile> getProfiles() { return profiles; }

    public ProfilesFactory(List<ProfileConfig> profilesConfig, MatchersFactory matchersFactory, MappingsFactory mappingsFactory) {
        this.mappingsFactory = mappingsFactory;
        this.matchersFactory = matchersFactory;
        this.profiles = new HashMap<String, Profile>();

        for (ProfileConfig profileConfig : profilesConfig) {
            addProfile(createProfile(profileConfig));
        }
    }

    private void addProfile(Profile profile) {
        if (profiles.containsKey(profile.getName())) {
            throw new IllegalStateException("A profile name '" + profile.getName() + "' already exists!");
        }
        profiles.put(profile.getName(), profile);
    }

    private Profile createProfile(ProfileConfig profileConfig) {
        if (StringUtils.isBlank(profileConfig.name)) {
            throw new IllegalArgumentException("Blank profile name!");
        }

        Mapping tempMapping = null;
        if (profileConfig.mapping != null) {
            if (StringUtils.isBlank(profileConfig.mapping) || !mappingsFactory.getMappings().containsKey(profileConfig.mapping)) {
                throw new IllegalStateException("Mapping '" + profileConfig.mapping + "' does not exists!");
            }
            tempMapping = mappingsFactory.getMappings().get(profileConfig.mapping);
        }
        Optional<Mapping> defaultMapping = Optional.ofNullable(tempMapping);

        if (profileConfig.domains == null || profileConfig.domains.isEmpty()) {
            throw new IllegalArgumentException("Profile must have non-empty domains list!");
        }

        ArrayList<Pattern> domains = Lists.newArrayList();
        profileConfig.domains.forEach((patternStr) -> {
            // Domain names are case insensitive.
            domains.add(Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE));
        });

        ArrayList<Matcher> matchers = Lists.newArrayList();
        profileConfig.matchers.forEach((matcherRef) -> {
            String mappingName = matcherRef.mapping;
            Optional<Mapping> localMapping = Optional.empty();
            if (!StringUtils.isBlank(mappingName)) {
                if (!mappingsFactory.getMappings().containsKey(mappingName)) {
                    throw new IllegalStateException("Mapping '" + mappingName + "' does not exists!");
                }
                localMapping = Optional.of(mappingsFactory.getMappings().get(mappingName));
            }

            String matcherName = matcherRef.matcher;
            if (StringUtils.isBlank(matcherName) || !matchersFactory.getMatchers().containsKey(matcherName)) {
                throw new IllegalStateException("Matcher '" + matcherName + "' does not exists!");
            }
            Matcher matcher = matchersFactory.getMatchers().get(matcherName);

            // Validate that a matcher has a mapping.
            // Select in order of appearance: localMapping, defaultMapping, matcher.mapping.
            Optional<Mapping> finalMapping = Stream.of(localMapping, defaultMapping, matcher.getMapping())
                    .filter(Optional::isPresent)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Matcher '" + matcherName + "' does not have a valid mapping! (Profile '" + profileConfig.name + "')"));

            matchers.add(matcher.shallowCopyWithMapping(finalMapping));
        });

        return new Profile(profileConfig.name).withDomains(domains).withMatchers(matchers);
    }

}

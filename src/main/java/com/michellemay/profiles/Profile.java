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

import com.michellemay.matchers.Matcher;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author Michel Lemay
 */
public class Profile {
    private String name;
    private List<Pattern> domains;
    private List<Matcher> matchers;

    public String getName() { return name; }

    public List<Pattern> getDomains() { return domains; }
    public Profile withDomains(List<Pattern> domains) { this.domains = domains; return this; }

    public List<Matcher> getMatchers() { return matchers; }
    public Profile withMatchers(ArrayList<Matcher> matchers) { this.matchers = matchers; return this; }

    protected Profile(String name) {
        this.name = name;
    }

    public Optional<Locale> detect(URL url) {
        return matchers
                .stream()
                .map((matcher) -> matcher.detect(url))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    public boolean match(String host) {
        return domains
                .stream()
                .anyMatch((p) -> p.matcher(host).matches());
    }
}

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

package com.michellemay;

import com.michellemay.profiles.Profile;
import com.michellemay.profiles.ProfilesFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;

/**
 * URL language detector impl.
 * 
 * @author Michel Lemay
 */
public class URLLanguageDetectorImpl implements URLLanguageDetector {
    private ProfilesFactory profilesFactory;

    /**
     * Instantiates a new URL language detector impl.
     *
     * @param profilesFactory the profiles factory
     */
    URLLanguageDetectorImpl(ProfilesFactory profilesFactory) {
        this.profilesFactory = profilesFactory;
    }

    @Override
    public Optional<Locale> detect(String url) {
        Optional<Locale> lang = Optional.empty();

        try {
            // First, make sure we have a valid url
            URL parsedURL = new URL(url);

            // Select matching profile
            Optional<Profile> profile = profilesFactory.findProfileForHost(parsedURL.getHost());

            // Execute matchers
            if (profile.isPresent()) {
                lang = profile.get().detect(parsedURL);
            }

        } catch (MalformedURLException e) {
            // Malformed url, cannot detect.
        }

        return lang;
    }
}

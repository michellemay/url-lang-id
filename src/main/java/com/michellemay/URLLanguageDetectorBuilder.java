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

import com.michellemay.config.Config;
import com.michellemay.mappings.MappingsFactory;
import com.michellemay.matchers.MatchersFactory;
import com.michellemay.profiles.ProfilesFactory;

/**
 * @author Michel Lemay
 */
public class URLLanguageDetectorBuilder {
    private MappingsFactory mappingsFactory;
    private MatchersFactory matchersFactory;
    private ProfilesFactory profilesFactory;

    public static URLLanguageDetectorBuilder create(Config config) {
        return new URLLanguageDetectorBuilder(config);
    }

    private URLLanguageDetectorBuilder(Config config)
    {
        this.mappingsFactory = new MappingsFactory(config.mappings);
        this.matchersFactory = new MatchersFactory(config.matchers, this.mappingsFactory);
        this.profilesFactory = new ProfilesFactory(config.profiles, this.matchersFactory, this.mappingsFactory);
    }

    public URLLanguageDetector create() {
        return new URLLanguageDetectorImpl(profilesFactory);
    }
}

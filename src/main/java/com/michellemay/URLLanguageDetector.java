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

import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Optional;

/**
 * Guesses the language of an URL.
 *
 * @author Michel Lemay
 */
public interface URLLanguageDetector {

    /**
     * @param url Url to detect language from.
     * @return The language if confident, absent if unknown or not confident enough.
     */
    Optional<Locale> detect(String url);
    
}

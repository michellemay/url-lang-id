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

import com.michellemay.config.ConfigReader;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.tuple.Pair;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
public class URLLanguageDetectorTest {
    private List<Pair<String,String>> testCasesDefault = Arrays.asList(
            // Should detect english (Case insensitive by default)
            Pair.of("http://en.test.com/", "en"),
            Pair.of("http://ENG.test.com/", "en"),
            Pair.of("http://www.test.com/en/index.html", "en"),
            Pair.of("http://www.test.com/ENG/index.html", "en"),
            Pair.of("http://www.test.com/en/longer/Path//index.html", "en"),
            Pair.of("http://www.test.com/path/index.html?lang=en", "en"),
            Pair.of("http://www.test.com/path/index.html?lang=en-US", "en_US"),
            Pair.of("http://www.test.com/path/index.html?lang=fr_ca", "fr_CA"),
            Pair.of("http://www.test.com/path/index.html?lang=french", "fr"),
            Pair.of("http://fr:fr@www.test.com:8080/path/index.html?lang=en", "en"),
            Pair.of("http://www.test.com/path/index.html?q=fr&lang=en&lang=fr", "en"),  // Multiple lang=.. parts

            // Must not detect language:
            // Empty or invalid url
            Pair.of("", ""),
            Pair.of("http//en.test.com", ""),
            // Domain too short
            Pair.of("http://en.com/", ""),
            Pair.of("http://fr/", ""),
            // Use of %ca escape sequence should not match catalan
            Pair.of("http://test.com/?lang=%ca%80", ""),
            // Last path element not supported by default.
            Pair.of("http://www.test.com/path/en/index.html", "")
    );

    private List<Pair<String,String>> testCasesTest = Arrays.asList(
            // docs/ path is case insensitive (but language name is)
            Pair.of("http://mystuff.stuff/docs/ANGLAIS/index.html", "en"),
            Pair.of("http://mystuff.stuff/docs/ANGLAIS/INDEX.html", "en"),

            // KB/ path is case sensitive
            Pair.of("http://mystuff.stuff/KB/es/index.html", "es"),
            Pair.of("http://mystuff.stuff/kb/es/index.html", ""),

            // Different querystring params (case sensitive)
            Pair.of("http://mystuff.stuff?cv_lang=fr", "fr"),
            Pair.of("http://mystuff.stuff?CV_LANG=fr", ""),
            Pair.of("http://mystuff.stuff?language=fr", "fr"),
            Pair.of("http://mystuff.stuff?LANGUAGE=fr", ""),

            // Different querystring params (case insensitive)
            Pair.of("http://mystuff.stuff?lr=ita", "it"),
            Pair.of("http://mystuff.stuff?LR=français", "fr"),

            // Match on second profile
            Pair.of("http://other.com?lr=it", "it"),
            Pair.of("http://OTHER.COM?LR=es", "es")
    );

    private URLLanguageDetector makeNewDetector(String configFile) throws IOException {
        URLLanguageDetectorBuilder builder = URLLanguageDetectorBuilder.create(ConfigReader.readBuiltIn(configFile));
        return builder.create();
    }

    public void validateTestCases(URLLanguageDetector detector, List<Pair<String,String>> testCases) throws Exception {
        List<Pair<Pair<String,String>,Optional<Locale>>> failedTests = testCases
                .stream()
                .map((test) -> Pair.of(test, detector.detect(test.getKey())))
                .filter((testAndResult) -> {
                    Pair<String, String> test = testAndResult.getKey();
                    Optional<Locale> detectedLanguage = testAndResult.getValue();
                    boolean localTestFailed = (detectedLanguage.isPresent() != !test.getValue().isEmpty());
                    if (!localTestFailed && detectedLanguage.isPresent()) {
                        localTestFailed = !detectedLanguage.get().equals(LocaleUtils.toLocale(test.getValue()));
                    }
                    return localTestFailed;
                }).collect(Collectors.toList());

        failedTests.forEach((test) -> System.out.println("FAILED: " + test.getKey() + ", FOUND: " + test.getValue()));
        assertTrue(failedTests.isEmpty());

    }

    @Test
    public void validateTestCasesDefault() throws Exception {
        validateTestCases(makeNewDetector(ConfigReader.DEFAULT_CONFIG), testCasesDefault);
    }

    @Test
    public void validateTestCasesTest() throws Exception {
        validateTestCases(makeNewDetector(ConfigReader.TEST_CONFIG), testCasesTest);
    }

}

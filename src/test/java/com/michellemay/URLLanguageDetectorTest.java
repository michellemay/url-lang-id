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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.michellemay.config.ConfigReader;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Unit test for simple App.
 */
public class URLLanguageDetectorTest {
    public List<Pair<String,String>> testCases = Lists.newArrayList(
            // Should detect english
            Pair.of("http://en.test.com/", "en"),
            Pair.of("http://www.test.com/en/index.html", "en"),
            Pair.of("http://www.test.com/path/index.html?lang=en", "en"),
            Pair.of("http://www.test.com/path/en/index.html", "en"),
            Pair.of("http://fr:fr@www.test.com:8080/path/index.html?lang=en", "en"),
            // Must not detect language:
            // Use of %ca escape sequence should not match catalan
            Pair.of("http://test.com/?lang=%ca%80", ""),
            // Domain too short
            Pair.of("http://en.com/", "")
    );


    private URLLanguageDetector makeNewDetector() throws IOException {
        URLLanguageDetectorBuilder builder = URLLanguageDetectorBuilder.create(ConfigReader.readBuiltIn());
        return builder.create();
    }

    @Test
    public void testEmptyURL() throws Exception {
        URLLanguageDetector detector = makeNewDetector();
        assertFalse(detector.detect("").isPresent());
    }

    @Test
    public void validateTestCases() throws Exception {
        URLLanguageDetector detector = makeNewDetector();

        List<Pair<Pair<String,String>,Optional<Locale>>> failedTests = testCases
                .stream()
                .map((test) -> Pair.of(test, detector.detect(test.getKey())))
                .filter((testAndResult) -> {
                    Pair<String, String> test = testAndResult.getKey();
                    Optional<Locale> detectedLanguage = testAndResult.getValue();
                    boolean localTestFailed = (detectedLanguage.isPresent() != !test.getValue().isEmpty());
                    if (!localTestFailed && detectedLanguage.isPresent()) {
                        localTestFailed = detectedLanguage.get().equals(LocaleUtils.toLocale(test.getValue()));
                    }
                    return localTestFailed;
                }).collect(Collectors.toList());

        failedTests.forEach((test) -> System.out.println("FAILED: " + test.getKey() + ", FOUND: " + test.getValue()));
        assertTrue(failedTests.isEmpty());

    }

}

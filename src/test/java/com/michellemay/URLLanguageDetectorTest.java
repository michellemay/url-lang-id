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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class URLLanguageDetectorTest extends TestCase {
    // Should detect english:
    //  http://en.test.com/
    //  http://www.test.com/en/index.html
    //  http://www.test.com/path/index.html?lang=en
    //  http://fr:fr@www.test.com:8080/path/index.html?lang=en

    // Support %nn values

    // By default: must not detect language:
    //  http://en.com/
    //  http://www.test.com/path/en/index.html


    private URLLanguageDetector makeNewDetector() throws IOException {
        URLLanguageDetectorBuilder builder = URLLanguageDetectorBuilder.create(ConfigReader.readBuiltIn());
        URLLanguageDetector detector = builder.create();
        return detector;
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public URLLanguageDetectorTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(URLLanguageDetectorTest.class);
    }

    public void testEmptyURL() throws Exception {
        URLLanguageDetector detector = makeNewDetector();
        assertFalse(detector.detect("").isPresent());
    }
}

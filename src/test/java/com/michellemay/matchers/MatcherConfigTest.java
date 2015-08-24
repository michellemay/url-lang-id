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

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import java.util.Arrays;

/**
 * MatcherConfig Tester.
 *
 * @author Michel Lemay
 * @version 1.0
 */
public class MatcherConfigTest extends TestCase {
    public MatcherConfigTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRead() throws Exception {
        String json = "{\n" +
                "      \"name\":\"myquerystring\",\n" +
                "      \"urlpart\":\"querystring\",\n" +
                "      \"patterns\":[\"(cv_)?lang(uage)?=(?<lang>.*)\",\"loc=(?<lang>.*)\"],\n" +
                "      \"casesensitive\":\"true\"\n" +
                "    }";

        ObjectMapper mapper = JsonFactory.create();
        MatcherConfig matcher = mapper.fromJson(json, MatcherConfig.class);

        assertEquals(matcher.name, "myquerystring");
        assertEquals(matcher.urlpart, Matcher.UrlPart.querystring);
        assertTrue(matcher.patterns.size() == 2 && matcher.patterns.containsAll(Arrays.asList("(cv_)?lang(uage)?=(?<lang>.*),loc=(?<lang>.*)".split(","))));
        assertTrue(matcher.casesensitive);
    }

    public void testReadWithMissingValues() throws Exception {
        String json = "{\n" +
                "      \"name\":\"myquerystring\",\n" +
                "      \"urlpart\":\"path\",\n" +
                "      \"patterns\":[],\n" +
                "    }";

        ObjectMapper mapper = JsonFactory.create();
        MatcherConfig matcher = mapper.fromJson(json, MatcherConfig.class);

        assertEquals(matcher.name, "myquerystring");
        assertEquals(matcher.urlpart, Matcher.UrlPart.path);
        assertTrue(matcher.patterns.size() == 0);
        assertTrue(!matcher.casesensitive);
    }

    public static Test suite() {
        return new TestSuite(MatcherConfigTest.class);
    }
} 

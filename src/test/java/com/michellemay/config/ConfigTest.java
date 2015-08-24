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

package com.michellemay.config;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Config Tester.
 *
 * @author Michel Lemay
 * @version 1.0
 */
public class ConfigTest extends TestCase {
    public ConfigTest(String name) {
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
                "  \"mappings\":[\n" +
                "    {\n" +
                "      \"name\":\"all\",\n" +
                "      \"extend\":[\"ISO-639-ALPHA-2\",\"ISO-639-ALPHA-3\",\"LANGUAGE_TAGS\"]\n" +
                "    }\n" +
                "  ],\n" +
                "\n" +
                "  \"matchers\":[\n" +
                "    {\n" +
                "      \"name\":\"hostname\",\n" +
                "      \"urlpart\":\"hostname\",\n" +
                "      \"patterns\":[\"(?<lang>[^\\\\.]+\\\\..*\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"path\",\n" +
                "      \"urlpart\":\"path\",\n" +
                "      \"patterns\":[\"/(?<lang>[^/]+)/.*\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"querystring\",\n" +
                "      \"urlpart\":\"querystring\",\n" +
                "      \"patterns\":[\"lang(uage)?=(?<lang>.*)\"]\n" +
                "    }\n" +
                "  ],\n" +
                "\n" +
                "  \"profiles\": [\n" +
                "    {\n" +
                "      \"name\":\"default\",\n" +
                "      \"domains\":[\".*\"],\n" +
                "      \"mapping\":\"ISO-639-ALPHA-2\",\n" +
                "      \"matchers\":[\n" +
                "        {\n" +
                "          \"matcher\":\"hostname\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"matcher\":\"path\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"matcher\":\"querystring\",\n" +
                "          \"mapping\":\"all\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n" +
                "\n";

        ObjectMapper mapper = new ObjectMapper();
        Config config = mapper.readValue(json, Config.class);

        assertEquals(config.mappings.size(), 1);
        assertEquals(config.matchers.size(), 3);
        assertEquals(config.profiles.size(), 1);
    }

    public static Test suite() {
        return new TestSuite(ConfigTest.class);
    }
} 

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

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class ConfigReaderTest {
    @Test
    public void testReadBuiltInDefault() throws Exception {
        // Coverage:
        new ConfigReader();

        Config config1 = ConfigReader.readBuiltIn();
        assertEquals(config1.mappings.size(), 2);
        assertEquals(config1.matchers.size(), 3);
        assertEquals(config1.profiles.size(), 1);
    }

    @Test
    public void testReadBuiltInTest() throws Exception {
        Config config2 = ConfigReader.readBuiltIn(ConfigReader.TEST_CONFIG);
        assertEquals(config2.mappings.size(), 2);
        assertEquals(config2.matchers.size(), 4);
        assertEquals(config2.profiles.size(), 1);
    }

    @Test(expected = IOException.class)
    public void testInvalidResource() throws Exception {
        ConfigReader.readBuiltIn("dummyConfig");
    }

    @Test
    public void testReadFromString() throws Exception {
        String configStr = "{\n" +
                "  \"mappings\":[\n" +
                "    {\n" +
                "      \"name\":\"mytest\",\n" +
                "      \"extend\":[\"ISO-639-ALPHA-2\", \"ISO-639-ALPHA-3\"],\n" +
                "      \"add\":{\"en\":\"english,anglais\",\"es\":\"spanish,espagnol\"},\n" +
                "      \"override\":{\"fr\":\"french,français\"},\n" +
                "      \"filter\":\"en,fr-*,de,es,it\",\n" +
                "      \"casesensitive\":\"true\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"mytest2\",\n" +
                "      \"extend\":[\"ISO-639-ALPHA-2\"]\n" +
                "    }\n" +
                "  ],\n" +
                "\n" +
                "  \"matchers\":[\n" +
                "    {\n" +
                "      \"name\":\"mypathregex\",\n" +
                "      \"urlpart\":\"path\",\n" +
                "      \"patterns\":[\"/docs/(?<lang>[^/]+)/index\\\\.html\"],\n" +
                "      \"mapping\":\"mytest\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"myquerystring\",\n" +
                "      \"urlpart\":\"querystring\",\n" +
                "      \"patterns\":[\"(cv_)?lang(uage)?=(?<lang>.*)\",\"loc=(?<lang>.*)\"],\n" +
                "      \"casesensitive\":\"true\"\n" +
                "    }\n" +
                "  ],\n" +
                "\n" +
                "  \"profiles\": [\n" +
                "    {\n" +
                "      \"name\":\"custom\",\n" +
                "      \"domains\":[\"mystuff.stuff\",\"my.*\\\\.stuff\"],\n" +
                "      \"mapping\":\"mytest\",\n" +
                "      \"matchers\":[\n" +
                "        {\n" +
                "          \"matcher\":\"mypathregex\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"matcher\":\"myquerystring\",\n" +
                "          \"mapping\":\"mytest2\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
        ConfigReader.read(configStr);
    }

}

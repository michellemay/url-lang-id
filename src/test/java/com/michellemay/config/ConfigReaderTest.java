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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class ConfigReaderTest {
    String configStr = "{\n" +
            "  \"mappings\":[\n" +
            "    {\n" +
            "      \"name\": \"mytest\"\n" +
            "    }\n" +
            "  ],\n" +
            "\n" +
            "  \"matchers\":[\n" +
            "    {\n" +
            "      \"name\":\"myquerystring\",\n" +
            "      \"urlpart\":\"querystring\",\n" +
            "      \"patterns\":[\"loc=(?<lang>.*)\"]\n" +
            "    }\n" +
            "  ],\n" +
            "\n" +
            "  \"profiles\": [\n" +
            "    {\n" +
            "      \"name\":\"custom\",\n" +
            "      \"domains\":[\"mystuff.stuff\"],\n" +
            "      \"matchers\":[\n" +
            "        {\n" +
            "          \"matcher\":\"myquerystring\",\n" +
            "          \"mapping\":\"mytest2\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

    @Test
    public void testReadBuiltInDefault() throws Exception {
        // Coverage:
        new ConfigReader();

        Config config = ConfigReader.readBuiltIn();
        assertEquals(config.mappings.size(), 2);
        assertEquals(config.matchers.size(), 3);
        assertEquals(config.profiles.size(), 1);
    }

    @Test
    public void testReadBuiltInTest() throws Exception {
        Config config = ConfigReader.readBuiltIn(ConfigReader.TEST_CONFIG);
        assertEquals(config.mappings.size(), 2);
        assertEquals(config.matchers.size(), 4);
        assertEquals(config.profiles.size(), 2);
    }

    @Test(expected = IOException.class)
    public void testInvalidResource() throws Exception {
        ConfigReader.readBuiltIn("dummyConfig");
    }

    @Test
    public void testReadFromString() throws Exception {
        Config config = ConfigReader.read(configStr);
        assertEquals(config.mappings.size(), 1);
        assertEquals(config.matchers.size(), 1);
        assertEquals(config.profiles.size(), 1);
    }

    @Test
    public void testReadFromInputStream() throws Exception {
        InputStream stream = new ByteArrayInputStream(configStr.getBytes(StandardCharsets.UTF_8));
        Config config = ConfigReader.read(stream);
        assertEquals(config.mappings.size(), 1);
        assertEquals(config.matchers.size(), 1);
        assertEquals(config.profiles.size(), 1);
    }

    @Test
    public void testReadFromInputStreamU16() throws Exception {
        InputStream stream = new ByteArrayInputStream(configStr.getBytes(StandardCharsets.UTF_16));
        Config config = ConfigReader.read(stream, "utf-16");
        assertEquals(config.mappings.size(), 1);
        assertEquals(config.matchers.size(), 1);
        assertEquals(config.profiles.size(), 1);
    }
}

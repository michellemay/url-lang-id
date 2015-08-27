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
import com.michellemay.config.ConfigReader;

import org.junit.Test;

/**
* URLLanguageDetectorBuilder Tester. 
* 
* @author Michel Lemay
*/
public class URLLanguageDetectorBuilderTest {
    @Test
    public void testBuiltInConfigs() throws Exception {
        URLLanguageDetectorBuilder.create(ConfigReader.readBuiltIn());
        URLLanguageDetectorBuilder.create(ConfigReader.readBuiltIn(ConfigReader.TEST_CONFIG));
    }

    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testCreateWithInvalidConfig1() throws Exception {
        String configStr = "";
        Config config = ConfigReader.read(configStr);
        URLLanguageDetectorBuilder.create(config);
    }

    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testCreateWithInvalidConfig2() throws Exception {
        String configStr = "{ \"mappings\":[ }";
        Config config = ConfigReader.read(configStr);
        URLLanguageDetectorBuilder.create(config);
    }

    @Test
    public void testCreateEmptyConfig() throws Exception {
        String configStr = "{ }";  // Valid but not quite useful
        Config config = ConfigReader.read(configStr);
        URLLanguageDetectorBuilder.create(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidConfig() throws Exception {
        String configStr = "{ \"matchers\":[{}]}";  // matchers must have a name
        Config config = ConfigReader.read(configStr);
        URLLanguageDetectorBuilder.create(config);
    }
}

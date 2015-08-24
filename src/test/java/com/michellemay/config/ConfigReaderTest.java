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
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ConfigReaderTest extends TestCase {
    private static final String TEST_CONFIG = "test-profiles.json";

    public ConfigReaderTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Method: readBuiltIn()
     */
    public void testReadBuiltIn() throws Exception {
        Config config1 = ConfigReader.readBuiltIn();
        assertEquals(config1.mappings.size(), 1);
        assertEquals(config1.matchers.size(), 3);
        assertEquals(config1.profiles.size(), 1);

        Config config2 = ConfigReader.readBuiltIn(TEST_CONFIG);
        assertEquals(config2.mappings.size(), 2);
        assertEquals(config2.matchers.size(), 2);
        assertEquals(config2.profiles.size(), 1);
    }


    public static Test suite() {
        return new TestSuite(ConfigReaderTest.class);
    }
}

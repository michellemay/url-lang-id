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

package com.michellemay.profiles;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

/**
 * ProfileConfig Tester.
 *
 * @author Michel Lemay
 * @version 1.0
 */
public class ProfileConfigTest extends TestCase {
    public ProfileConfigTest(String name) {
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
                "      \"name\":\"profile1\",\n" +
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
                "    }";

        ObjectMapper mapper = new ObjectMapper();
        ProfileConfig profile = mapper.readValue(json, ProfileConfig.class);

        assertEquals(profile.name, "profile1");
        String[] domains = "mystuff.stuff,my.*\\.stuff".split(",");
        assertTrue(profile.domains.size() == 2 && profile.domains.containsAll(Arrays.asList(domains)));
        assertEquals(profile.mapping, "mytest");
        assertTrue(profile.matchers.size() == 2);
        assertEquals(profile.matchers.get(0).matcher, "mypathregex");
        assertEquals(profile.matchers.get(1).matcher, "myquerystring");
        assertEquals(profile.matchers.get(1).mapping, "mytest2");
    }

    public static Test suite() {
        return new TestSuite(ProfileConfigTest.class);
    }
} 

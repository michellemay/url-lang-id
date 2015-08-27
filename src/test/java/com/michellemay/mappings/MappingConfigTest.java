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

package com.michellemay.mappings;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

/**
 * MappingConfig Tester.
 *
 * @author Michel Lemay
 */
public class MappingConfigTest {
    @Test
    public void testRead() throws Exception {
        String json = "{\n" +
                "      \"name\":\"mytest\",\n" +
                "      \"extend\":[\"ISO-639-ALPHA-2\", \"ISO-639-ALPHA-3\"],\n" +
                "      \"add\":{\"en\":\"english,anglais\",\"es\":\"spanish,espagnol\"},\n" +
                "      \"override\":{\"fr\":\"french,français\"},\n" +
                "      \"filter\":[\"en\",\"fr\",\"de\",\"es\",\"it\"],\n" +
                "      \"casesensitive\":\"true\"\n" +
                "    }";

        ObjectMapper mapper = new ObjectMapper();
        MappingConfig mapping = mapper.readValue(json, MappingConfig.class);

        assertEquals(mapping.name, "mytest");
        assertTrue(mapping.extend.size() == 2 && mapping.extend.containsAll(Arrays.asList("ISO-639-ALPHA-2,ISO-639-ALPHA-3".split(","))));
        assertEquals(mapping.add.size(), 2);
        assertTrue(mapping.add.containsKey("en") && mapping.add.get("en").equals("english,anglais"));
        assertTrue(mapping.add.containsKey("es") && mapping.add.get("es").equals("spanish,espagnol"));
        assertEquals(mapping.override.size(), 1);
        assertTrue(mapping.override.containsKey("fr") && mapping.override.get("fr").equals("french,français"));
        assertTrue(mapping.filter.size() == 5 && mapping.filter.containsAll(Arrays.asList("en,fr,de,es,it".split(","))));
        assertTrue(mapping.casesensitive);
    }
}

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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

/**
 * MappingsFactory Tester.
 *
 * @author Michel Lemay
 */
public class MappingsFactoryTest {
    @Test
    public void testDefaultMappings() throws Exception {
        MappingsFactory f = new MappingsFactory(Collections.emptyList());

        assertEquals(f.getMappings().size(), 4);

        assertTrue(f.getMappings().containsKey("ISO-639-ALPHA-2"));
        assertEquals(f.getMappings().get("ISO-639-ALPHA-2").getMapping().get("en"), LocaleUtils.toLocale("en"));

        assertTrue(f.getMappings().containsKey("ISO-639-ALPHA-3"));
        assertEquals(f.getMappings().get("ISO-639-ALPHA-3").getMapping().get("eng"), LocaleUtils.toLocale("en"));

        assertTrue(f.getMappings().containsKey("LANGUAGE_TAGS"));
        assertEquals(f.getMappings().get("LANGUAGE_TAGS").getMapping().get("en-US"), LocaleUtils.toLocale("en_US"));

        assertTrue(f.getMappings().containsKey("ENGLISH_NAMES"));
        assertEquals(f.getMappings().get("ENGLISH_NAMES").getMapping().get("french"), LocaleUtils.toLocale("fr"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyMapping() throws Exception {
        MappingConfig config = new MappingConfig();
        new MappingsFactory(Collections.singletonList(config));
    }

    @Test
    public void testCreateSimpleMapping() throws Exception {
        // Mapping must have at least a name
        MappingConfig config = new MappingConfig();
        config.name = "test";
        config.casesensitive = true;
        MappingsFactory f = new MappingsFactory(Collections.singletonList(config));
        assertTrue(f.getMappings().containsKey("test"));
        assertTrue(f.getMappings().get("test").getCaseSensitive());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateMappingWithMissingExtend() throws Exception {
        MappingConfig config = new MappingConfig();
        config.name = "test";
        config.extend = Collections.singletonList("notfound");
        new MappingsFactory(Collections.singletonList(config));
    }

    @Test
    public void testCreateMappingExtend() throws Exception {
        MappingConfig config = new MappingConfig();
        config.name = "test";
        config.extend = asList("ISO-639-ALPHA-2", "ISO-639-ALPHA-3", "LANGUAGE_TAGS");
        MappingsFactory f = new MappingsFactory(Collections.singletonList(config));
        assertTrue(f.getMappings().containsKey("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMappingInvalidLocaleName() throws Exception {
        MappingConfig config = new MappingConfig();
        config.name = "test";
        config.add = ImmutableMap.of("EN", "english,anglais");
        new MappingsFactory(Collections.singletonList(config));
    }

    @Test
    public void testCreateMappingAdd() throws Exception {
        MappingConfig config = new MappingConfig();
        config.name = "test";
        config.add = ImmutableMap.of("en_US", "english,anglais", "fr", "french, francais");
        MappingsFactory f = new MappingsFactory(Collections.singletonList(config));
        assertTrue(f.getMappings().containsKey("test"));

        Mapping mapping = f.getMappings().get("test");
        assertEquals(mapping.getMapping().get("english"), LocaleUtils.toLocale("en_US"));
        assertEquals(mapping.getMapping().get("anglais"), LocaleUtils.toLocale("en_US"));
        assertEquals(mapping.getMapping().get("french"), LocaleUtils.toLocale("fr"));
        assertEquals(mapping.getMapping().get("francais"), LocaleUtils.toLocale("fr"));
    }

    @Test
    public void testCreateMappingFilterAndOverride() throws Exception {
        MappingConfig config = new MappingConfig();
        config.name = "test";
        config.extend = ImmutableList.of("ISO-639-ALPHA-3");
        config.filter = "en,fr";
        config.override = ImmutableMap.of("fr", "french,francais");
        MappingsFactory f = new MappingsFactory(Collections.singletonList(config));
        assertTrue(f.getMappings().containsKey("test"));

        Mapping mapping = f.getMappings().get("test");
        assertEquals(mapping.getMapping().size(), 3);
        assertEquals(mapping.getMapping().get("eng"), LocaleUtils.toLocale("en")); // Kept by filter
        assertFalse(mapping.getMapping().containsKey("fra")); // Kept by filter but overriden
        assertFalse(mapping.getMapping().containsKey("ita")); // Removed by filter
        assertEquals(mapping.getMapping().get("french"), LocaleUtils.toLocale("fr"));
        assertEquals(mapping.getMapping().get("francais"), LocaleUtils.toLocale("fr"));
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateMapping() throws Exception {
        MappingConfig config = new MappingConfig();
        config.name = "test";
        new MappingsFactory(ImmutableList.of(config, config));
    }
}

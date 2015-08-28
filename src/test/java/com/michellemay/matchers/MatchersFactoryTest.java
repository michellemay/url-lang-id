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

import com.michellemay.mappings.MappingsFactory;
import com.michellemay.mappings.ISO639Alpha2Mapping;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Collections;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * MatchersFactory Tester.
 *
 * @author Michel Lemay
 */
public class MatchersFactoryTest {
    @Test
    public void testEmptyMatchers() throws Exception {
        MatchersFactory f = new MatchersFactory(Collections.emptyList(), new MappingsFactory(Collections.emptyList()));
        assertTrue(f.getMatchers().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMatcherName() throws Exception {
        // Matcher must have a name
        MatcherConfig config = new MatcherConfig();
        new MatchersFactory(Collections.singletonList(config), new MappingsFactory(Collections.emptyList()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMatcherUrlPart() throws Exception {
        // Matcher must have a urlpart
        MatcherConfig config = new MatcherConfig();
        config.name = "test";
        new MatchersFactory(Collections.singletonList(config), new MappingsFactory(Collections.emptyList()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMatcherPatterns() throws Exception {
        // Matcher must have a list of patterns
        MatcherConfig config = new MatcherConfig();
        config.name = "test";
        config.urlpart = Matcher.UrlPart.hostname;
        new MatchersFactory(Collections.singletonList(config), new MappingsFactory(Collections.emptyList()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMatcherPatterns2() throws Exception {
        // Matcher must have a list of patterns with capturing group
        MatcherConfig config = new MatcherConfig();
        config.name = "test";
        config.urlpart = Matcher.UrlPart.hostname;
        config.patterns = ImmutableList.of("(?<invalidgroupname>[^\\.]+\\..*");
        new MatchersFactory(Collections.singletonList(config), new MappingsFactory(Collections.emptyList()));
    }

    @Test(expected = PatternSyntaxException.class)
    public void testInvalidMatcherPatterns3() throws Exception {
        // Matcher must have a list of patterns with capturing group
        MatcherConfig config = new MatcherConfig();
        config.name = "test";
        config.urlpart = Matcher.UrlPart.hostname;
        config.patterns = ImmutableList.of("(?<lang>.*]");
        new MatchersFactory(Collections.singletonList(config), new MappingsFactory(Collections.emptyList()));
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidMapping() throws Exception {
        // If set, mapping name must refer to a valid mapping.
        MatcherConfig config = new MatcherConfig();
        config.name = "test";
        config.urlpart = Matcher.UrlPart.hostname;
        config.patterns = ImmutableList.of("(?<lang>[^\\.]+\\..*)");
        config.mapping = "dummymapping";
        new MatchersFactory(Collections.singletonList(config), new MappingsFactory(Collections.emptyList()));
    }

    @Test
    public void testValidMatcher() throws Exception {
        MatcherConfig config = new MatcherConfig();
        config.name = "test";
        config.urlpart = Matcher.UrlPart.hostname;
        config.patterns = ImmutableList.of("(?<lang>[^\\.]+)\\..*");
        config.mapping = ISO639Alpha2Mapping.NAME;
        MatchersFactory f = new MatchersFactory(Collections.singletonList(config), new MappingsFactory(Collections.emptyList()));
        assertEquals(f.getMatchers().size(), 1);
        assertTrue(f.getMatchers().containsKey("test"));

        Matcher matcher = f.getMatchers().get("test");
        assertFalse(matcher.getCaseSensitive());
        assertEquals(matcher.getUrlPart(), Matcher.UrlPart.hostname);
        assertEquals(matcher.getPatterns().size(), 1);
        assertEquals(matcher.getPatterns().get(0).toString(), "(?<lang>[^\\.]+)\\..*");
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicateMatcher() throws Exception {
        MatcherConfig config = new MatcherConfig();
        config.name = "test";
        config.urlpart = Matcher.UrlPart.hostname;
        config.patterns = ImmutableList.of("(?<lang>[^\\.]+)\\..*");
        new MatchersFactory(ImmutableList.of(config, config), new MappingsFactory(Collections.emptyList()));
    }
}

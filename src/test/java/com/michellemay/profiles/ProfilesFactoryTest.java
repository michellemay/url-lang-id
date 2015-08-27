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

import com.google.common.collect.ImmutableList;
import com.michellemay.mappings.ISO639Alpha2Mapping;
import com.michellemay.mappings.ISO639Alpha3Mapping;
import com.michellemay.mappings.LanguageTagsMapping;
import com.michellemay.mappings.MappingsFactory;
import com.michellemay.matchers.Matcher;
import com.michellemay.matchers.MatcherConfig;
import com.michellemay.matchers.MatchersFactory;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** 
* ProfilesFactory Tester. 
* 
* @author Michel Lemay
*/
public class ProfilesFactoryTest {
   private ProfilesFactory create(List<ProfileConfig> profilesConfig) {
      // Default mappings is enough
      MappingsFactory mappingsFactory = new MappingsFactory(Collections.emptyList());

      // Create some matchers
      MatcherConfig matcher1 = new MatcherConfig();
      matcher1.name = "withmapping";
      matcher1.urlpart = Matcher.UrlPart.hostname;
      matcher1.patterns = ImmutableList.of("(?<lang>[^\\\\.]+)\\\\..*");
      matcher1.mapping = ISO639Alpha2Mapping.NAME;

      MatcherConfig matcher2 = new MatcherConfig();
      matcher2.name = "nomapping";
      matcher2.urlpart = Matcher.UrlPart.hostname;
      matcher2.patterns = ImmutableList.of("(?<lang>[^\\\\.]+)\\\\..*");

      MatchersFactory matchersFactory = new MatchersFactory(ImmutableList.of(matcher1, matcher2), mappingsFactory);

      return new ProfilesFactory(profilesConfig, matchersFactory, mappingsFactory);
   }

   @Test
   public void testEmptyProfiles() throws Exception {
      ProfilesFactory f = create(Collections.emptyList());
      assertTrue(f.getProfiles().isEmpty());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testInvalidProfileName() throws Exception {
      ProfileConfig config = new ProfileConfig();
      config.name = " ";
      create(Collections.singletonList(config));
   }

   @Test(expected = IllegalStateException.class)
   public void testInvalidMappingName() throws Exception {
      ProfileConfig config = new ProfileConfig();
      config.name = "test";
      config.mapping = "dummy";
      create(Collections.singletonList(config));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testInvalidMissingDomains() throws Exception {
      ProfileConfig config = new ProfileConfig();
      config.name = "test";
      create(Collections.singletonList(config));
   }

   @Test(expected = PatternSyntaxException.class)
   public void testInvalidInvalidDomains() throws Exception {
      ProfileConfig config = new ProfileConfig();
      config.name = "test";
      config.domains = ImmutableList.of("[.+)");
      create(Collections.singletonList(config));
   }

   @Test(expected = IllegalStateException.class)
   public void testInvalidMatcherName() throws Exception {
      ProfileConfig config = new ProfileConfig();
      config.name = "test";
      config.domains = ImmutableList.of(".*\\\\.com");
      ProfileConfig.MatcherRef matcher = new ProfileConfig.MatcherRef();
      matcher.matcher = "dummy";
      config.matchers = ImmutableList.of(matcher);
      create(Collections.singletonList(config));
   }

   @Test(expected = IllegalStateException.class)
   public void testInvalidMatcherMapping() throws Exception {
      ProfileConfig config = new ProfileConfig();
      config.name = "test";
      config.domains = ImmutableList.of(".*\\\\.com");
      ProfileConfig.MatcherRef matcher = new ProfileConfig.MatcherRef();
      matcher.matcher = "withmapping";
      matcher.mapping = "dummy";
      config.matchers = ImmutableList.of(matcher);
      create(Collections.singletonList(config));
   }

   @Test(expected = IllegalStateException.class)
   public void testNoMappingForMatcher() throws Exception {
      ProfileConfig config = new ProfileConfig();
      config.name = "test";
      config.domains = ImmutableList.of(".*\\\\.com");
      ProfileConfig.MatcherRef matcher = new ProfileConfig.MatcherRef();
      matcher.matcher = "nomapping";
      config.matchers = ImmutableList.of(matcher);
      create(Collections.singletonList(config));
   }

   @Test
   public void testMatcherMappings1() throws Exception {
      // Use max precedence: matcher local mapping
      ProfileConfig config = new ProfileConfig();
      config.name = "test";
      config.domains = ImmutableList.of(".*\\\\.com");
      config.mapping = LanguageTagsMapping.NAME;
      ProfileConfig.MatcherRef matcher = new ProfileConfig.MatcherRef();
      matcher.matcher = "withmapping";
      matcher.mapping = ISO639Alpha3Mapping.NAME;
      config.matchers = ImmutableList.of(matcher);
      ProfilesFactory f = create(Collections.singletonList(config));
      assertEquals(f.getProfiles().get("test").getMatchers().get(0).getMapping().get().getName(), ISO639Alpha3Mapping.NAME);
      assertTrue(!f.getProfiles().get("test").getDomains().isEmpty());
   }

   @Test
   public void testMatcherMappings2() throws Exception {
      // Use medium precedence: profile default mapping
      ProfileConfig config = new ProfileConfig();
      config.name = "test";
      config.domains = ImmutableList.of(".*\\\\.com");
      config.mapping = LanguageTagsMapping.NAME;
      ProfileConfig.MatcherRef matcher = new ProfileConfig.MatcherRef();
      matcher.matcher = "withmapping";
      config.matchers = ImmutableList.of(matcher);
      ProfilesFactory f = create(Collections.singletonList(config));
      assertEquals(f.getProfiles().get("test").getMatchers().get(0).getMapping().get().getName(), LanguageTagsMapping.NAME);
   }

   @Test
   public void testMatcherMappings3() throws Exception {
      // Use least precedence: matcher default mapping
      ProfileConfig config = new ProfileConfig();
      config.name = "test";
      config.domains = ImmutableList.of(".*\\\\.com");
      ProfileConfig.MatcherRef matcher = new ProfileConfig.MatcherRef();
      matcher.matcher = "withmapping";
      config.matchers = ImmutableList.of(matcher);
      ProfilesFactory f = create(Collections.singletonList(config));
      assertEquals(f.getProfiles().get("test").getMatchers().get(0).getMapping().get().getName(), ISO639Alpha2Mapping.NAME);
   }

   @Test(expected = IllegalStateException.class)
   public void testDuplicateProfiles() throws Exception {
      ProfileConfig config = new ProfileConfig();
      config.name = "test";
      config.domains = ImmutableList.of(".*\\\\.com");
      ProfileConfig.MatcherRef matcher = new ProfileConfig.MatcherRef();
      matcher.matcher = "withmapping";
      config.matchers = ImmutableList.of(matcher);
      ProfilesFactory f = create(ImmutableList.of(config, config));
   }
}

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

import com.google.common.collect.ImmutableList;

import java.net.URL;
import java.util.List;

/**
 * Path matcher.
 * 
 * @author Michel Lemay
 */
public class PathMatcher extends Matcher {
    /**
     * Instantiates a new Path matcher.
     *
     * @param name the name
     */
    public PathMatcher(String name) {
        super(name, UrlPart.hostname);
    }

    @Override
    protected List<String> getParts(URL url) {
        return url.getPath() != null ? ImmutableList.of(url.getPath()) : ImmutableList.of();
    }

    @Override
    protected Matcher cloneInstance() {
        return new PathMatcher(this.getName());
    }
}

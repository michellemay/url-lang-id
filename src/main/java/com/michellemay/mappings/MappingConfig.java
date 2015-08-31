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

import java.util.List;
import java.util.Map;

/**
 * Mapping config.
 *
 * @author Michel Lemay
 */
public class MappingConfig {
    /**
     * The Name.
     */
    public String name;
    /**
     * The Extend.
     */
    public List<String> extend;
    /**
     * The Add.
     */
    public Map<String, String> add;
    /**
     * The Override.
     */
    public Map<String, String> override;
    /**
     * The Filter.
     */
    public String filter;
    /**
     * The Casesensitive.
     */
    public boolean casesensitive = false;
}

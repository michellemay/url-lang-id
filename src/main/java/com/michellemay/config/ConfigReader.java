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

import java.io.*;
import java.nio.charset.Charset;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

/**
 * @author Michel Lemay
 */
public class ConfigReader {
    private static final String DEFAULT_CONFIG = "default-profiles.json";

    /**
     * Read from an InputStream in UTF-8.
     */
    public static Config read(InputStream inputStream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (buffer.length() > 0) {
                    buffer.append(' ');
                }
                buffer.append(line);
            }
        }

        return JsonFactory.create().fromJson(buffer.toString(), Config.class);
    }

    /**
     * Load profiles configuration from the classpath in a specific directory.
     *
     * <p>This is usually used to load built-in profiles, shipped with the jar.</p>
     *
     * @param classLoader the ClassLoader to load the profiles from. Use {@code MyClass.class.getClassLoader()}
     * @param configName profile path inside the classpath.
     */
    public static Config read(ClassLoader classLoader, String configName) throws IOException {
        try (InputStream in = classLoader.getResourceAsStream(configName)) {
            if (in == null) {
                throw new IOException("No profile file available named at " + configName + "!");
            }
            return read(in);
        }
    }

    /**
     * Load builtin profiles configuration from the classpath.
     */
    public static Config readBuiltIn() throws IOException {
        return readBuiltIn(DEFAULT_CONFIG);
    }

    /**
     * Load builtin profiles configuration from the classpath.
     *
     * @param configName profile path inside the classpath.
     */
    public static Config readBuiltIn(String configName) throws IOException {
        String path = configName;
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IOException("No config file available at " + path + "!");
            }
            return read(in);
        }
    }
}

# url-lang-id
Detect common language patterns in URLs.

#### URL pattern matching

- check if domains starts with a language code: fr.site.com
- check if first folder is a language code: site.com/fr/
- check common language query string parameters: site.com?lang=fr
- check custom URL patterns: site.com/path/en_US/document

#### Specs
- Per domain rules specifications.
- Detect ISO 639 languages codes: 2 or 3 letters codes, language_COUNTRY_Variant codes.
- Support custom or localized language names with mappings.
- Languages, query string parameters and other url pattern matching order can be prioritized.
- Extract complete locale description if possible (language, country, variant).
- Json or programmatic configuration with default profiles.

## Maven

```
  <dependency>
    <groupId>com.michellemay</groupId>
    <artifactId>url-lang-id</artifactId>
    <version>0.1.1</version>
  </dependency>
  
```

## Usage

Complete sample with built-in config defaults:

```java
  // Create builder and detector
  URLLanguageDetectorBuilder builder = URLLanguageDetectorBuilder.create(ConfigReader.readBuiltIn());
  URLLanguageDetector detector = builder.create();
  
  // Extract language from url:
  Optional<Locale> lang = detector.detect("http://en.site.org/");
```

### ConfigReader

Read configuration from InputStream:

```java
  InputStream stream = ...
  Config config = ConfigReader.read(stream, "utf-8");
```

Read configuration from String:

```java
  String jsonConfig = "..."
  Config config = ConfigReader.read(jsonConfig);
```


## JSON Configuration

#### Mappings

Mappings attributes:

- "name": Mapping name.
- "extend": List of base mappings to inherit from.
- "add": Adds key-value pairs to the mappings. Keys are locale tags. Values are comma separated list of display names.
- "override" Replace base mappings with new ones.
- "filter": Keep only base mappings which matches languages ranges (RFC 4647).
- "casesensitive": Value matching case sensitiveness (default = false).

Default mappings built from Java Locale:

- ISO-639-ALPHA-2: 2 letters language codes (ex: en, fr).
- ISO-639-ALPHA-3: 3 letters language codes (ex: eng, fra).
- LANGUAGE_TAGS: Common language tags with country codes (ex: en_US, es-PT). Also matches underscores and dashes.
- ENGLISH_NAMES: Language display name in en_US. (ex: english, french, croatian)

### Matchers

Matchers attributes:

- "name": Matcher name.
- "urlpart": Selects which part of the url should be used: "hostname", "path" or "querystring"
- "patterns": Regex patterns to apply on hostname, path or query parameters. Must include a capturing group named 'lang' (ex: (?<lang>\w+))
- "casesensitive": Pattern matching case sensitiveness (default = false)
- "mapping": Sets default language mappings for this matcher.
- "patternorder": Match pattern in the same order as listed.  Otherwise, match using url parts order. (default = true)

Default matchers:

- "hostname": Match language in first hostname sub-part.
- "path": Match language in first path sub-part.
- "querystring": Match language in "language" and "lang" parameters.

### Profiles

Profiles attributes:

- "name": Profile name
- "domains": List of domain name matching patterns.
- "mapping": Default language mappings to use when not specified in matchers.
- "matchers": List of matchers used in order of appearance. Each matcher can use default profile mapping or specify one locally.

Mappings are selected in the following order:
1) Mappings found in "profiles > matchers > mapping"
2) Default mapping set in "profiles > mapping"
3) Default mapping set in "matchers > mapping"

### Example

```json

  {
    "mappings":[
      {
        "name":"all",
        "extend":["ISO-639-ALPHA-2","ISO-639-ALPHA-3","LANGUAGE_TAGS","ENGLISH_NAMES"]
      },
      {
        "name":"ISO-639-CODES",
        "extend":["ISO-639-ALPHA-2","ISO-639-ALPHA-3"]
      }
    ],
  
    "matchers":[
      {
        "name":"hostname",
        "urlpart":"hostname",
        "patterns":["(?<lang>[^\\.]+)(\\.[^\\.]+){2,}"]
      },
      {
        "name":"path",
        "urlpart":"path",
        "patterns":["/(?<lang>[^/]+)(/.*)?"]
      },
      {
        "name":"querystring",
        "urlpart":"querystring",
        "patterns":["lang(uage)?=(?<lang>.*)"]
      }
    ],
  
    "profiles": [
      {
        "name":"default",
        "domains":[".*"],
        "mapping":"ISO-639-CODES",
        "matchers":[
          {
            "matcher":"hostname"
          },
          {
            "matcher":"path"
          },
          {
            "matcher":"querystring",
            "mapping":"all"
          }
        ]
      }
    ]
  }
  
 
```

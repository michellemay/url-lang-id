# url-lang-id
Detect common language patterns in URLs.

# URL pattern matching:
- check if domains starts with a language code: fr.site.com
- check if first folder is a language code: site.com/fr/
- check common language query string parameters: site.com?lang=fr
- check custom URL patterns: site.com/path/en_US/document

# Other requirements:
- Per domain (regex pattern) rules specifications
- Detect ISO 639 languages codes: 2 or 3 letters codes, language_COUNTRY_Variant codes
- Support custom or localized language names with mappings
- Languages, query string parameters and other url pattern matching order can be prioritized
- Extract complete locale description if possible (language, country, variant)
- Json or programmatic configuration with default profiles

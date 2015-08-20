package com.michellemay;

import java.util.Optional;
import java.util.Locale;

/**
 * Guesses the language of an URL.
 *
 * <p>See website for details.</p>
 *
 * @author Michel Lemay
 */
public interface URLLanguageDetector {

    /**
     * @param url Url to detect language from.
     * @return The language if confident, absent if unknown or not confident enough.
     */
    Optional<Locale> detect(CharSequence url);
    
}

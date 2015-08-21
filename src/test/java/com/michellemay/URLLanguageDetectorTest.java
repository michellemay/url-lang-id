package com.michellemay;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class URLLanguageDetectorTest 
    extends TestCase
{
    // Should detect english:
    //  http://en.test.com/
    //  http://www.test.com/en/index.html
    //  http://www.test.com/path/index.html?lang=en
    //  http://fr:fr@www.test.com:8080/path/index.html?lang=en

    // Support %nn values

    // By default: must not detect language:
    //  http://en.com/
    //  http://www.test.com/path/en/index.html


    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public URLLanguageDetectorTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( URLLanguageDetectorTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}

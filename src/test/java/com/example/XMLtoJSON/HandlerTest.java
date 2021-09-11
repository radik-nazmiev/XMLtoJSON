package com.example.XMLtoJSON;

import org.junit.jupiter.api.Test;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class HandlerTest {
    private String referenceString = "{\r\n" +
            "  \"root\" : {\r\n" +
            "    \"value\" : \"6\",\r\n" +
            "    \"nodeA\" : {\r\n" +
            "      \"value\" : \"1\"\r\n" +
            "    },\r\n" +
            "    \"nodeB\" : {\r\n" +
            "      \"value\" : \"5\",\r\n" +
            "      \"nodeC\" : {\r\n" +
            "        \"value\" : \"2\"\r\n" +
            "      },\r\n" +
            "      \"nodeD\" : {\r\n" +
            "        \"value\" : \"3\"\r\n" +
            "      }\r\n" +
            "    }\r\n" +
            "  }\r\n" +
            "}";

    @Test
    void startTest() {
        try {
            File file = new File("src/test.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SaxXmlHandler handler = new SaxXmlHandler();
            handler.setSaxHmlHandlerListener(new SaxHmlHandlerListener() {
                @Override
                public void onComplete(String json) {
                    assertEquals(json, referenceString);
                }

                @Override
                public void onError() {
                    fail();
                }
            });
            saxParser.parse(file, handler);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
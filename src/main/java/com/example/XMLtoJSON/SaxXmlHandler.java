package com.example.XMLtoJSON;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaxXmlHandler extends DefaultHandler {
    private PrintWriter out;
    private StringBuilder json = new StringBuilder();
    private String currentElementName = "";

    SaxXmlHandler(PrintWriter _out){
        out = _out;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

        json.append("{ \n");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        json.append("\n}");
        out.println(json.toString());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        currentElementName = qName;
        json.append(qName);
        json.append(" : { \n value: ");
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        json.append("}");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        String stringForAnalyse = String.valueOf(ch, start, ch.length - start);
        Pattern pattern = Pattern.compile("([\\s\\S]*?)</" + currentElementName + ">");
        Matcher matcher = pattern.matcher(stringForAnalyse);
        if(matcher.find()){
            String str = matcher.group(1);
            String s = str;
        }

        json.append(ch, start, length);
    }
}

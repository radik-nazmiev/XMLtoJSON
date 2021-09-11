package com.example.XMLtoJSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaxXmlHandler extends DefaultHandler {
    private StringBuilder json = new StringBuilder();
    private ArrayList<XmlElement> elements = new ArrayList<>();
    private SaxHmlHandlerListener saxHmlHandlerListener;

    public void setSaxHmlHandlerListener(SaxHmlHandlerListener _saxHmlHandlerListener){
        saxHmlHandlerListener = _saxHmlHandlerListener;
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

        DecimalFormat format = new DecimalFormat("0.#");
        ArrayList<String> values = new ArrayList<>();
        elements.forEach(xmlElement -> {
            values.add(format.format(xmlElement.value).replace(",", "."));
        });
        String finalString = String.format(json.toString(), values.toArray());
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object obj = mapper.readValue(finalString, Object.class);
            finalString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            if(saxHmlHandlerListener != null){
                saxHmlHandlerListener.onError();
            }
        }

        if(saxHmlHandlerListener != null){
            saxHmlHandlerListener.onComplete(finalString);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        XmlElement element = new XmlElement();
        element.name = qName;
        elements.add(element);
        json.append(" \"").append(qName).append("\"");
        json.append(" : { \n  \"value\": \"%s\"");
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        elements.forEach(xmlElement -> {
            if(xmlElement.name.equals(qName) && !xmlElement.isClosed){
                xmlElement.isClosed = true;
            }
        });

        String str = json.substring(json.length() - 2);
        if(str.contains(",")){
            json.setLength(json.length() - 2);
        }

        json.append(" }\n");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        String stringForAnalyse = String.valueOf(ch, start, length);
        Pattern pattern = Pattern.compile("\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher(stringForAnalyse);
        if(matcher.find()){
            String str = matcher.group(0);
            double number = Double.parseDouble(str);
            elements.forEach(xmlElement -> {
                if(!xmlElement.isClosed){
                    xmlElement.value += number;
                }
            });
        }

        String naturalValue = String.valueOf(ch, start, length);
        if(naturalValue.startsWith("\n ")){
            json.append(",");
        }
        naturalValue = naturalValue.replaceAll("[^\\r\\n]+", "");
        json.append(naturalValue);
    }
}

package com.example.XMLtoJSON;

import java.io.*;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

@WebServlet(name = "rebuild", value = "/rebuild")
@MultipartConfig
public class Rebuild extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        Part filePart = request.getPart("file");
        InputStream fileContent = filePart.getInputStream();
        PrintWriter out = response.getWriter();

        SAXParserFactory factory = SAXParserFactory.newInstance();

        try{
            SAXParser saxParser = factory.newSAXParser();
            SaxXmlHandler handler = new SaxXmlHandler();
            handler.setSaxHmlHandlerListener(new SaxHmlHandlerListener() {
                @Override
                public void onComplete(String json) {
                    out.println(json);
                }

                @Override
                public void onError() {
                    out.println("Ошибка при обработке XML");
                }
            });
            saxParser.parse(fileContent, handler);
        }
        catch (Exception ex){
            ex.printStackTrace();
            out.println("Ошибка при обработке XML");
        }
    }
}
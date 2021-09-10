package com.example.XMLtoJSON;

import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "rebuild", value = "/rebuild")
@MultipartConfig
public class Rebuild extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        InputStream fileContent = filePart.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(fileContent);

        JSONObject json = XML.toJSONObject(inputStreamReader);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
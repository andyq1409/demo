package com.example.demo.controllers;

import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Value("${uzyt}")
    private String user;

    @GetMapping
    public String get() {
        return "Hello!";
    }

    @GetMapping("witaj")
    public String witaj() {
        return "Witaj: " + user;
    }

    @GetMapping("raport")
    public void doPost(HttpServletRequest request, HttpServletResponse response)  {
        JasperPrint jasperPrint;
        try {
            String reportFileName="report1.jrxml";
            String reportPath="reports/"+reportFileName;
            String targetFileName=reportFileName.replace(".jrxml", ".pdf");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            logger.info("Komunikat 1");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("RESOURCE_DIR","reports/");
            logger.info("Komunikat 2");

            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, JDBCConnection.getJDBCConnection());
            ServletOutputStream outputstream = response.getOutputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            response.setContentType("application/pdf");
            outputstream.write(byteArrayOutputStream.toByteArray());
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=" + targetFileName);
            outputstream.flush();
            outputstream.close();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

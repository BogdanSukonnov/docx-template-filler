package com.bogdansukonnov.util;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class MultipartDataParser {

    public MultipartData parseMultipartData(MultipartFormDataInput input, String fileParam) throws IOException {

        InputPart inputPart = null;

        try {
            inputPart = input.getFormDataMap().get(fileParam).get(0);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("template expected in %s parameter", fileParam));
        }

        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        byte[] bytes = IOUtils.toByteArray(inputStream);

        String fileName = getFileName(inputPart.getHeaders());

        return new MultipartData(bytes, fileName);

    }

    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                return name[1].trim().replaceAll("\"", "");
            }
        }

        throw new IllegalArgumentException("can't parse file name");
    }
}

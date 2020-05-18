package com.bogdansukonnov.util;

import com.bogdansukonnov.model.FillData;
import com.bogdansukonnov.model.RequestData;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class MultipartDataParser {

    public RequestData parseMultipartData(MultipartFormDataInput input, String fileParam, String fillDataParam) throws IOException {
        // parse file
        InputPart filePart = getInputPart(fileParam, "template", input);

        InputStream fileInputStream = filePart.getBody(InputStream.class, null);
        byte[] fileBytes = IOUtils.toByteArray(fileInputStream);

        String fileName = getFileName(filePart.getHeaders());

        // parse fill data
        InputPart fillDataPart = getInputPart(fillDataParam, "fillData", input);

        String fillDataString = fillDataPart.getBodyAsString();

        FillData fillData = getFillData(fillDataString);

        return new RequestData(fileBytes, fileName, fillData);

    }

    public FillData getFillData(String fillDataString) {
        FillData fillData = null;
        try {
            try (Jsonb jsonb = JsonbBuilder.create()) {
                fillData = jsonb.fromJson(fillDataString, FillData.class);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("wrong fillData format");
        }
        return fillData;
    }

    private InputPart getInputPart(String fileParam, String paramDescription, MultipartFormDataInput input) {
        InputPart filePart;
        try {
            filePart = input.getFormDataMap().get(fileParam).get(0);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("%s expected in %s parameter", paramDescription, fileParam));
        }
        return filePart;
    }

    private FillData getFillData() {
        return null;
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

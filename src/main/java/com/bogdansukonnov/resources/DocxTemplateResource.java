package com.bogdansukonnov.resources;

import com.bogdansukonnov.model.RequestData;
import com.bogdansukonnov.service.FileService;
import com.bogdansukonnov.util.MultipartDataParser;
import lombok.AllArgsConstructor;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/docx-template")
@AllArgsConstructor
public class DocxTemplateResource {

    private final FileService fileService;

    private final MultipartDataParser multipartDataParser;

    private static final String FILE_PARAM = "file";

    private static final String FILL_DATA_PARAM = "fillData";

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String saveTemplate(MultipartFormDataInput input) throws IOException {

        RequestData requestData = multipartDataParser.parseMultipartData(input, FILE_PARAM, FILL_DATA_PARAM);

        fileService.writeFile(requestData.getBytes(), requestData.getFileName());

        return String.format("%s saved", requestData.getFileName());
    }
}
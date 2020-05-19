package com.bogdansukonnov.resources;

import com.bogdansukonnov.model.RequestData;
import com.bogdansukonnov.model.ResponseFileData;
import com.bogdansukonnov.service.FileService;
import com.bogdansukonnov.util.MultipartDataParser;
import lombok.AllArgsConstructor;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;

@Path("/fill")
@AllArgsConstructor
public class DocxFillTemplateResource {

    private final FileService fileService;

    private final MultipartDataParser multipartDataParser;

    private static final String FILE_PARAM = "template";

    private static final String FILL_DATA_PARAM = "fillData";

    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response fillTemplate(MultipartFormDataInput input) throws IOException {

        RequestData requestData = multipartDataParser.parseMultipartData(input, FILE_PARAM, FILL_DATA_PARAM);

        ResponseFileData responseFileData = fileService.fillTemplate(requestData);

        StreamingOutput stream = fileService.getStreamingOutput(responseFileData.getFile());
        Response.ResponseBuilder response = Response.ok(stream);
        response.header("Content-Disposition", "attachment; filename=" + responseFileData.getFileName());

        return response.build();
    }
}
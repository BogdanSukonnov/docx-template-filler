package com.bogdansukonnov.resources;

import com.bogdansukonnov.model.FillData;
import com.bogdansukonnov.model.ResponseFileData;
import com.bogdansukonnov.service.FileService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.IOException;

@Path("/docx-fill")
public class DocxFillResource {

    @Inject
    FileService fileService;

    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fillTemplate(FillData data) throws IOException {
        ResponseFileData responseFileData = fileService.fillTemplate(data, new File(""));
        StreamingOutput stream = fileService.getStreamingOutput(responseFileData.getFile());
        Response.ResponseBuilder response = Response.ok(stream);
        response.header("Content-Disposition", "attachment; filename=" + responseFileData.getFileName());
        return response.build();
    }
}
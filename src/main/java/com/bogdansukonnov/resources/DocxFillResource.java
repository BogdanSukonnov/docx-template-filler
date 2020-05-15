package com.bogdansukonnov.resources;

import com.bogdansukonnov.model.FillData;
import com.bogdansukonnov.model.ResponseFileData;
import com.bogdansukonnov.service.FileService;
import lombok.AllArgsConstructor;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/docx-fill")
@AllArgsConstructor
public class DocxFillResource {

    private final FileService fileService;

    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fillTemplate(FillData data) throws IOException {
        ResponseFileData responseFileData = fileService.fillTemplate(data);
        Response.ResponseBuilder response = Response.ok((Object) responseFileData.getFile());
        response.header("Content-Disposition", "attachment; filename=" + responseFileData.getFileName());
        return response.build();
    }
}
package com.bogdansukonnov.resources;

import com.bogdansukonnov.service.FileService;
import com.bogdansukonnov.util.MultipartDataParser;
import lombok.AllArgsConstructor;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/docx-fill")
@AllArgsConstructor
public class DocxFillResource {

    private final FileService fileService;

    private final MultipartDataParser multipartDataParser;

    private static final String FILE_PARAM = "file";

    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_JSON)
    public String filltemplate() throws IOException {


        return "";
    }
}
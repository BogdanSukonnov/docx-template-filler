package com.bogdansukonnov.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@ApplicationScoped
public class FileService {

    @ConfigProperty(name = "templates.path")
    String templatesPath;

    public void writeFile(byte[] content, String filename) throws IOException {

        File templatesDirectory = new File(templatesPath);
        if (!templatesDirectory.exists()) {
            throw new RuntimeException("Can't find templates directory " + templatesPath);
        }

        File file = new File(templatesDirectory.getAbsolutePath() + "/" + filename);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();
    }
}

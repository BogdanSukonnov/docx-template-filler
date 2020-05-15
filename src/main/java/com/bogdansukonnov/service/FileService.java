package com.bogdansukonnov.service;

import com.bogdansukonnov.model.FillData;
import com.bogdansukonnov.model.ResponseFileData;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@ApplicationScoped
public class FileService {

    @ConfigProperty(name = "templates.path")
    String templatesPath;

    @Inject
    ZipService zipService;

    public void writeFile(byte[] content, String filename) throws IOException {

        File file = getTemplateFile(filename);

        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Can't create file: " + filename);
            }
        }

        try (FileOutputStream fop = new FileOutputStream(file)) {
            fop.write(content);
            fop.flush();
        }
    }

    public ResponseFileData fillTemplate(FillData data) throws IOException {

        File template = getTemplateFile(data.getTemplate());

        if (!template.exists()) {
            throw new IllegalArgumentException("Don't have template: " + data.getTemplate());
        }

        File copiedTemplate = copyToTemp(template);

        Map<String, String> zipMap = zipService.unzip(copiedTemplate);

        Files.delete(copiedTemplate.toPath());

        File filledFile = zipService.zipFile(zipMap);

        String fileName = "filled.template.docx";

        return new ResponseFileData(filledFile, fileName);
    }

    private File copyToTemp(File template) throws IOException {
        File tempFile = File.createTempFile("toFill", ".docx");
        Files.copy(template.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return tempFile;
    }

    private File getTemplateFile(String filename) {

        File templatesDirectory = new File(templatesPath);

        if (!templatesDirectory.exists()) {
            throw new IllegalArgumentException("Can't find templates directory: " + templatesPath);
        }

        return new File(templatesDirectory.getAbsolutePath() + "/" + filename);
    }
}

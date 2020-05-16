package com.bogdansukonnov.service;

import com.bogdansukonnov.model.FillData;
import com.bogdansukonnov.model.ResponseFileData;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@ApplicationScoped
public class FileService {

    public static final int BUFFER_SIZE = 1024;

    @ConfigProperty(name = "templates.path")
    String templatesPath;

    @Inject
    ZipService zipService;

    public StreamingOutput getStreamingOutput(File file) {
        return outputStream -> {
            try (InputStream fis = new FileInputStream(file)) {
                byte[] bytes = new byte[BUFFER_SIZE];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    outputStream.write(bytes, 0, length);
                }
            } finally {
                // clean up temp file
                Files.delete(file.toPath());
            }
        };
    }

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

        replaceVariables(data, zipMap);

        File filledFile = zipService.zipFile(zipMap);

        String fileName = "filled.template.docx";

        return new ResponseFileData(filledFile, fileName);
    }

    private void replaceVariables(FillData data, Map<String, String> zipMap) throws IOException {
        String multiplier = "[^\\" + data.getClosingTag() + "]*?";

        for (Map.Entry<String, String> fileEntry : zipMap.entrySet()) {
            String filePath = fileEntry.getKey();
            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            for (FillData.Variable variable : data.getVariables()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\\").append(data.getOpeningTag()).append(multiplier);
                for (char ch : variable.getName().toCharArray()) {
                    stringBuilder.append(ch).append(multiplier);
                }
                stringBuilder.append("\\").append(data.getClosingTag());
                content = content.replaceAll(stringBuilder.toString(), variable.getValue());
            }
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(content);
            }
        }
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

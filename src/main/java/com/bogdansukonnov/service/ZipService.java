package com.bogdansukonnov.service;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@ApplicationScoped
public class ZipService {

    public static final int BUFFER_SIZE = 1024;

    public File zipFile(Map<String, String> zipMap) throws IOException {
        File zippedFile = Files.createTempFile("filled", null).toFile();
        try (FileOutputStream fos = new FileOutputStream(zippedFile.getCanonicalPath());
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            for (Map.Entry<String, String> zipMapEntry : zipMap.entrySet()) {
                try (FileInputStream fis = new FileInputStream(zipMapEntry.getKey())) {
                    zipOut.putNextEntry(new ZipEntry(zipMapEntry.getValue()));
                    byte[] bytes = new byte[BUFFER_SIZE];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                }
                Files.delete(new File(zipMapEntry.getKey()).toPath());
            }
        }
        return zippedFile;
    }

    public Map<String, String> unzip(File zipFile) throws IOException {
        Map<String, String> zipMap = new HashMap<>();
        byte[] buffer = new byte[BUFFER_SIZE];
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String tempFile = Files.createTempFile(null, null).toFile().getCanonicalPath();
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                zipMap.put(tempFile, zipEntry.getName());
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        return zipMap;
    }
}

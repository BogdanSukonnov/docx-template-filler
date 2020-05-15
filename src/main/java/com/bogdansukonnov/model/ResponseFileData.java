package com.bogdansukonnov.model;

import lombok.Data;
import lombok.NonNull;

import java.io.File;

@Data
public class ResponseFileData {

    @NonNull
    private final File file;

    @NonNull
    private final String fileName;
}

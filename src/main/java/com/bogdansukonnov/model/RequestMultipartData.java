package com.bogdansukonnov.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class RequestMultipartData {

    @NonNull
    private final byte[] bytes;

    @NonNull
    private final String fileName;
}

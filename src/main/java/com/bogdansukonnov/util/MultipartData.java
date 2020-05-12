package com.bogdansukonnov.util;

import lombok.Data;
import lombok.NonNull;

@Data
public class MultipartData {

    @NonNull
    private final byte[] bytes;

    @NonNull
    private final String fileName;
}

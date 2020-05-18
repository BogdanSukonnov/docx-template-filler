package com.bogdansukonnov.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class RequestData {

    @NonNull
    private final byte[] bytes;

    @NonNull
    private final String fileName;

    @NonNull
    private final FillData fillData;
}

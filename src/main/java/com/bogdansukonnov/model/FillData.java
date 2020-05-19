package com.bogdansukonnov.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Data
public class FillData {

    @NotNull
    private List<Variable> variables;

    @NotNull
    private String openingTag;

    @NotNull
    private String closingTag;

    @Null
    private String resultFilename;

    @Data
    public static class Variable {

        private String name;

        private String value;
    }
}

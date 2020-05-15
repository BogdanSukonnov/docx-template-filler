package com.bogdansukonnov.model;

import lombok.Data;

import java.util.List;

@Data
public class FillData {

    private String template;

    private List<Variable> variables;

    @Data
    public static class Variable {

        private String name;

        private String value;
    }
}

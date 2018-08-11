package com.hribol.bromium.core.config;

import java.util.function.Function;

public class ContextProvider {
    private Function<ParameterValues, SearchContextFunction> function;

    public Function<ParameterValues, SearchContextFunction> getFunction() {
        return function;
    }

    public void setFunction(Function<ParameterValues, SearchContextFunction> function) {
        this.function = function;
    }
}
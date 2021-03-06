package com.hribol.bromium.core.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JsFunctionInvocation {
    private String name;
    private List<Supplier<String>> parameters;
    private String raw;
    private boolean trailingSemicolon;

    public JsFunctionInvocation(String name) {
        this.name = name;
        this.parameters = new ArrayList<>();
        this.trailingSemicolon = true;
    }

    public JsFunctionInvocation raw(String raw) {
        this.raw = raw;
        return this;
    }

    public JsFunctionInvocation string(String rawString) {
        parameters.add(() -> "'" + rawString + "'");
        return this;
    }

    public JsFunctionInvocation variable(String variable) {
        parameters.add(() -> variable);
        return this;
    }

    public JsFunctionInvocation literal(String variable) {
        parameters.add(() -> variable);
        return this;
    }

    public JsFunctionInvocation invocation(JsFunctionInvocation jsFunctionInvocation) {
        parameters.add(jsFunctionInvocation::getInvocation);
        return this;
    }

    public JsFunctionInvocation callback(JsFunctionCallback jsFunctionCallback) {
        parameters.add(jsFunctionCallback::getDefinition);
        return this;
    }

    public JsFunctionInvocation trailingSemicolon(boolean trailingSemicolon) {
        this.trailingSemicolon = trailingSemicolon;
        return this;
    }

    public boolean isLeaf() {
        return raw != null;
    }

    public String getInvocation() {
        return Optional.ofNullable(raw).orElse(name + "(" + parameters
                .stream()
                .map(Supplier::get)
                .collect(Collectors.joining(", ")) + ")" + (trailingSemicolon ? ";\n" : ""));
    }
}
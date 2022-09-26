package com.oop.project.ioc.initialization;

import java.util.HashSet;
import java.util.Set;

public class InitializationRulesBuilder {
    private final Set<InitializationRule> initializationRules = new HashSet<>();

    public InitializationRulesBuilder() {
    }

    public InitializationRulesBuilder withInitRule(InitializationRule rule) {
        initializationRules.add(rule);
        return this;
    }

    public Set<InitializationRule> build() {
        return initializationRules;
    }
}

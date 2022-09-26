package com.oop.project.ioc.initialization;

import com.oop.project.ioc.annotations.Bean;

import java.util.Set;
import java.util.stream.Collectors;

public class SkipPrototypeBeanInitializationRule implements InitializationRule {
    @Override
    public <T> T nextElement() {
        return null;
    }

    @Override
    public Set<Class<?>> applyRule(Set<Class<?>> classes) {
        return classes.stream()
                .filter(this::isNotPrototype)
                .collect(Collectors.toSet());
    }

    private boolean isNotPrototype(Class<?> aClass) {
        return !aClass.getAnnotation(Bean.class).prototype();
    }
}

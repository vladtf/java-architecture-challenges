package com.oop.project.ioc.initialization;

import java.util.Set;
import java.util.stream.Collectors;

public class SkipAnnotationBeanInitializationRule implements InitializationRule {
    @Override
    public <T> T nextElement() {
        return null;
    }

    @Override
    public Set<Class<?>> applyRule(Set<Class<?>> classes) {
        return classes.stream()
                .filter(this::isNotAnnotation)
                .collect(Collectors.toSet());
    }

    private boolean isNotAnnotation(Class<?> aClass) {
        return !aClass.isAnnotation();
    }
}

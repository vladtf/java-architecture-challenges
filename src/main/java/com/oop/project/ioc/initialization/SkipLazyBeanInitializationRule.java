package com.oop.project.ioc.initialization;

import com.oop.project.ioc.annotations.Bean;

import java.util.Set;
import java.util.stream.Collectors;

public class SkipLazyBeanInitializationRule implements InitializationRule {
    @Override
    public <T> T nextElement() {
        return null;
    }

    @Override
    public Set<Class<?>> applyRule(Set<Class<?>> classes) {
        return classes.stream()
                .filter(this::isNotLazy)
                .collect(Collectors.toSet());
    }

    private boolean isNotLazy(Class<?> aClass) {
        return !aClass.getAnnotation(Bean.class).lazy();
    }
}

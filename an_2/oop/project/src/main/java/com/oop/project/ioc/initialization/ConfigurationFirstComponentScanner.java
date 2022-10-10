package com.oop.project.ioc.initialization;

import java.util.Set;

public class ConfigurationFirstComponentScanner implements ComponentsScanner {
    private final ComponentsScanner scanner;

    public ConfigurationFirstComponentScanner(ComponentsScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public <T> T nextElement() {
        return null;
    }

    @Override
    public Set<Class<?>> scanPrefixes(String... prefix) {
        Set<Class<?>> classes = scanner.scanPrefixes(prefix);
        // todo sort classes for @Configuration to be first
        return classes;
    }
}

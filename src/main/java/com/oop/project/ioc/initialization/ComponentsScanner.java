package com.oop.project.ioc.initialization;

import java.util.Set;

public interface ComponentsScanner extends Chained {
    Set<Class<?>> scanPrefixes(String... prefix);
}

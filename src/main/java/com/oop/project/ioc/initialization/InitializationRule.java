package com.oop.project.ioc.initialization;

import java.util.Set;

public interface InitializationRule extends Chained {
    Set<Object> applyRule(Set<Class<?>> classes);
}

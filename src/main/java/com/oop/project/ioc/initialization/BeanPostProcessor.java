package com.oop.project.ioc.initialization;

// Create a bean post processor to resolve @InjectedValue fields or methods
public interface BeanPostProcessor {
    <T> T processBeanAfterInitialization(T obj);
}

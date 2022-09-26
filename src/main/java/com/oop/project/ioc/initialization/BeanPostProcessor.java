package com.oop.project.ioc.initialization;

public interface BeanPostProcessor {
    <T> T processBeanAfterInitialization(T obj);
}

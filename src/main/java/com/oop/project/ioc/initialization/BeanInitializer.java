package com.oop.project.ioc.initialization;

import java.util.Optional;
import java.util.Set;

public interface BeanInitializer {
    <T> Optional<T> getBean(Class<T> clazz, Object... args);

     void applyBeanPostProcessors(Set<Object> beans, Set<BeanPostProcessor> beanPostProcessors);
}

package com.oop.project.ioc.initialization;

import java.util.Optional;
import java.util.Set;

public interface BeanInitializer {
    <T> Optional<T> getBean(Class<T> clazz, Object... args);

    // TODO this method seems to be more correct (need to move bean post proc logic out of the initializer)
    void applyBeanPostProcessors(Set<Object> beans, Set<BeanPostProcessor> beanPostProcessors);

    // TODO neet to remove this one
    void applyBeanPostProcessors(Set<Object> beans);
}

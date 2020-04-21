package org.zhenchao.kratos.inject;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Bean property descriptor.
 *
 * @author zhenchao.wang 2017-09-05 17:39:50
 * @version 1.0.0
 */
public interface BeanProperty {

    /**
     * Get the property name.
     *
     * @return
     */
    String getName();

    /**
     * Get the property type.
     *
     * @return
     */
    Class<?> getType();

    /**
     * Get the bean type.
     *
     * @return
     */
    Class<?> getBeanType();

    /**
     * Check if the property is primitive type.
     *
     * @return
     */
    boolean isPrimitive();

    /**
     * Check if the property is array type.
     *
     * @return
     */
    boolean isArray();

    /**
     * Check if the property use raw inject type.
     *
     * @return
     */
    boolean isRawType();

    /**
     * Get all {@link Annotation}s of the bean property,
     * include the {@link Annotation}s which is annotated on getter or setter.
     *
     * @return
     */
    Collection<Annotation> getPropAnnotations();

    /**
     * Get all {@link Annotation}s of the bean, include the super class.
     *
     * @return
     */
    Collection<Annotation> getBeanAnnotations();
}

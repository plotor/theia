package org.zhenchao.kratos.inject;

import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.error.ConfigException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author zhenchao.wang 2020-01-13 17:37
 * @version 1.0.0
 */
public class MethodPropertyInjector extends AbstractPropertyInjector implements PropertyInjector {

    public MethodPropertyInjector(Object bean, PropertyDescriptor descriptor, Field field, Attribute attr) {
        super(bean, descriptor, field, attr);
    }

    @Override
    public void doInjectValue(Object fieldValue) throws ConfigException {
        try {
            descriptor.getWriteMethod().setAccessible(true);
            descriptor.getWriteMethod().invoke(bean, fieldValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ConfigException("set field value error, " +
                    "options: " + beanProperty.getBeanType() +
                    ", field: " + beanProperty.getName() + "(" + beanProperty.getType() + ")", e);
        }
    }

    @Override
    public Class<?> getFieldType() {
        return descriptor.getPropertyType();
    }

}
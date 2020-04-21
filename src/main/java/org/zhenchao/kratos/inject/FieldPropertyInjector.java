package org.zhenchao.kratos.inject;

import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.error.ConfigException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author zhenchao.wang 2020-01-13 17:37
 * @version 1.0.0
 */
public class FieldPropertyInjector extends AbstractPropertyInjector implements PropertyInjector {

    public FieldPropertyInjector(Object bean, PropertyDescriptor descriptor, Field field, Attribute attr) {
        super(bean, descriptor, field, attr);
    }

    @Override
    public void doInjectValue(Object fieldValue) throws ConfigException {
        field.setAccessible(true);
        if (Modifier.isStatic(field.getModifiers())) {
            throw new ConfigException("static field is unsupported, " +
                    "options: " + beanProperty.getBeanType() +
                    ", field: " + beanProperty.getName() + "(" + beanProperty.getType() + ")");
        }
        try {
            field.set(bean, fieldValue);
        } catch (IllegalAccessException e) {
            throw new ConfigException("set field value error, " +
                    "options: " + beanProperty.getBeanType() +
                    ", field: " + beanProperty.getName() + "(" + beanProperty.getType() + ")", e);
        }
    }

    @Override
    public Class<?> getFieldType() {
        return field.getType();
    }
}
package org.zhenchao.kratos.inject;

import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Options;
import org.zhenchao.kratos.error.ConfigException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link PropertyInjector} detector.
 *
 * @author zhenchao.wang 2017-09-05 18:12:37
 * @version 1.0.0
 */
public class PropertyInjectorDetector {

    /**
     * Resolved {@link PropertyInjector} collection for the options bean.
     * Priority use the setter method, than use filed injection directly.
     *
     * @param options options bean
     * @return {@link PropertyInjector} collection.
     * @throws ConfigException
     */
    public static Collection<PropertyInjector> resolveInjectors(final Options options) throws ConfigException {
        List<PropertyInjector> injectors = new ArrayList<>();
        Map<String, PropertyDescriptor> descriptors = new HashMap<>();
        Class<?> beanClass = options.getClass();


        /* 1. Iterate through all getter and setter */

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            // Iterate through bean's property descriptors
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();

                if (getter != null) {
                    descriptors.put(descriptor.getDisplayName(), descriptor);
                }

                // exist setter
                if (setter != null) {
                    final Field field = getField(beanClass, descriptor);

                    // annotated on setter
                    if (setter.isAnnotationPresent(Attribute.class)) {
                        injectors.add(new MethodPropertyInjector(options, descriptor, field, setter.getAnnotation(Attribute.class)));
                        descriptors.remove(descriptor.getDisplayName());
                    }

                    // annotated on getter
                    if (getter != null && getter.isAnnotationPresent(Attribute.class)) {
                        injectors.add(new MethodPropertyInjector(options, descriptor, field, getter.getAnnotation(Attribute.class)));
                        descriptors.remove(descriptor.getDisplayName());
                    }
                }
            }
        } catch (Throwable t) {
            throw new ConfigException("can't introspect bean class: " + beanClass, t);
        }

        /* 2. Iterate through all fields */

        do {
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Attribute.class)) {
                    if (descriptors.containsKey(field.getName())
                            && descriptors.get(field.getName()).getWriteMethod() != null) {
                        PropertyDescriptor descriptor = descriptors.get(field.getName());
                        injectors.add(new MethodPropertyInjector(options, descriptor, field, field.getAnnotation(Attribute.class)));
                    } else {
                        injectors.add(new FieldPropertyInjector(options, null, field, field.getAnnotation(Attribute.class)));
                    }
                } else if (descriptors.containsKey(field.getName())) {
                    // annotated on getter
                    PropertyDescriptor descriptor = descriptors.get(field.getName());
                    if (descriptor.getReadMethod().isAnnotationPresent(Attribute.class)) {
                        injectors.add(new FieldPropertyInjector(
                                options, descriptor, field, descriptor.getReadMethod().getAnnotation(Attribute.class)));
                    }
                }
            }
        } while ((beanClass = beanClass.getSuperclass()) != null);

        if (injectors.stream().anyMatch(injector -> injector.getBeanProperty().isRawType()) && injectors.size() > 1) {
            throw new ConfigException("raw type is mutually exclusive with general type: " + options.getClass());
        }

        return injectors;
    }

    private static Field getField(Class<?> beanClass, PropertyDescriptor descriptor) {
        do {
            try {
                return beanClass.getDeclaredField(descriptor.getName());
            } catch (NoSuchFieldException exc) {
                // ignore, move on
            }
        } while ((beanClass = beanClass.getSuperclass()) != null);
        return null;
    }

}

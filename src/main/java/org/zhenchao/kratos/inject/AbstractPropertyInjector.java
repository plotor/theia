package org.zhenchao.kratos.inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.constant.Constants;
import org.zhenchao.kratos.error.ConfigException;
import org.zhenchao.kratos.inject.converter.Converter;
import org.zhenchao.kratos.inject.converter.ConverterRegistry;
import org.zhenchao.kratos.inject.converter.VoidConverter;
import org.zhenchao.kratos.util.ConfUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author zhenchao.wang 2020-01-13 17:27
 * @version 1.0.0
 */
public abstract class AbstractPropertyInjector implements PropertyInjector {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private ConverterRegistry converterRegistry = ConverterRegistry.getInstance();

    protected final Object bean;
    protected final PropertyDescriptor descriptor;
    protected final Field field;
    protected final BeanProperty beanProperty;
    protected final Attribute attr;

    public AbstractPropertyInjector(final Object bean, final PropertyDescriptor descriptor, final Field field, final Attribute attr) {
        this.bean = bean;
        this.descriptor = descriptor;
        this.field = field;
        this.attr = attr;

        Class<?> propertyType = field != null ? field.getType() : descriptor.getPropertyType();
        String propertyName = field != null ? field.getName() : descriptor.getName();

        this.beanProperty = new DefaultBeanProperty(
                bean.getClass(),
                propertyType,
                propertyName,
                this.getBeanAnnotations(bean.getClass()),
                this.getPropAnnotations(field, descriptor),
                attr.raw());
    }

    @Override
    public BeanProperty getBeanProperty() {
        return this.beanProperty;
    }

    @Override
    public String getPropertyName() {
        String propName = attr.name();
        if (StringUtils.isEmpty(propName)) {
            propName = attr.value();
        }
        if (StringUtils.isEmpty(propName)) {
            // use the bean property name if not set
            propName = beanProperty.getName();
        }
        return propName;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public void injectValue(final Properties properties) throws ConfigException {
        final Class<?> beanType = beanProperty.getBeanType();
        final String propName = beanProperty.getName();
        final Class<?> propType = beanProperty.getType();

        try {
            final String propertyKey = this.getPropertyName();
            final String defaultValue = attr.defaultValue();

            Object fieldValue;
            if (attr.raw()) {
                log.info("Inject filed[{}: {}] value with raw type, bean[{}]", propName, propType.getSimpleName(), beanType);
                if (String.class.equals(propType)) {
                    if (properties.containsKey(Constants.RAW_CONFIG_KEY)) {
                        fieldValue = properties.getProperty(Constants.RAW_CONFIG_KEY);
                    } else {
                        fieldValue = ConfUtils.toString(properties);
                    }
                } else {
                    fieldValue = properties;
                }
            } else {
                final Class<?> fieldType = this.getFieldType();
                final Class<? extends Converter> customConverter = attr.converter();

                // use built-in converter
                Converter converter;
                if (VoidConverter.class.equals(customConverter)) {
                    converter = ConverterRegistry.getInstance().getBuiltInConverter(fieldType);
                    log.info("Inject filed[{}: {}] value by built-in converter[{}], bean[{}]",
                            propName, propType.getSimpleName(), converter.getClass().getSimpleName(), beanType);
                }
                // use custom converter
                else {
                    converter = converterRegistry.getCustomConverter(customConverter);
                    if (fieldType != converter.supportedClass()) {
                        throw new ConfigException("invalid custom converter: " + customConverter
                                + ", supportedClass: " + converter.supportedClass() + ", fieldType: " + fieldType);
                    }
                    log.info("Inject filed[{}: {}] value by custom converter[{}], bean[{}]",
                            propName, propType.getSimpleName(), customConverter, beanType);
                }

                if (attr.required() && !properties.containsKey(propertyKey) && StringUtils.isEmpty(defaultValue)) {
                    log.error("Missing required field config: [\n{}\n].", ConfUtils.toString(properties));
                    throw new ConfigException("missing required field config, " +
                            "bean: " + beanType + ", field: " + propName + "(" + propType + ")");
                }
                final String value = properties.getProperty(propertyKey, defaultValue);
                log.debug("Convert value[{}] by converter[{}].", value, converter.getClass());
                fieldValue = converter.convert(value, beanProperty);

                // inject by the default primitive value
                if (fieldValue == null) {
                    if (!beanProperty.isArray() && beanProperty.isPrimitive()) {
                        fieldValue = ConfUtils.resolveDefaultValue(beanProperty.getType());
                    }
                }
            }

            log.debug("Inject bean[{}]'s property[{}] by value[{}].", bean.getClass().getName(), propertyKey, fieldValue);
            this.doInjectValue(fieldValue);
        } catch (Throwable t) {
            throw new ConfigException("can't inject bean's property, "
                    + "options: " + beanProperty.getBeanType()
                    + ", property: " + beanProperty.getName() + "(" + beanProperty.getType() + ")",
                    t);
        }
    }

    /**
     * Get field type.
     *
     * @return
     */
    public abstract Class<?> getFieldType();

    /**
     * Inject the filter value by {@link PropertyInjector}.
     *
     * @param fieldValue
     * @throws ConfigException
     * @throws ConfigException
     */
    protected abstract void doInjectValue(final Object fieldValue) throws ConfigException;

    private Collection<Annotation> getBeanAnnotations(Class<?> beanClass) {
        return Arrays.asList(beanClass.getAnnotations());
    }

    private Collection<Annotation> getPropAnnotations(Field field, PropertyDescriptor descriptor) {
        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();

        if (descriptor != null) {
            if (descriptor.getWriteMethod() != null) {
                this.addAnnotationsToMap(annotations, descriptor.getWriteMethod().getAnnotations());
            }
            if (descriptor.getReadMethod() != null) {
                this.addAnnotationsToMap(annotations, descriptor.getReadMethod().getAnnotations());
            }
        }
        if (field != null) {
            this.addAnnotationsToMap(annotations, field.getAnnotations());
        }
        return annotations.values();
    }

    private void addAnnotationsToMap(Map<Class<? extends Annotation>, Annotation> annotationMap, Annotation[] annotations) {
        if (ArrayUtils.isNotEmpty(annotations)) {
            for (Annotation annotation : annotations) {
                if (!annotationMap.containsKey(annotation.annotationType())) {
                    annotationMap.put(annotation.annotationType(), annotation);
                }
            }
        }
    }

    protected static class DefaultBeanProperty implements BeanProperty {

        private final Class<?> beanType;
        private final Class<?> fieldType;
        private final String fieldName;
        private final Collection<Annotation> beanAnnotations;
        private final Collection<Annotation> propAnnotations;
        private final boolean rawType;

        public DefaultBeanProperty(Class<?> beanType,
                                   Class<?> fieldType,
                                   String fieldName,
                                   Collection<Annotation> beanAnnotations,
                                   Collection<Annotation> propAnnotations,
                                   boolean rawType) {
            this.beanType = beanType;
            this.fieldType = fieldType;
            this.fieldName = fieldName;
            this.propAnnotations = Collections.unmodifiableCollection(propAnnotations);
            this.beanAnnotations = Collections.unmodifiableCollection(beanAnnotations);
            this.rawType = rawType;
        }

        @Override
        public String getName() {
            return fieldName;
        }

        @Override
        public Class<?> getType() {
            return this.isArray() ? fieldType.getComponentType() : fieldType;
        }

        @Override
        public Class<?> getBeanType() {
            return beanType;
        }

        @Override
        public boolean isPrimitive() {
            return fieldType.isPrimitive();
        }

        @Override
        public boolean isArray() {
            return fieldType.isArray();
        }

        @Override
        public boolean isRawType() {
            return rawType;
        }

        @Override
        public Collection<Annotation> getPropAnnotations() {
            return propAnnotations;
        }

        @Override
        public Collection<Annotation> getBeanAnnotations() {
            return beanAnnotations;
        }
    }

}
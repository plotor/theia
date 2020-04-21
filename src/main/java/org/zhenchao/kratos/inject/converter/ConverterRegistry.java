package org.zhenchao.kratos.inject.converter;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhenchao.wang 2016-09-06 09:44
 * @version 1.0.0
 */
public class ConverterRegistry {

    private static final ConverterRegistry INSTANCE = new ConverterRegistry();

    private Map<Class<?>, Converter<?>> builtInRegistry = new HashMap<>();
    private Map<Class<? extends Converter>, Converter<?>> customRegistry = new HashMap<>();

    private ConverterRegistry() {
        this.init();
    }

    public static ConverterRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Register built-in converter.
     *
     * @param converter
     * @return
     */
    public ConverterRegistry register(final Converter<?> converter) {
        return this.register(converter, true);
    }

    /**
     * Register built-in or custom converter.
     *
     * @param converter
     * @param builtIn
     * @return
     */
    public ConverterRegistry register(final Converter<?> converter, boolean builtIn) {
        Validate.notNull(converter, "null converter");
        if (builtIn) {
            Class<?> supportedClass = converter.supportedClass();
            if (supportedClass != null) {
                if (builtInRegistry.containsKey(supportedClass)) {
                    throw new IllegalStateException("duplicated converter found for class: " + supportedClass);
                }
                this.builtInRegistry.put(supportedClass, converter);
            } else {
                throw new IllegalStateException("null supported class, converter: " + converter.getClass());
            }
        } else {
            customRegistry.putIfAbsent(converter.getClass(), converter);
        }
        return this;
    }

    /**
     * Get built-in converter by field class.
     *
     * @param supportedClass filed type.
     * @return {@link GenericConverter} will be used if no suitable converter found.
     */
    @SuppressWarnings("unchecked")
    public <T> Converter<T> getBuiltInConverter(Class<T> supportedClass) {
        Validate.notNull(supportedClass, "null supported class");

        if (supportedClass.isArray()) {
            return ArrayConverter.newConverter(supportedClass);
        }

        Class<?> normalizedType = this.normalized(supportedClass);
        if (this.builtInRegistry.containsKey(normalizedType)) {
            return (Converter<T>) this.builtInRegistry.get(normalizedType);
        }

        return (Converter<T>) builtInRegistry.get(Object.class);
    }

    /**
     * Get custom converter by converter class.
     *
     * @param converterClass
     * @return
     * @throws IllegalStateException the custom converter class must defined a public default constructor.
     */
    public Converter<?> getCustomConverter(Class<? extends Converter> converterClass) {
        Validate.notNull(converterClass, "null converter class");

        Converter<?> converter = customRegistry.get(converterClass);
        if (null == converter) {
            try {
                converter = converterClass.newInstance();
                this.register(converter, false);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new IllegalStateException("instantiate custom converter error: " + converterClass, e);
            }
        }
        return converter;
    }

    public void reset() {
        this.init();
    }

    private void init() {
        this.builtInRegistry = new HashMap<>();
        this.customRegistry = new HashMap<>();
        this.register(new StringConverter())
                .register(new BooleanConverter())
                .register(new CharacterConverter())
                .register(NumberConverter.newConverter(Byte.class))
                .register(NumberConverter.newConverter(Short.class))
                .register(NumberConverter.newConverter(Integer.class))
                .register(NumberConverter.newConverter(Long.class))
                .register(NumberConverter.newConverter(Float.class))
                .register(NumberConverter.newConverter(Double.class))
                .register(new DateConverter())
                .register(new CalendarConverter())
                .register(new GenericConverter());
    }

    private Class<?> normalized(Class<?> type) {
        return type.isPrimitive() ? this.toBoxingType(type) : type;
    }

    private Class<?> toBoxingType(Class<?> primitiveType) {
        if (boolean.class == primitiveType) {
            return Boolean.class;
        }
        if (char.class == primitiveType) {
            return Character.class;
        }
        if (byte.class == primitiveType) {
            return Byte.class;
        }
        if (short.class == primitiveType) {
            return Short.class;
        }
        if (int.class == primitiveType) {
            return Integer.class;
        }
        if (long.class == primitiveType) {
            return Long.class;
        }
        if (float.class == primitiveType) {
            return Float.class;
        }
        return Double.class;
    }

}

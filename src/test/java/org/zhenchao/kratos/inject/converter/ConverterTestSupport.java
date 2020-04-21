package org.zhenchao.kratos.inject.converter;

import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.DatePattern;
import org.zhenchao.kratos.NumberRadix;
import org.zhenchao.kratos.NumberRadix.NumberFormat;
import org.zhenchao.kratos.inject.BeanProperty;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author zhenchao.wang 2017-09-06 11:20:32
 * @version 1.0.0
 */
public abstract class ConverterTestSupport {

    protected ConverterRegistry registry = ConverterRegistry.getInstance();

    protected BeanProperty beanProperty;

    @Before
    public void init() throws Exception {
        beanProperty = mock(BeanProperty.class);
        this.setBeanAnnotations(mock(Configurable.class));
        this.setPropAnnotations(mock(Attribute.class));
        this.setupBeanProperty();
    }

    void setupBeanProperty() {
        // override
    }

    void setBeanAnnotations(Annotation... annotations) {
        when(beanProperty.getBeanAnnotations()).thenReturn(Arrays.asList(annotations));
    }

    void setPropAnnotations(Annotation... annotations) {
        when(beanProperty.getPropAnnotations()).thenReturn(Arrays.asList(annotations));
    }

    <T extends Annotation> T mockAnnotation(Class<T> annotationType) {
        return mock(annotationType);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    void setPropertyType(Class type) {
        when(beanProperty.getType()).thenReturn(type);
    }

    NumberRadix mockNumberRadixAnnotation(NumberFormat numberFormat) {
        NumberRadix numberRadix = this.mockAnnotation(NumberRadix.class);

        when(numberRadix.value()).thenReturn(numberFormat != null ? numberFormat : NumberFormat.DECIMAL);

        return numberRadix;
    }

    DatePattern mockDateFormat(String format) {
        DatePattern datePattern = this.mockAnnotation(DatePattern.class);

        when(datePattern.value()).thenReturn(format);

        return datePattern;
    }

}

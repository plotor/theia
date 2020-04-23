package org.zhenchao.kratos.inject.zk;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zhenchao.kratos.AbstractOptions;
import org.zhenchao.kratos.Attribute;
import org.zhenchao.kratos.Configurable;
import org.zhenchao.kratos.error.ConvertException;
import org.zhenchao.kratos.inject.BeanProperty;
import org.zhenchao.kratos.inject.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configurable("ZK:/kratos/example")
public class DefaultOptions extends AbstractOptions {

    private static final long serialVersionUID = 3950396769022356384L;

    @Attribute("access_url")
    private String accessUrl;

    @Attribute(name = "nums", converter = ListConverter.class)
    public List<Integer> nums;

    private Set<Integer> set = new HashSet<>();

    @Override
    public void update() {
        set.clear();
        set.addAll(nums);
    }

    @Override
    public boolean validate() {
        return StringUtils.isNotBlank(accessUrl) && CollectionUtils.isNotEmpty(nums);
    }

    public String getAccessUrl() {
        return this.accessUrl;
    }

    public Set<Integer> getSet() {
        return set;
    }

    public static class ListConverter implements Converter<List<Integer>> {

        @Override
        public List<Integer> convert(String value, BeanProperty property) throws ConvertException {
            if (StringUtils.isBlank(value)) {
                return new ArrayList<>();
            }
            return Arrays.stream(value.split(",\\s*"))
                    .map(Integer::parseInt).collect(Collectors.toList());
        }

        @Override
        public Class<?> supportedClass() {
            return List.class;
        }
    }

}
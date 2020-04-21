package org.zhenchao.kratos.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author zhenchao.wang 2018-11-02 09:55
 * @version 1.0.0
 */
public class Parser {

    private static final Logger log = LoggerFactory.getLogger(Parser.class);

    /* parse to list */

    public static List<String> toList(String[] elements, String name, Predicate<String> predicate) {
        return toList(elements, name, element -> null == element ? null : element.trim(), predicate);
    }

    public static <T> List<T> toList(String[] elements, String name, Function<String, T> function) {
        return toList(elements, name, function, element -> true);
    }

    public static <T> List<T> toList(String[] elements, String name, Function<String, T> function, Predicate<T> predicate) {
        log.debug("Parse conf item '{}' to list, value[{}]", name, Arrays.toString(elements));
        if (ArrayUtils.isEmpty(elements)) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        for (final String element : elements) {
            if (StringUtils.isBlank(element)) {
                continue;
            }
            T value = function.apply(element);
            boolean valid = predicate.test(value);
            if (valid) {
                log.info("[{}] add to list, element[{}]", name, element);
                list.add(value);
            } else {
                log.error("[{}] add to list error, illegal element[{}]", name, element);
            }
        }
        return list;
    }

    /* parse to set */

    public static Set<String> toSet(String[] elements, String name, Predicate<String> predicate) {
        return toSet(elements, name, element -> null == element ? null : element.trim(), predicate);
    }

    public static <T> Set<T> toSet(String[] elements, String name, Function<String, T> function) {
        return toSet(elements, name, function, element -> true);
    }

    public static <T> Set<T> toSet(String[] elements, String name, Function<String, T> function, Predicate<T> predicate) {
        log.debug("Parse conf item '{}' to set, value[{}]", name, Arrays.toString(elements));
        if (ArrayUtils.isEmpty(elements)) {
            return Collections.emptySet();
        }
        Set<T> set = new HashSet<>();
        for (final String element : elements) {
            if (StringUtils.isBlank(element)) {
                continue;
            }
            T value = function.apply(element.trim());
            boolean valid = predicate.test(value);
            if (valid) {
                log.info("[{}] add to set, element[{}]", name, element);
                set.add(value);
            } else {
                log.error("[{}] add to set error, illegal element[{}]", name, element);
            }
        }
        return set;
    }

    /* parse to map */

    // TODO add parse to map, by zhenchao 2020-01-15 18:30:09

}

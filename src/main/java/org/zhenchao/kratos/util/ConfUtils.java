package org.zhenchao.kratos.util;

import org.apache.commons.lang3.StringUtils;
import org.zhenchao.kratos.constant.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhenchao.wang 2020-01-10 15:42
 * @version 1.0.0
 */
public class ConfUtils {

    private static final Set<String> PREFIX_SET = new HashSet<>();

    static {
        PREFIX_SET.add(Constants.ZK_PREFIX);
        PREFIX_SET.add(Constants.CP_PREFIX);
    }

    public static boolean registerPrefix(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return false;
        }
        PREFIX_SET.add(prefix.trim().toUpperCase());
        return true;
    }

    public static boolean isValidPrefix(String prefix) {
        prefix = prefix.endsWith(Constants.DELIMITER) ? prefix : (prefix + Constants.DELIMITER);
        return PREFIX_SET.contains(prefix.toUpperCase());
    }

    public static boolean notValidPrefix(String prefix) {
        return !isValidPrefix(prefix);
    }

    public static boolean isZkResource(final String resourceName) {
        return StringUtils.startsWithIgnoreCase(resourceName, Constants.ZK_PREFIX)
                && resourceName.length() > Constants.ZK_PREFIX.length();
    }

    public static boolean isClassPathResource(final String resourceName) {
        return StringUtils.startsWithIgnoreCase(resourceName, Constants.CP_PREFIX)
                && resourceName.length() > Constants.CP_PREFIX.length();
    }

    public static Map<String, String> toMap(Properties properties) {
        return properties.entrySet().stream()
                .filter(entry -> Objects.nonNull(entry.getKey()))
                .collect(Collectors.toMap(
                        entry -> String.valueOf(entry.getKey()),
                        entry -> null == entry.getValue() ? "" : String.valueOf(entry.getValue())));
    }

    public static String toString(Properties properties) {
        List<String> items = new ArrayList<>();
        properties.forEach((name, value) -> {
            String prefix = null == name ? "=" : (name + "=");
            items.add(null == value ? prefix : (prefix + value));
        });
        return items.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    public static Object resolveDefaultValue(final Class<?> primitiveType) {
        if (boolean.class == primitiveType) {
            return Boolean.FALSE;
        }
        if (char.class == primitiveType) {
            return '\u0000';
        }
        if (float.class == primitiveType) {
            return 0f;
        }
        if (double.class == primitiveType) {
            return 0d;
        }
        if (byte.class == primitiveType) {
            return (byte) 0;
        }
        if (short.class == primitiveType) {
            return (short) 0;
        }
        if (int.class == primitiveType) {
            return 0;
        }
        return 0L;
    }

    private ConfUtils() {
    }
}

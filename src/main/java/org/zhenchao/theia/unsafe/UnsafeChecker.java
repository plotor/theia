package org.zhenchao.theia.unsafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Copy from HBase.
 *
 * @author zhenchao.wang 2018-06-19 12:00
 * @version 1.0.0
 */
public class UnsafeChecker {

    private static final Logger log = LoggerFactory.getLogger(UnsafeChecker.class);

    private static final String UNSAFE_CLASS_NAME = "sun.misc.Unsafe";
    private static boolean avail = false;
    private static boolean unaligned = false;

    static {
        avail = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            @Override
            public Boolean run() {
                try {
                    Class<?> clazz = Class.forName(UNSAFE_CLASS_NAME);
                    Field f = clazz.getDeclaredField("theUnsafe");
                    f.setAccessible(true);
                    return f.get(null) != null;
                } catch (Throwable e) {
                    log.warn("sun.misc.Unsafe is not available/accessible", e);
                }
                return false;
            }
        });
        // when Unsafe itself is not available/accessible consider unaligned as false.
        if (avail) {
            try {
                // using java.nio.Bits#unaligned() to check for unaligned-access capability
                Class<?> clazz = Class.forName("java.nio.Bits");
                Method m = clazz.getDeclaredMethod("unaligned");
                m.setAccessible(true);
                unaligned = (Boolean) m.invoke(null);
            } catch (Exception e) {
                log.warn("java.nio.Bits#unaligned() check failed."
                    + "Unsafe based read/write of primitive types won't be used", e);
            }
        }
    }

    private UnsafeChecker() {
        // private constructor to avoid instantiation
    }

    /**
     * @return true when running JVM is having sun's Unsafe package available in it and it is accessible.
     */
    public static boolean isAvailable() {
        return avail;
    }

    /**
     * @return true when running JVM is having sun's Unsafe package available in it and underlying
     * system having unaligned-access capability.
     */
    public static boolean unaligned() {
        return unaligned;
    }

}
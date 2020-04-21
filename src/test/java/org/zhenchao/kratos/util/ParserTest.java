package org.zhenchao.kratos.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author zhenchao.wang 2018-11-02 10:17
 * @version 1.0.0
 */
public class ParserTest {

    @Test
    public void toList() {
        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        List<Integer> list = Parser.toList(array, "aaa", (Function<String, Integer>) Integer::valueOf);
        Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), list);

        List<Integer> list2 = Parser.toList(array, "bbb", Integer::valueOf, integer -> integer % 2 == 0);
        Assert.assertEquals(Arrays.asList(2, 4, 6, 8), list2);
    }

    @Test
    public void toSet() {
        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Set<Integer> set = Parser.toSet(array, "aaa", (Function<String, Integer>) Integer::valueOf);
        Assert.assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)), set);

        Set<Integer> set2 = Parser.toSet(array, "bbb", Integer::valueOf, integer -> integer % 2 == 0);
        Assert.assertEquals(new HashSet<>(Arrays.asList(2, 4, 6, 8)), set2);
    }
}
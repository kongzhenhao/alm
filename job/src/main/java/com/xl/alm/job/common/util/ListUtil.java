package com.xl.alm.job.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ListUtil
 * @Author luyu
 * @date 2023.09.18 15:47
 * @Description
 */
public class ListUtil {
    /**
     * list 平均分为若干份，如果list.siz小于份数，就只分成1份
     * @param list
     * @param numSubLists
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int numSubLists) {
        List<List<T>> subLists = new ArrayList<>();
        int size = list.size();
        int remainder = size % numSubLists;
        int divisor = size / numSubLists;

        if (size < numSubLists) {
            subLists.add(list.subList(0, size));
            return subLists;
        }

        int head = 0, tail = 0;
        for (int i = 0; i < numSubLists; i++) {
            tail = head + (i < remainder ? divisor + 1 : divisor);
            subLists.add(list.subList(head, tail));
            head = tail;
        }
        return subLists;
    }
}

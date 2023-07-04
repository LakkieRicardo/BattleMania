package net.battle.core.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtils {
    public static final int TYPE_LIST = 1;
    public static final int TYPE_SET = 2;

    public static <T> void copyTo(Collection<T> orig, Collection<T> to) {
        for (T t : orig) {
            to.add(t);
        }
    }

    public static <T> List<T> convertToList(Collection<T> orig) {
        List<T> noo = new ArrayList<>();
        for (T t : orig) {
            noo.add(t);
        }
        return noo;
    }

    public static <T> Set<T> convertToSet(Collection<T> orig) {
        Set<T> noo = new HashSet<>();
        for (T t : orig) {
            noo.add(t);
        }
        return noo;
    }

    public static <T> Collection<T> convertTo(Collection<T> orig, int mode) {
        switch (mode) {
        case 1:
            return convertToList(orig);
        case 2:
            return convertToSet(orig);
        }
        return null;
    }

    public static <T> List<T> copyToList(Collection<T> from) {
        List<T> list = new ArrayList<>();
        for (T t : from) {
            list.add(t);
        }
        return list;
    }

    public static <T> Set<T> copyToSet(Collection<T> from) {
        Set<T> list = new HashSet<>();
        for (T t : from) {
            list.add(t);
        }
        return list;
    }
}
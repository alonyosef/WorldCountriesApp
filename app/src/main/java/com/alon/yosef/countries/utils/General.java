package com.alon.yosef.countries.utils;

/**
 * Created by alony on 17/07/2019.
 */

public class General {
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isArrayNullOrEmpty(String[] sArr) {
        if (sArr == null || sArr.length == 0)
            return true;
        for (String s : sArr) {
            if (!isNullOrEmpty(s))
                return false;
        }
        return true;
    }
}

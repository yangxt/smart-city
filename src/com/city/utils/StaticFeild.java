package com.elf.common;

/**
 * Created by yangyi on 13-7-30.
 */
public class StaticFeild {
    private static String biParams = null;

    public static String getBIParams() {
        if (biParams == null) {
            biParams = "";
        }
        return biParams;
    }
}

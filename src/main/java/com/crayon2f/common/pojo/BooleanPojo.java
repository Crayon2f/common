package com.crayon2f.common.pojo;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Created by feifan.gou@gmail.com on 2018/7/31 16:52.
 */
@Slf4j
public class BooleanPojo {

    private static final int ORIGIN = 1;
    private static final int BOUND = 99;
    public static boolean alwaysTrue() {

        return true;
    }

    public static boolean alwaysFalse() {

        return false;
    }

    public static boolean random() {

        int random = new Random().ints(ORIGIN, ORIGIN, BOUND).parallel().findAny().orElse(ORIGIN);
        return (random & 1) == 0; //偶数
    }

}

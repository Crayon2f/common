package com.crayon2f.common.pojo.enums;

import lombok.Getter;

/**
 * Created by feifan.gou@gmail.com on 2018/6/4 16:29.
 */
@Getter
public enum Whether {

    NO("否"),
    YES("是"),
    ;

    private int value;
    private String name;

    Whether(String name) {

        this.value = this.ordinal();
        this.name = name;
    }

}

package com.crayon2f.common.pojo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by feifan.gou@gmail.com on 2018/7/31 14:00.
 * 集合数据
 */
public class Data {

    public static final List<Integer> INTEGER_LIST = IntStream.range(97, 97 + 26).boxed().collect(Collectors.toList());

    public static final List<String> STRING_LIST = INTEGER_LIST.stream().map(integer -> Character.valueOf((char)integer.intValue()).toString()).collect(Collectors.toList());

}

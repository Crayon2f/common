package com.crayon2f.common.kit;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by feifan.gou@gmail.com on 2018/7/26 17:50.
 * 拼音工具类
 */
public final class PinYinKit {

    private static final String CHINESE_REGEX = "[\\u4E00-\\u9FA5]+";

    /**
     * 获取字符串的拼音全拼
     * @param Chinese 汉字字符串
     * @return 拼音全拼
     */
    public static String get(String Chinese) {

        if (StringUtils.isEmpty(Chinese)) {
            return Chinese;
        }
        Chinese = Chinese.toLowerCase();
        char[] srcCharArray = Chinese.toCharArray();
        // 设置汉字拼音输出的格式
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder result = new StringBuilder();
        for (char ch : srcCharArray) {
            String[] tempArray = null;
            if (Character.toString(ch).matches(CHINESE_REGEX)) {
                try {
                    tempArray = PinyinHelper.toHanyuPinyinStringArray(ch, outputFormat);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
                if (null != tempArray && tempArray.length > 0) {
                    result.append(tempArray[0]);
                }
            } else {
                result.append(Character.toString(ch));
            }
        }
        return result.toString();
    }


    /**
     * 获取和汉字首字母
     * @param Chinese 汉字
     * @return 首字母
     */
    public static String getInitials(String Chinese) {

        StringBuilder convert = new StringBuilder();
        for (int j = 0; j < Chinese.length(); j++) {
            char word = Chinese.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null && pinyinArray.length > 0) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString();
    }

    /**
     * 根据汉字全拼排序
     * @param sourceList 数据源集合
     * @param sortMapping 排序字段
     * @param <T> 泛型
     */
    public static <T> void sortByPinyin(List<T> sourceList, Function<T, String> sortMapping) {

        if (CollectionUtils.isNotEmpty(sourceList)) {
            Map<String, List<T>> sourceMap = sourceList.stream().collect(Collectors.groupingBy(t -> get(sortMapping.apply(t))));
            TreeMap<String, List<T>> sortMap = new TreeMap<>(sourceMap);
            sourceList.clear();
            sortMap.values().forEach(sourceList::addAll);
        }
    }

}

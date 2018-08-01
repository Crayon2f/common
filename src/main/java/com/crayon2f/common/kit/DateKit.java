package com.crayon2f.common.kit;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Optional;

/**
 * Created by feifan.gou@gmail.com on 2018/1/20 16:19.
 * 日期工具类
 */
public final class DateKit {

    public static LocalDate yesterday() {

        return LocalDate.now().minus(-1, ChronoUnit.DAYS);
    }

    public static LocalDate tomorrow() {

        return LocalDate.now().plus(1, ChronoUnit.DAYS);
    }

    public static LocalDate getFirstDayOfMonth(LocalDate current) {

        return Optional.ofNullable(current).orElse(LocalDate.now()).with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate getFirstDayOfMonth() {

        return getFirstDayOfMonth(LocalDate.now());
    }


    public static LocalDate getLastDayOfMonth(LocalDate current) {

        return Optional.ofNullable(current).orElse(LocalDate.now()).with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate getLastDayOfMonth() {

        return getLastDayOfMonth(LocalDate.now());
    }

    public static LocalDate getFirstDayOfWeek(LocalDate current) {

        return Optional.ofNullable(current).orElse(LocalDate.now()).with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.getValue());
    }

    public static LocalDate getFirstDayOfWeek() {

        return getFirstDayOfWeek(LocalDate.now());
    }

    public static LocalDate getLastDayOfWeek(LocalDate current) {

        return Optional.ofNullable(current).orElse(LocalDate.now()).with(ChronoField.DAY_OF_WEEK, DayOfWeek.SUNDAY.getValue());
    }

    public static LocalDate getLastDayOfWeek() {

        return getLastDayOfWeek(LocalDate.now());
    }

    public static LocalDateTime getLocalDateTimeByMillis(long millis) {

        return getInstantByMillis(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Instant getInstantByMillis(long millis) {

        return Instant.ofEpochMilli(millis);
    }

    public static Date localDateTime2Date(LocalDateTime time) {

        return Optional.ofNullable(time).map(thsTime -> Date.from(time.atZone(ZoneId.systemDefault()).toInstant())).orElse(null);
    }

    public static Date localDate2Date(LocalDate date) {

        return Optional.ofNullable(date).map(thsDate -> localDateTime2Date(date.atTime(0, 0, 0))).orElse(null);
    }


    public static LocalDateTime date2LocalDateTime(Date date) {

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate date2LocalDate(Date date) {

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalTime date2LocalTime(Date date) {

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }
}

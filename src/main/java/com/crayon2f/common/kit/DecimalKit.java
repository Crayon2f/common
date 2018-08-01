package com.crayon2f.common.kit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

/**
 * Created by feifan.gou@gmail.com on 2018/7/9 18:06.
 * 使用 double/float 计算会存在精度丢失问题
 * "+ - * /" 统一使用 BigDecimal 计算以保证精度准确
 */
public final class DecimalKit {

    public static Calculator init(double origin) {

        if (Double.isNaN(origin)) {
            throw new IllegalArgumentException("origin can't be NaN");
        }
        return new Calculator(origin);
    }

    public static double plus(double addend, double augend) {

        return init(addend).plus(augend).result;
    }

    public static double subtract(double minuend, double subtrahend) {

        return plus(minuend, -subtrahend);
    }

    public static double multiply(double multiplicand, double factor) {

        return init(multiplicand).multiply(factor).result;
    }

    public static double divide(double dividend, double divisor) {

        return init(dividend).divide(divisor).result;
    }

    public static double divide(double dividend, double divisor, int scale, RoundingMode roundingMode) {

        return init(dividend).divide(divisor, scale, roundingMode).result;
    }

    public static final class Calculator {

        private double result; //最终结果

        private Calculator(double origin) {

            this.result = origin;
        }

        private static final Function<Double, BigDecimal> DOUBLE_2_DECIMAL_FN = d -> new BigDecimal(String.valueOf(d));

        private static final int DEFAULT_SCALE = 2;

        public Calculator plus(double augend) {

            checkParam(augend);
            result = DOUBLE_2_DECIMAL_FN.apply(result).add(DOUBLE_2_DECIMAL_FN.apply(augend)).doubleValue();
            return this;
        }

        public Calculator subtract(double subtrahend) {

            result = plus(-subtrahend).result;
            return this;
        }

        public Calculator multiply(double multiplicand) {

            checkParam(multiplicand);
            result = DOUBLE_2_DECIMAL_FN.apply(result).multiply(DOUBLE_2_DECIMAL_FN.apply(multiplicand)).doubleValue();
            return this;
        }

        public Calculator divide(double divisor) {

            result = divide(divisor, DEFAULT_SCALE, RoundingMode.HALF_UP).result;
            return this;
        }

        public Calculator divide(double divisor, int scale, RoundingMode roundingMode) {

            if (divisor == 0.0d) {
                throw new IllegalArgumentException("divisor can't be zero");
            }
            checkParam(divisor);
            result = DOUBLE_2_DECIMAL_FN.apply(result).divide(DOUBLE_2_DECIMAL_FN.apply(divisor), scale, roundingMode).doubleValue();
            return this;
        }

        public double calculate() {

            return result;
        }

        private void checkParam(double param) {

            if (Double.isNaN(param)) {
                throw new IllegalArgumentException("there is NaN in your params");
            }
        }
    }

}

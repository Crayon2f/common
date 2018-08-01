package com.crayon2f.common.kit;

import static java.lang.Double.MAX_EXPONENT;
import static java.lang.Double.NaN;
import static java.lang.Math.getExponent;

/**
 * 计算线性差值 copy from [com.google.common.math.LinearTransformation]
 * 唯一区别是：计算时候，使用 BigDecimal计算避免了精度丢失
 */

public abstract class LinearKit {

    public static LinearKitBuilder mapping(double x1, double y1) {
        checkArgument(isFinite(x1) && isFinite(y1));
        return new LinearKitBuilder(x1, y1);
    }

    public static final class LinearKitBuilder {

        private final double x1;
        private final double y1;

        private LinearKitBuilder(double x1, double y1) {
            this.x1 = x1;
            this.y1 = y1;
        }

        public LinearKit and(double x2, double y2) {
            checkArgument(isFinite(x2) && isFinite(y2));
            if (x2 == x1) {
                checkArgument(y2 != y1);
                return new VerticalLinearTransformation(x1);
            } else {
                return withSlope((y2 - y1) / (x2 - x1));
            }
        }

        public LinearKit withSlope(double slope) {
            checkArgument(!Double.isNaN(slope));
            if (isFinite(slope)) {
                double yIntercept = y1 - x1 * slope;
                return new RegularLinearTransformation(slope, yIntercept);
            } else {
                return new VerticalLinearTransformation(x1);
            }
        }
    }

    public static LinearKit vertical(double x) {
        checkArgument(isFinite(x));
        return new VerticalLinearTransformation(x);
    }

    public static LinearKit horizontal(double y) {
        checkArgument(isFinite(y));
        double slope = 0.0;
        return new RegularLinearTransformation(slope, y);
    }

    public static LinearKit forNaN() {
        return NaNLinearTransformation.INSTANCE;
    }

    public abstract boolean isVertical();

    public abstract boolean isHorizontal();

    public abstract double slope();

    public abstract double transform(double x);

    public abstract LinearKit inverse();

    private static boolean isFinite(double d) {
        return getExponent(d) <= MAX_EXPONENT;
    }

    private static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    private static final class RegularLinearTransformation extends LinearKit {

        final double slope;
        final double yIntercept;

        LinearKit inverse;

        RegularLinearTransformation(double slope, double yIntercept) {
            this.slope = slope;
            this.yIntercept = yIntercept;
            this.inverse = null; // to be lazily initialized
        }

        RegularLinearTransformation(double slope, double yIntercept, LinearKit inverse) {
            this.slope = slope;
            this.yIntercept = yIntercept;
            this.inverse = inverse;
        }

        @Override
        public boolean isVertical() {
            return false;
        }

        @Override
        public boolean isHorizontal() {
            return (slope == 0.0);
        }

        @Override
        public double slope() {
            return slope;
        }

        @Override
        public double transform(double x) {

            return DecimalKit.init(x).multiply(slope).plus(yIntercept).calculate();
        }

        @Override
        public LinearKit inverse() {
            LinearKit result = inverse;
            return (result == null) ? inverse = createInverse() : result;
        }

        @Override
        public String toString() {
            return String.format("y = %g * x + %g", slope, yIntercept);
        }

        private LinearKit createInverse() {
            if (slope != 0.0) {
                return new RegularLinearTransformation(1.0 / slope, -1.0 * yIntercept / slope, this);
            } else {
                return new VerticalLinearTransformation(yIntercept, this);
            }
        }
    }

    private static final class VerticalLinearTransformation extends LinearKit {

        final double x;

        LinearKit inverse;

        VerticalLinearTransformation(double x) {
            this.x = x;
            this.inverse = null; // to be lazily initialized
        }

        VerticalLinearTransformation(double x, LinearKit inverse) {
            this.x = x;
            this.inverse = inverse;
        }

        @Override
        public boolean isVertical() {
            return true;
        }

        @Override
        public boolean isHorizontal() {
            return false;
        }

        @Override
        public double slope() {
            throw new IllegalStateException();
        }

        @Override
        public double transform(double x) {
            throw new IllegalStateException();
        }

        @Override
        public LinearKit inverse() {
            LinearKit result = inverse;
            return (result == null) ? inverse = createInverse() : result;
        }

        @Override
        public String toString() {
            return String.format("x = %g", x);
        }

        private LinearKit createInverse() {
            return new RegularLinearTransformation(0.0, x, this);
        }
    }

    private static final class NaNLinearTransformation extends LinearKit {

        static final NaNLinearTransformation INSTANCE = new NaNLinearTransformation();

        @Override
        public boolean isVertical() {
            return false;
        }

        @Override
        public boolean isHorizontal() {
            return false;
        }

        @Override
        public double slope() {
            return NaN;
        }

        @Override
        public double transform(double x) {
            return NaN;
        }

        @Override
        public LinearKit inverse() {
            return this;
        }

        @Override
        public String toString() {
            return "NaN";
        }
    }
}

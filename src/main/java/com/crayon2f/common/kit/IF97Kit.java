package com.crayon2f.common.kit;

import com.hummeling.if97.IF97;
import lombok.Getter;

/**
 * Created by feifan.gou@gmail.com on 2018/7/16 10:01.
 * IF97 工具
 */
public final class IF97Kit {

    private static final IF97 IF_97 = new IF97();

    private IF97Kit() {
    }

    public static SaturatedProperty getSaturatedByPressure(double pressure) {

        verifyPressure(pressure);
        return new SaturatedPropertyByPressure(pressure);
    }

    public static SaturatedProperty getSaturatedByTemperature(double temperature) {

        verifyTemperature(temperature);
        return new SaturatedPropertyByTemperature(temperature);
    }

    public static UnsaturatedProperty getUnsaturated(double pressure, double temperature) {

        verifyPressure(pressure);
        verifyTemperature(temperature);
        return new UnsaturatedProperty(pressure, temperature);
    }

    private static double kevin2temperature(double kevin) {

        return DecimalKit.subtract(kevin, IF97.T0);
    }

    private static double temperature2kevin(double temperature) {

        return DecimalKit.plus(temperature, IF97.T0);
    }

    private static void verifyTemperature(double temperature) {

        if (DecimalKit.plus(temperature, IF97.T0) > IF97.Tc || temperature < 0) {
            throw new IllegalArgumentException("temperature should be [0~373.946]");
        }
    }

    private static void verifyPressure(double pressure) {

        if(pressure < 0 || pressure > IF97.pc) {
            throw new IllegalArgumentException("pressure should be [0~22.0640Mpa]");
        }
    }



    @Getter
    public static class SaturatedProperty {

        protected Double temperature; //"温度(T)"
        protected Double pressure; //"压强(P)"
        protected Double liquidVolume;//比热 //"比热容（水）(V')"
        protected Double vaporVolume; //"比热容（蒸汽）(V'')"
        protected Double liquidEnthalpy; //"焓值（水）(H')"
        protected Double vaporEnthalpy; //"焓值（蒸汽）(H'')"
        protected Double latentHeat; //"潜热(R)"
        protected Double liquidEntropy; //"熵（水）(S')"
        protected Double vaporEntropy; //"熵（蒸汽）(S'')"

        private SaturatedProperty(double param){}
    }

    private static class SaturatedPropertyByPressure extends SaturatedProperty {

        private SaturatedPropertyByPressure(double pressure) {

            super(pressure);
            this.pressure = pressure;
            this.temperature = IF_97.saturationTemperatureP(pressure);
            this.vaporEntropy = IF_97.specificEntropySaturatedVapourP(pressure);
            this.liquidEntropy = IF_97.specificEntropySaturatedLiquidP(pressure);
            this.vaporEnthalpy = IF_97.specificEnthalpySaturatedVapourP(pressure);
            this.liquidEnthalpy = IF_97.specificEnthalpySaturatedLiquidP(pressure);
            this.vaporVolume = IF_97.specificVolumePH(pressure, this.vaporEnthalpy);
            this.liquidVolume = IF_97.specificVolumePH(pressure, this.liquidEnthalpy);
            this.latentHeat = this.vaporEnthalpy - this.liquidEnthalpy;
            this.temperature = kevin2temperature(this.temperature);
        }
    }

    private static class SaturatedPropertyByTemperature extends  SaturatedProperty {

        private SaturatedPropertyByTemperature(double temperature) {

            super(temperature);
            this.temperature = temperature;
            double kevin = temperature2kevin(temperature);
            this.pressure = IF_97.saturationPressureT(kevin);
            this.vaporEntropy = IF_97.specificEntropySaturatedVapourT(kevin);
            this.liquidEntropy = IF_97.specificEntropySaturatedLiquidT(kevin);
            this.vaporEnthalpy = IF_97.specificEnthalpySaturatedVapourT(kevin);
            this.liquidEnthalpy = IF_97.specificEnthalpySaturatedLiquidT(kevin);
            this.vaporVolume = IF_97.specificVolumePH(pressure, this.vaporEnthalpy);
            this.liquidVolume = IF_97.specificVolumePH(pressure, this.liquidEnthalpy);
            this.latentHeat = this.vaporEnthalpy - this.liquidEnthalpy;
        }
    }


    @Getter
    public static class UnsaturatedProperty {

        private Double temperature; //"温度(T)"
        private Double pressure; //"压强(P)"
        private Double volume; //"比热容(V)"
        private Double enthalpy; //"焓值(H)"
        private Double entropy; //"熵(S)"
        private int state = 1; // 1:气态 2:液态
        private boolean saturated; // 是否饱和
        private SaturatedProperty saturatedProperty; //饱和


        private UnsaturatedProperty(double pressure, double temperature) {

            double kevin = temperature2kevin(temperature);
            double saturatedTemperature = IF_97.saturationTemperatureP(pressure);
            this.temperature = temperature;
            this.pressure = pressure;
            this.saturated = kevin == saturatedTemperature;
            this.saturatedProperty = new SaturatedPropertyByPressure(pressure);
            if (!this.saturated) { //不饱和
                this.state = kevin > saturatedTemperature ? 1 : 2;
                this.volume = IF_97.specificVolumePT(pressure, kevin);
                this.enthalpy = IF_97.specificEnthalpyPT(pressure, kevin);
                this.entropy = IF_97.specificEntropyPT(pressure, kevin);
            }
        }
    }
}

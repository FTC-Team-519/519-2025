package org.firstinspires.ftc.teamcode.util;

public class RobotMath {

    //copied from https://github.com/openjdk/jdk/blob/256a9beffc106d6657a912a33f97e7f97acbb1e1/src/java.base/share/classes/java/lang/Math.java#L2219
    /**
     * Clamps the value to fit between min and max. If the value is less
     * than {@code min}, then {@code min} is returned. If the value is greater
     * than {@code max}, then {@code max} is returned. Otherwise, the original
     * value is returned. If value is NaN, the result is also NaN.
     * <p>
     * Unlike the numerical comparison operators, this method considers
     * negative zero to be strictly smaller than positive zero.
     * E.g., {@code clamp(-0.0, 0.0, 1.0)} returns 0.0.
     *
     * @param value value to clamp
     * @param min minimal allowed value
     * @param max maximal allowed value
     * @return a clamped value that fits into {@code min..max} interval
     * @throws IllegalArgumentException if either of {@code min} and {@code max}
     * arguments is NaN, or {@code min > max}, or {@code min} is +0.0, and
     * {@code max} is -0.0.
     *
     * @since 21
     */
    public static double clamp(double value, double min, double max) {
        // This unusual condition allows keeping only one branch
        // on common path when min < max and neither of them is NaN.
        // If min == max, we should additionally check for +0.0/-0.0 case,
        // so we're still visiting the if statement.
        if (!(min < max)) { // min greater than, equal to, or unordered with respect to max; NaN values are unordered
            if (Double.isNaN(min)) {
                throw new IllegalArgumentException("min is NaN");
            }
            if (Double.isNaN(max)) {
                throw new IllegalArgumentException("max is NaN");
            }
            if (Double.compare(min, max) > 0) {
                throw new IllegalArgumentException(min + " > " + max);
            }
            // Fall-through if min and max are exactly equal (or min = -0.0 and max = +0.0)
            // and none of them is NaN
        }
        return Math.min(max, Math.max(value, min));
    }


    static protected double deadzoneAndSmoothstep(double raw_value, double limit, double steepness){
        if (Math.abs(raw_value) < limit){
            return 0.0d;
        }
        double output = (Math.abs(raw_value)-limit) / (1.0d - limit);
        double TwoPowerSteepness = Math.pow(2, steepness);
        double denom = TwoPowerSteepness*Math.pow(0.5, steepness + 1) + 0.5d;
        if (output>1.0){
            return 1.0d;
        }
        if (output<0.5){
            output = TwoPowerSteepness*Math.pow(output, steepness + 1)/denom;
        }else{
            output = 1.0d + TwoPowerSteepness*(output-1)*Math.pow(1-output, steepness)/denom;
        }
        Math.min(1.0d, Math.max(0.0d, output));
        if (raw_value<0) {
            output *= -1;
        }
        return output;
    }
}

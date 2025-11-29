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
    /*
        Returns a double array of length 4, with the values resolving to
        the drive motor powers in the following order:
        0: frontLeft
        1: frontRight
        2: backLeft
        3: backRight
     */
    public static double[] motorPowers(double xOffset, double yaw) {
        double[] ans = new double[4];

        double fL = - xOffset - yaw;
        double fR = xOffset + yaw;
        double bL = xOffset - yaw;
        double bR = - xOffset + yaw;

        double max = Math.max(Math.abs(fL),Math.abs(fR));
        max = Math.max(max,Math.abs(bL));
        max = Math.max(max,Math.abs(bR));
        max = max * 2;

        if(max>1.0d) {
            fL /= max;
            fR /= max;
            bL /= max;
            bR /= max;
        }

        ans[0] = fL;
        ans[1] = fR;
        ans[2] = bL;
        ans[3] = bR;

        return ans;
    }

    public static double trueMod(double dividend, double divisor) {
        if (divisor == 0) {
            // Handle division by zero based on your application's requirements
            // e.g., throw an IllegalArgumentException or return NaN
            return Double.NaN;
        }

        double remainder = dividend % divisor;

        // Adjust the remainder to always be in the range [0, |divisor|) if divisor is positive,
        // or (0, -|divisor|] if divisor is negative.
        if (divisor > 0 && remainder < 0) {
            remainder += divisor;
        } else if (divisor < 0 && remainder > 0) {
            remainder += divisor;
        }

        return remainder;
    }

    // helper to get the encoder distance from a given angle to april tag
    public static int angleToTicks(double yawDeg) {
        double angleRad = Math.toRadians(yawDeg);
        double dist = (angleRad * 15.5) / 2.0;     // distance each wheel must move, (15.5 in between wheels)
        double rotations = dist / (2 * Math.PI * 2); // number of rotations necessary, (2 in is radius of wheels)
        return (int)(rotations * 384.5); // number of ticks to rotate that much (384.5 is ticks per rev for gobilda 5202)
    }

    public static double outPower(double dist) {
        if(dist<=40) return 0.62;
        return 0.00265359 * (dist - 40) + 0.62; // magic numbers moment
    }
}

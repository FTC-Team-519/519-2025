package org.firstinspires.ftc.teamcode.util.hardware;

import android.graphics.Color;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class IntakeColorSensor {

    public enum pieceType {
        NOT_THERE,
        GREEN,
        PURPLE;

        public String toString() {
            if (this == NOT_THERE){
                return "not there";
            } else if (this == GREEN) {
                return "green";
            }else if (this == PURPLE) {
                return "purple";
            }
            return "null";
        }
    }

    private final ColorRangeSensor colorSensor;
    public IntakeColorSensor(HardwareMap hardwareMap, String name){
        colorSensor = hardwareMap.get(ColorRangeSensor.class, name);
    }

    public double get_distance_inch(){
        return colorSensor.getDistance(DistanceUnit.INCH);
    }

    public int[] get_rgb(){
        return new int[]{colorSensor.red(), colorSensor.blue(), colorSensor.red()};
    }
    public float[] get_hsv(){
        float[] hsv = new float[3];
        int[] rgb = get_rgb();
        Color.RGBToHSV(rgb[0] * 8, rgb[1] * 8, rgb[2] * 8, hsv);
        return hsv;
    }

    /*
    are we being covered by the disk
     */
    public boolean is_covered(){
        return (get_distance_inch() < 5.0 && get_hsv()[1] < 10);
    }

    public pieceType get_piece(){
        if (get_distance_inch() > (2.5) || get_distance_inch() < (0.32)) {
            return IntakeColorSensor.pieceType.NOT_THERE;
        } else if ((double)(get_rgb()[1])/get_rgb()[0] > 2.1) {
            return IntakeColorSensor.pieceType.GREEN;
        } else {
            return IntakeColorSensor.pieceType.PURPLE;
        }
    }

    public double get_alpha(){
        return colorSensor.alpha();
    }

    public static pieceType combine(pieceType p1, pieceType p2){
        if (p1 == pieceType.NOT_THERE){
            return p2;
        }else{
            return p1;
        }
    }

    public static pieceType[] defaultMotif(){
        return new pieceType[]{pieceType.PURPLE, pieceType.PURPLE, pieceType.GREEN};
    }
}

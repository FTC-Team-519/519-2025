package org.firstinspires.ftc.teamcode.util.hardware;

import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.util.RobotMath;

public class Outtake {

    public final static double optimalP = 7;

    public final static double optimalF = 12.1728;

    private final DcMotorEx LeftMotor;
    private final DcMotorEx RightMotor;
    public Outtake(HardwareMap hardwareMap) {
        LeftMotor = hardwareMap.get(DcMotorEx.class,"leftOuttakeMotor");
        RightMotor = hardwareMap.get(DcMotorEx.class,"rightOuttakeMotor");

        LeftMotor.setDirection(DcMotorEx.Direction.FORWARD);
        RightMotor.setDirection(DcMotorEx.Direction.REVERSE);

        LeftMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        RightMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        LeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        RightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        LeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        RightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public DcMotorEx getLeftMotor() {return LeftMotor;}
    public DcMotorEx getRightMotor() {return RightMotor;}

    public void changePIDFCoefficients(double P, double F) {
        LeftMotor.setVelocityPIDFCoefficients(P, 0, 0, F);
        RightMotor.setVelocityPIDFCoefficients(P, 0, 0, F);
    }

    public void setVelocity(double velocity) {
        LeftMotor.setVelocity(velocity);
        RightMotor.setVelocity(velocity/1.1); // for straightness
    }

    public void runMotors(double power) {
        LeftMotor.setVelocity(power * RobotMath.POWER_TO_VELOCITY_CONVERSION_FACTOR); // we do the conversion behind the scenes for simplicity
        RightMotor.setVelocity(power/1.1 * RobotMath.POWER_TO_VELOCITY_CONVERSION_FACTOR); //this will help the robot shoot straighter
    }
}

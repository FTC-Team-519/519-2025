package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.*;
public class Outtake {

    private DcMotor LeftMotor, RightMotor;
    public Outtake(HardwareMap hardwareMap) {
        LeftMotor = hardwareMap.get(DcMotor.class,"leftOuttakeMotor");
        RightMotor = hardwareMap.get(DcMotor.class,"rightOuttakeMotor");

        LeftMotor.setDirection(DcMotor.Direction.FORWARD);
        RightMotor.setDirection(DcMotor.Direction.REVERSE);

        LeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public DcMotor getLeftMotor() {return LeftMotor;}
    public DcMotor getRightMotor() {return RightMotor;}

    public void runMotors(double power) {
        LeftMotor.setPower(power);
        RightMotor.setPower(power);
    }


}

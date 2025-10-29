package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Testteleop")
public class Testteleop extends OpMode {

    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;



    @Override
    public void init() {
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight");

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void loop() {
        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);

        if (gamepad1.dpad_up) {
            frontRightMotor.setPower(1);
            backLeftMotor.setPower(1);
            frontLeftMotor.setPower(1);
            backRightMotor.setPower(1);
        } else if (gamepad1.dpad_down) {
            frontRightMotor.setPower(-1);
            backLeftMotor.setPower(-1);
            frontLeftMotor.setPower(-1);
            backRightMotor.setPower(-1);
        } else if (gamepad1.dpad_left) {
            frontRightMotor.setPower(1);
            backLeftMotor.setPower(1);
            frontLeftMotor.setPower(-1);
            backRightMotor.setPower(-1);
        } else if (gamepad1.dpad_right) {
            frontRightMotor.setPower(-1);
            backLeftMotor.setPower(-1);
            frontLeftMotor.setPower(1);
            backRightMotor.setPower(1);
        }
    }
}

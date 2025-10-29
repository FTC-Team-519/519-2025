package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "TestTeleOp")
public class TestTeleOp extends OpMode {
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;


    @Override
    public void init() {
        frontLeftMotor = hardwareMap.get(DcMotor.class, "leftFront");
        frontRightMotor = hardwareMap.get(DcMotor.class, "rightFront");
        backLeftMotor = hardwareMap.get(DcMotor.class, "leftBack");
        backRightMotor = hardwareMap.get(DcMotor.class, "rightBack");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);

        if (gamepad1.dpad_up) {
            frontLeftMotor.setPower(1);
            frontRightMotor.setPower(1);
            backLeftMotor.setPower(1);
            backRightMotor.setPower(1);
        }
        else if (gamepad1.dpad_down){
            frontLeftMotor.setPower(-1);
            frontRightMotor.setPower(-1);
            backLeftMotor.setPower(-1);
            backRightMotor.setPower(-1);
        }
        else if (gamepad1.dpad_left){
            frontLeftMotor.setPower(-1);
            frontRightMotor.setPower(1);
            backLeftMotor.setPower(-1);
            backRightMotor.setPower(1);
        }
    }

}

package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.RobotMath;
import org.firstinspires.ftc.teamcode.util.hardware.Rotator;

import static org.firstinspires.ftc.teamcode.teleops.CompetitionTeleOp.RotationSetting.AutoRotate;

@TeleOp(name = "Disk PID tuning")
public class PIDTuning extends OpModeBase {
    private boolean is_auto_rotating = false;

    @Override
    public void init() {
        super.init();
    }

    private double p_coef = Rotator.POS_COEF;
    private double d_coef = Rotator.DERIVATIVE_COEF;

    @Override
    public void loop() {
        if(gamepad1.left_trigger != 0 || gamepad1.right_trigger != 0){
            is_auto_rotating = false;
            robot.getRotator().runMotor(gamepad1.left_trigger - gamepad1.right_trigger);
        }

        if (gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0 && !is_auto_rotating) {
            robot.getRotator().resetEncoder();
            robot.getRotator().getMotor().setTargetPosition(robot.getRotatorPosition());
            is_auto_rotating = true;
        }

        if (gamepad1.leftBumperWasReleased()) {
            //clockwise
            robot.getRotator().setDiskRotation(false);
            is_auto_rotating = true;
        }

        if (is_auto_rotating) {
            robot.getRotator().runMotorToPositionPID(p_coef, d_coef);
        }

        if (gamepad1.dpadUpWasPressed()){
            p_coef += 0.001;
        }
        if (gamepad1.dpadDownWasPressed()){
            p_coef -= 0.001;
        }
        if (gamepad1.dpadRightWasPressed()){
            d_coef += 0.01;
        }
        if (gamepad1.dpadLeftWasPressed()){
            d_coef -= 0.01;
        }



        p_coef = RobotMath.clamp(p_coef, 0.0, 1.0);
        d_coef = RobotMath.clamp(d_coef, -1.0, 0.0);


        telemetry.addData("current disk pos: ", robot.getRotator().getEncoderPosition());
        telemetry.addData("desired pos:", robot.getRotator().getMotor().getTargetPosition());
        telemetry.addData("position error", robot.getRotator().getMotor().getTargetPosition() - robot.getRotator().getEncoderPosition());
        telemetry.addData("motor_power", robot.getRotator().getMotor().getPower());
        telemetry.addData("is auto rotating", is_auto_rotating);
        telemetry.addData("p_coef", p_coef);
        telemetry.addData("d_coef", d_coef);
        telemetry.update();

    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.util.OpModeBase;

@TeleOp(name = "Competition TeleOp")
public class CompetitionTeleOp extends OpModeBase {
    private boolean driving_field_centric = false;
    private boolean rotating_at_all = false;
    private boolean manual_adjustment = false;

    @Override
    public void init() {
        super.init();

    }

    @Override
    public void loop() {

        //outtake flywheels
        robot.runOuttake(-gamepad2.left_stick_y);

        //kicker
        if(gamepad2.aWasPressed()) {
            robot.engageDisengageKicker();
        }


        //intake
        robot.runIntake(gamepad1.right_trigger);

        //rotating
        robot.getRotator().runMotor(0.0);

        //rotating the disk to a specific position
        if (gamepad1.leftBumperWasReleased()){
            //clockwise
            robot.getRotator().setDiskRotation(true);
            rotating_at_all = true;
        }else if(gamepad1.rightBumperWasReleased()){
            //counter clock wise
            robot.getRotator().setDiskRotation(false);
            rotating_at_all = true;
        }
        if (rotating_at_all){
            robot.getRotator().runMotorToPosition(1.0);
            if (robot.getRotator().isAtPosition()){
                rotating_at_all = false;
            }
        }

        //manual rotation
        if (gamepad1.left_trigger != 0 || gamepad1.right_trigger != 0){
            rotating_at_all = false;
            manual_adjustment = true;
            robot.getRotator().runMotor(gamepad1.left_trigger - gamepad1.right_trigger);
        }
        //if we are no longer doing manual input, but we were previously
        if (gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0 && manual_adjustment){
            manual_adjustment = false;
            robot.getRotator().resetEncoder();
        }

        //driving
        if (gamepad1.aWasReleased()) {
            driving_field_centric = !driving_field_centric;
        }

        //regular driving
        if (driving_field_centric) {
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double angle = robot.getYaw() * Math.PI / 180.0;
            driving(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle), gamepad1.right_stick_x);
        } else {
            driving(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }

        //we want regular driving to be overwritten by dpad driving
        if (gamepad1.dpad_down || gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right) {
            double x = 0;
            double y = 0;
            if (gamepad1.dpad_down) {
                y -= 1;
            }
            if (gamepad1.dpad_up) {
                y += 1;
            }
            if (gamepad1.dpad_left) {
                x -= 1;
            }
            if (gamepad1.dpad_right) {
                x += 1;
            }
            double angle = Math.PI / 4.0; // we want it rotated by 45 deg so that we can line up the shot easier
            driving(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle), 0.0);
        }

        //telemetry
        telemetry();
    }

    private void driving(double x, double y, double rot) {
        double lf_power = y + x + rot;
        double rf_power = y - x - rot;
        double lb_power = y - x + rot;
        double rb_power = y + x - rot;

        double scale = Math.abs(y) + Math.abs(x) + Math.abs(rot);

        lf_power /= scale;
        rf_power /= scale;
        lb_power /= scale;
        rb_power /= scale;

        robot.setLeftFrontPower(lf_power);
        robot.setRightFrontPower(rf_power);
        robot.setLeftBackPower(lb_power);
        robot.setRightBackPower(rb_power);
    }


    private void telemetry(){
        telemetry.addData("Field Centric:", driving_field_centric);
        telemetry.addData("manually_adjusting_disk", manual_adjustment);
        telemetry.addData("outtake power:", robot.getOuttake().getLeftMotor().getPower());
        telemetry.update();
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.RobotMath;
import org.firstinspires.ftc.teamcode.util.Rotator;
import org.firstinspires.ftc.teamcode.util.commands.Command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import java.util.Arrays;

@TeleOp(name = "Competition TeleOp")
public class CompetitionTeleOp extends OpModeBase {
    private boolean driving_field_centric = false;
    private boolean is_auto_rotating = false;
    private boolean is_manual_rotating = false;
    private boolean intaking = false;
    private double outtake_power = 0.0;

    private boolean constant_rotation = false;

    private Queue<Command> commands_to_run = new LinkedList<>();

    @Override
    public void init() {
        super.init();

    }

    //FIXME: follow the specification in teleopcontrols.txt
    public void loop(){
        if (gamepad2.aWasReleased()){
            commands_to_run = new LinkedList<>(); //clear the entire list
        }

        if (!commands_to_run.isEmpty()){
            Command command = commands_to_run.peek();
            assert command != null;
            command.run();
            if (command.isDone()){
                command.shutdown();
                commands_to_run.remove();
                Command next_command = commands_to_run.peek();
                if (next_command != null) {
                    next_command.init();
                }
            }
        }else{
            manual_controls();
        }
    }


    public void manual_controls() {
        camera();

        //outtake flywheels
        outtake();

        //kicker
        kicking();

        //rotating
        rotating();

        //intake
        intake();

        //driving
        driving();

        //telemetry
        telemetry();
    }

    private void camera(){
        //save cpu using camera
        if(gamepad2.leftBumperWasPressed()) {
            robot.stopStreaming();
        } else if(gamepad2.rightBumperWasPressed()) {
            robot.resumeStreaming();
        }
    }

    private void outtake(){
        if (gamepad2.dpadUpWasReleased()) {
            outtake_power += 0.02;
        }

        if (gamepad2.dpadDownWasReleased()) {
            outtake_power -= 0.02;
        }
        if (gamepad2.xWasReleased()){
            outtake_power = 0.0;
        }
        if(gamepad2.yWasReleased()){
            outtake_power = 1.0;
        }
        outtake_power = RobotMath.clamp(outtake_power, 0.0, 1.0);
        robot.runOuttake(outtake_power);
    }

    private void kicking(){
        if (gamepad2.aWasPressed()) {
            robot.changeKicking();
        }
    }

    private void rotating(){
        if(gamepad1.yWasPressed()) {
            constant_rotation = !constant_rotation;
        }

        boolean setting_rotation = false;
        //rotating the disk to a specific position
        if (gamepad1.leftBumperWasReleased()) {
            //clockwise
            robot.getRotator().setDiskRotation(true);
            is_auto_rotating = true;
        } else if (gamepad1.rightBumperWasReleased()) {
            //counter clock wise
            robot.getRotator().setDiskRotation(false);
            is_auto_rotating = true;
        }
        if (is_auto_rotating) {
            setting_rotation = true;
            robot.getRotator().runMotorToPosition(0.3);
            if (robot.getRotator().isAtPosition()) {
                is_auto_rotating = false;
            }
        }


        //manual rotation
        if (gamepad1.left_trigger != 0 || gamepad1.right_trigger != 0) {
            is_auto_rotating = false;
            is_manual_rotating = true;
            setting_rotation = true;
            robot.getRotator().runMotor((gamepad1.left_trigger - gamepad1.right_trigger));//*Rotator.MAX_SPEED);
        }
        //if we are no longer doing manual input, but we were previously
        if (gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0 && is_manual_rotating) {
            is_manual_rotating = false;
            robot.getRotator().resetEncoder();
            robot.getRotator().getMotor().setTargetPosition(robot.getRotatorPosition());
        }

        if(constant_rotation) {
            setting_rotation = true;
            robot.getRotator().runMotor(-0.3);
        }

        if (!setting_rotation) {
            robot.getRotator().runMotor(0.0);
        }

        if (!robot.getRotator().isAtPosition() && !is_manual_rotating && !constant_rotation){
            is_auto_rotating = true;
        }
    }

    private void intake(){
        if (gamepad1.bWasReleased()) {
            intaking = !intaking;
        }
        if (intaking) {
            robot.runIntake(1.0);
        } else {
            robot.runIntake(0.0);
        }
    }

    private void driving(){
        if (gamepad1.aWasReleased()) {
            driving_field_centric = !driving_field_centric;
            robot.resetYaw();
        }

        //regular driving
        if (driving_field_centric) {
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double mag = Math.hypot(x, y);
            double drive_angle = Math.atan2(y, x);
            double robot_angle = robot.getYaw() * Math.PI / 180.0;
            double angle = drive_angle - robot_angle;

            raw_driving(mag * Math.cos(angle), mag * Math.sin(angle), gamepad1.right_stick_x);
        } else {
            raw_driving(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
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
            double angle = -3.0 * Math.PI / 4.0; // we want it rotated by 135 deg so that we can line up the shot easier
            raw_driving(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle), 0.0);
        }
    }

    private void raw_driving(double x, double y, double rot) {
        double lf_power = y + x + rot;
        double rf_power = y - x - rot;
        double lb_power = y - x + rot;
        double rb_power = y + x - rot;

        double scale = Math.abs(y) + Math.abs(x) + Math.abs(rot);

        if (scale > 1.0) {
            lf_power /= scale;
            rf_power /= scale;
            lb_power /= scale;
            rb_power /= scale;
        }

        robot.setLeftFrontPower(lf_power);
        robot.setRightFrontPower(rf_power);
        robot.setLeftBackPower(lb_power);
        robot.setRightBackPower(rb_power);
    }


    private void telemetry() {

        double[] distanceArray = robot.getDistancesFromAprilTag();

        telemetry.addData("X Offset",distanceArray[0]);
        telemetry.addData("Y Offset",distanceArray[1]);
        telemetry.addData("Z Offset",distanceArray[2]);
        telemetry.addData("Yaw",distanceArray[3]);

        telemetry.addData("AprilTag Ids found", Arrays.toString(robot.getIds()));

        telemetry.addData("Field Centric:", driving_field_centric);
        telemetry.addData("manually_adjusting_disk:", is_manual_rotating);
        telemetry.addData("rotating disk:", is_auto_rotating);
        telemetry.addData("outtake power:", robot.getOuttake().getLeftMotor().getPower());
        try{
            //if (robot.getRotator().getPieceColor() != null) {
                telemetry.addData("sensing:", robot.getRotator().getPieceColor().toString());
            //}
        } catch (NullPointerException ignored){

        }
        telemetry.addData("current disk pos: ", robot.getRotator().getEncoderPosition());
        telemetry.addData("desired pos:", robot.getRotator().getMotor().getTargetPosition());
        telemetry.addData("current disk pos(normalized): ", robot.getRotator().getEncoderPosition()/(double) Rotator.CLICKS_PER_ROTATION);
        telemetry.update();
    }
}

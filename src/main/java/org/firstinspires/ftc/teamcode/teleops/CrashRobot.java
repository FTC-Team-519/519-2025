package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.util.OpModeBase;

import static org.firstinspires.ftc.teamcode.teleops.CompetitionTeleOpTest.RotationSetting.*;

@TeleOp(name = "crash robot")
public class CrashRobot extends OpModeBase {


    private CompetitionTeleOpTest.RotationSetting rotationSetting = AutoRotate;

    @Override
    public void loop() {
        switch (this.rotationSetting){
            case AutoAlignment:
                robot.getRotator().runMotor(-0.1);
                if(robot.isRotatorAligned()){
                    this.rotationSetting = AutoRotate;
                    robot.getRotator().resetEncoder();
                    robot.getRotator().getMotor().setTargetPosition(robot.getRotatorPosition());
                }
                break;
            case AutoRotate:
                if (!robot.getRotator().isAtPosition()) {
                    robot.getRotator().runMotorToPositionPID();
                    if (gamepad2.right_stick_x != 0.0){
                        robot.getKicker().runRotator(0.5);
                    }
                }else{
                    robot.getRotator().runMotor(0.0);
                }
                break;
            case ManualRotate:
                if (gamepad2.left_trigger == 0 && gamepad2.right_trigger == 0) {
                    this.rotationSetting = AutoRotate;
                    robot.getRotator().resetEncoder();
                    robot.getRotator().getMotor().setTargetPosition(robot.getRotatorPosition());
                }else{
                    robot.getRotator().runMotor((gamepad2.left_trigger - gamepad2.right_trigger));
                }
                break;
            case ConstantRotate:
                robot.getRotator().runMotor(-0.3);
                break;
        }

//        if (gamepad2.yWasPressed()) {
//            if(this.rotationSetting == ConstantRotate){
//                this.rotationSetting = AutoRotate;
//            }else{
//                this.rotationSetting = ConstantRotate;
//            }
//            if (this.rotationSetting != ConstantRotate) {
//                robot.getRotator().setDiskRotation(true);
//            }
//        }

        //rotating the disk to a specific position
        if (gamepad2.leftBumperWasReleased()) {
            //clockwise
            robot.getRotator().setDiskRotation(false);
            this.rotationSetting = AutoRotate;
        } else if (gamepad2.rightBumperWasReleased()) {
            //counter clock wise
            robot.getRotator().setDiskRotation(true); //FIXME:If we rotate left then right with the bumpers then the robot crashs
            this.rotationSetting = AutoRotate;
        }

        //manual rotation
        if (gamepad2.left_trigger != 0 || gamepad2.right_trigger != 0) {
            this.rotationSetting = ManualRotate;
        }

        //will align to the magnet
        if(gamepad2.right_stick_button && gamepad2.left_stick_button){
            this.rotationSetting = AutoAlignment;
        }
    }
}

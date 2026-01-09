package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.DriveForward;
import org.firstinspires.ftc.teamcode.util.commands.actions.RotateRobotCommand;
import org.firstinspires.ftc.teamcode.util.commands.actions.RotateRobotCommandPID;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.util.hardware.IntakeColorSensor;

import java.util.Arrays;

@Autonomous(name = "ShootSixArtifactsRed")
public class ShootSixArtifactsRed extends OpModeBase {

    private Command seq;

    private boolean is_done = false;


    @Override
    public void init(){
        super.init();
        final double shoot_power = 0.66;
        final double turn_first_balls = 160;
        this.seq = new SequentialCommandGroup(
                MoveAndShootThreeArtifacts.init(robot),
                new RotateRobotCommandPID(turn_first_balls, robot),
                new DriveForward(8, 1.0, robot),
                PickupThreeBalls.init(robot),
                new RotateRobotCommandPID(360-turn_first_balls, robot),
                new DriveForward(16, 1.0, robot),
                new ShootThreeArtifacts(robot, shoot_power)
                //new DriveForward(40, 1.0, robot)
        );
        this.seq.init();
    }

    @Override
    public void loop() {
        if (!seq.isDone() && !is_done){
            seq.run();
        }else{
            is_done = true;
            seq.shutdown();
        }

        robot.updateMotif();
        telemetry.addData("current motif", Arrays.toString(robot.getMotif()));
        telemetry.addData("current disk pos: ", robot.getRotator().getEncoderPosition());
        telemetry.addData("desired pos:", robot.getRotator().getMotor().getTargetPosition());
        telemetry.addData("position error", robot.getRotator().getMotor().getTargetPosition() - robot.getRotator().getEncoderPosition());
        telemetry.update();
    }
}

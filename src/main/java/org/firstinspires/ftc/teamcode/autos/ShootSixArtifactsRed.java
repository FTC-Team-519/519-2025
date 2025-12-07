package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.DriveForward;
import org.firstinspires.ftc.teamcode.util.commands.actions.RotateRobotCommand;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

@Autonomous(name = "ShootSixArtifactsRed")
public class ShootSixArtifactsRed extends OpModeBase {

    private Command seq;

    private boolean is_done = false;

    @Override
    public void init(){
        super.init();
        final double shoot_power = 0.52;
        final double turn_first_balls = 150;
        this.seq = new SequentialCommandGroup(
                MoveAndShootThreeArtifacts.init(robot),
                new RotateRobotCommand(turn_first_balls, false, 0.8, robot),
                new DriveForward(10, 1.0, robot),
                PickupThreeBalls.init(robot),
                new DriveForward(-10, 1.0, robot),
                new RotateRobotCommand(180-turn_first_balls, false, 0.8, robot),
                ShootThreeArtifacts.init(robot, shoot_power)
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

        telemetry.addData("current disk pos: ", robot.getRotator().getEncoderPosition());
        telemetry.addData("desired pos:", robot.getRotator().getMotor().getTargetPosition());
        telemetry.addData("position error", robot.getRotator().getMotor().getTargetPosition() - robot.getRotator().getEncoderPosition());
        telemetry.update();
    }
}

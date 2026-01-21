package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.DriveInDirection;
import org.firstinspires.ftc.teamcode.util.commands.actions.DriveSideways;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

import java.util.Arrays;

@Autonomous(name = "ShootThreeArtifactsRed")
public class ShootThreeArtifactsRed extends OpModeBase {

    private Command seq;

    private boolean is_done = false;

    @Override
    public void init(){
        super.init();
        final double shoot_power = 0.67;

        this.seq = new SequentialCommandGroup(
                MoveAndShootThreeArtifacts.init(robot),
                new DriveInDirection(36 * 6.0 / 5.0,  Math.PI, robot)
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
        robot.updateDiskPower();
        telemetry.addData("current motif", Arrays.toString(robot.getMotif()));
        telemetry.addData("current disk pos: ", robot.getRotator().getEncoderPosition());
        telemetry.addData("desired pos:", robot.getRotator().getMotor().getTargetPosition());
        telemetry.addData("position error", robot.getRotator().getMotor().getTargetPosition() - robot.getRotator().getEncoderPosition());
        telemetry.addData("at position", robot.getRotator().isAtPosition());
        telemetry.addData("at position", robot.getRotator().getMotor().getPower());
        telemetry.addData("disk power set with command", robot.isRotatorPowerSet());
        telemetry.addData("outtake velocity", robot.getOuttake().getLeftMotor().getVelocity());
        telemetry.update();
    }
}

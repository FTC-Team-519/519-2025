package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.*;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.util.hardware.Rotator;

@Autonomous(name = "ShootThreeArtifactsBlue")
public class ShootThreeArtifactsBlue extends OpModeBase {

    private Command seq;

    private boolean is_done = false;

    @Override
    public void init(){
        super.init();
        final double shoot_power = 0.67;

        this.seq = new SequentialCommandGroup(
                MoveAndShootThreeArtifacts.init(robot),

                new DriveForward(40, robot)
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

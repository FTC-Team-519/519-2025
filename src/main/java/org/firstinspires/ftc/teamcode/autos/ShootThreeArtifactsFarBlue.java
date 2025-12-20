package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.AlignRotator;
import org.firstinspires.ftc.teamcode.util.commands.actions.DriveForward;
import org.firstinspires.ftc.teamcode.util.commands.actions.RotateRobotCommand;
import org.firstinspires.ftc.teamcode.util.commands.actions.RotateRobotCommandPID;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

@Autonomous(name = "ShootThreeArtifactsFarBlue")
public class ShootThreeArtifactsFarBlue extends OpModeBase {

    private Command seq;

    private boolean is_done = false;

    @Override
    public void init() {
        super.init();
        final double shoot_power = 0.90; //FIXME: TUNE PLEASE

        this.seq = new SequentialCommandGroup(
                new DriveForward(12.0, 1.0, robot),
                new AlignRotator(robot),
                new RotateRobotCommand(Math.PI, true, 0.3, robot),
                new ShootThreeArtifacts(robot, shoot_power)
        );
        this.seq.init();
    }

    @Override
    public void loop() {
        if (!seq.isDone() && !is_done) {
            seq.run();
        } else {
            is_done = true;
            seq.shutdown();
        }
    }
}

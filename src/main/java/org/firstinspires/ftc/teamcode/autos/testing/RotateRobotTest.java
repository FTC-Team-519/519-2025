package org.firstinspires.ftc.teamcode.autos.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.actions.RotateRobotCommand;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

@Autonomous(name="RotateRobotTest", group="test")
public class RotateRobotTest extends OpModeBase {

    private SequentialCommandGroup seq;

    public void init() {
        super.init();

        this.seq = new SequentialCommandGroup(
                new RotateRobotCommand(90, true, 0.5, robot)
        );
    }
    @Override
    public void loop() {
        if (!seq.isDone()) {
            seq.run();
        }
    }
}

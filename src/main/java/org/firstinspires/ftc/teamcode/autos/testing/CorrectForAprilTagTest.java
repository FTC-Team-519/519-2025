package org.firstinspires.ftc.teamcode.autos.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.actions.CorrectForAprilTag;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

@Autonomous(name="CorrectForAprilTagTest", group="test")
public class CorrectForAprilTagTest extends OpModeBase {

    private SequentialCommandGroup seq;

    public void init() {
        super.init();

        this.seq = new SequentialCommandGroup(
                new CorrectForAprilTag(robot)
        );
    }
    @Override
    public void loop() {
        if (!seq.isDone()) {
            seq.run();
        }
    }
}

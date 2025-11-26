package org.firstinspires.ftc.teamcode.autos.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.actions.OuttakeCommand;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

@Autonomous(name="OuttakeTest", group="test")
public class OuttakeTest extends OpModeBase {

    private SequentialCommandGroup seq;

    public void init() {
        super.init();

        this.seq = new SequentialCommandGroup(
                new OuttakeCommand(robot,0.5)
        );
    }
    @Override
    public void loop() {
        if (!seq.isDone()) {
            seq.run();
        }
    }
}

package org.firstinspires.ftc.teamcode.autos.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.actions.TimerCommand;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

@Autonomous(name="TimerTest", group="test")
public class TimerTest extends OpModeBase {

    private SequentialCommandGroup seq;

    public void init() {
        super.init();

        this.seq = new SequentialCommandGroup(
                new TimerCommand(5)
        );
    }
    @Override
    public void loop() {
        if (!seq.isDone()) {
            seq.run();
        }
    }
}

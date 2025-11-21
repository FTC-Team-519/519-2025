package org.firstinspires.ftc.teamcode.autos.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.actions.DriveSideways;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

// Jonah was here
@Autonomous(name="DriveSidewaysTest", group = "test")
public class DriveSidewaysTest extends OpModeBase {

    private SequentialCommandGroup seq;

    @Override
    public void init(){
        super.init();

        this.seq = new SequentialCommandGroup(
                new DriveSideways( 12,robot)
        );
    }

    @Override
    public void loop() {
        if (!seq.isDone()){
            seq.run();
        }
    }
}

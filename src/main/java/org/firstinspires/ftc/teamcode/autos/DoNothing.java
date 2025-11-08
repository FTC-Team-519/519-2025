package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;


@Autonomous(name="Do Nothing")
public class DoNothing extends OpModeBase {

    private SequentialCommandGroup seq;

    @Override
    public void init(){
        super.init();

        this.seq = new SequentialCommandGroup();
    }

    @Override
    public void loop() {
        if (!seq.isDone()){
            seq.run();
        }
    }
}

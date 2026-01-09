package org.firstinspires.ftc.teamcode.autos.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.util.OpModeBase;
import org.firstinspires.ftc.teamcode.util.commands.Command;
import org.firstinspires.ftc.teamcode.util.commands.actions.DiskRotatePIDF;
import org.firstinspires.ftc.teamcode.util.commands.command_groups.SequentialCommandGroup;

@Autonomous(name="RotateDiskPIDTest")
public class RotateDiskPIDTest extends OpModeBase {

    private Command seq;

    private boolean is_done = false;

    @Override
    public void init(){
        super.init();

        this.seq = new SequentialCommandGroup(new DiskRotatePIDF(robot, 3));
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
    }
}
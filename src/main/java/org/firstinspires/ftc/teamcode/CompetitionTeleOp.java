package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.util.OpModeBase;

@TeleOp(name = "Competition TeleOp")
public class CompetitionTeleOp extends OpModeBase {
    private boolean driving_field_centric = false;

    @Override
    public void init() {
        super.init();

    }

    @Override
    public void loop() {
        if (!driving_field_centric) {
            driving(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }

    }

    private void driving(double x, double y, double rot){
        double lf_power = y + x + rot;
        double rf_power = y - x - rot;
        double lb_power = y - x + rot;
        double rb_power = y + x - rot;

        double scale = Math.abs(y) + Math.abs(x) + Math.abs(rot);

        lf_power /= scale;
        rf_power /= scale;
        lb_power /= scale;
        rb_power /= scale;

        robot.setLeftFrontPower(lf_power);
        robot.setRightFrontPower(rf_power);
        robot.setLeftBackPower(lb_power);
        robot.setRightBackPower(rb_power);
    }
}

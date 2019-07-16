package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

public class RegisterOpModes {

    @OpModeRegistrar
    public static void registerMyOpModes(OpModeManager manager) {
        manager.register("TeleOpMode", TeleOpMode_Iterative.class);
        manager.register("LinearAutonomous", LinearAutonomous.class);
    }
}

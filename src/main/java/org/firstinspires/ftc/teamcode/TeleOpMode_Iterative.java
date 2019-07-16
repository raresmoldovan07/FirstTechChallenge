package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.LinearAutonomous.hsvValues;

@TeleOp(name = "Template: Iterative OpMode", group = "PushBot")
public class TeleOpMode_Iterative extends OpMode {

    private double servo_position1;
    private double servo_position2;
    private double liftSpeed = 0.0;

    private ElapsedTime runtime = new ElapsedTime();
    private HardwarePapiubot robot = new HardwarePapiubot();

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.color.enableLed(false);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {

        double left;
        double right;
        double max;

        if (gamepad1.y) {
            servo_position1 = 0;
            servo_position2 = 1;
            robot.servoLeft.setPosition(servo_position1);
            robot.servoRight.setPosition(servo_position2);
        }

        if (gamepad1.b) {
            servo_position1 = 1;
            servo_position2 = 0;
            robot.servoLeft.setPosition(servo_position1);
            robot.servoRight.setPosition(servo_position2);
        }

        if (gamepad1.x) {
            robot.liftMotor.setPower(-0.2);
        } else if (gamepad1.a) {
            liftSpeed += 0.01;
            if (liftSpeed > 0.4) liftSpeed = 0.4;
            robot.liftMotor.setPower(liftSpeed);
        } else {
            liftSpeed -= 0.03;
            if (liftSpeed < 0.0) liftSpeed = 0.0;
            robot.liftMotor.setPower(liftSpeed);
        }

        if (gamepad1.right_bumper) {
            robot.armMotor.setPower(-0.2);
        } else if (gamepad1.left_bumper) {
            robot.armMotor.setPower(0.2);
        } else {
            robot.armMotor.setPower(0);
        }

        left = -gamepad1.left_stick_y + gamepad1.left_stick_x;
        right = -gamepad1.left_stick_y - gamepad1.left_stick_x;

        max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1.0) {
            left /= max;
            right /= max;
        }

        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);
        Color.RGBToHSV(robot.color.red() * 8, robot.color.green() * 8, robot.color.blue() * 8, hsvValues);

        telemetry.addData("Arm ", robot.armMotor.getPower());
        telemetry.addData("Red", robot.color.red());
        telemetry.addData("Blue", robot.color.blue());
        telemetry.addData("Left servo: ", "%.2f", servo_position1);
        telemetry.addData("Right servo: ", "%.2f", servo_position2);
        telemetry.update();
    }
}

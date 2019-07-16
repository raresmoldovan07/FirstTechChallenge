package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "LinearAutonomous", group = "Pushbot")
public class LinearAutonomous extends LinearOpMode {

    private HardwarePapiubot robot = new HardwarePapiubot();
    private ElapsedTime runtime = new ElapsedTime();

    private static boolean ARM_UP = false;
    private static final double CENTER_DISTANCE = 75;
    private static final double TURN_DISTANCE = 12.5;
    private static final double COUNTS_PER_MOTOR_REV = 1440;
    private static final double WHEEL_DIAMETER = 10.16;
    private static final double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV) / (WHEEL_DIAMETER * 3.1415);
    static final float[] hsvValues = {0F, 0F, 0F};

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("Robot initialized. ", "Press start.");
        telemetry.update();

        if (isStopRequested()) return;

        waitForStart();

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        catchGlyph();

        takeBall();

        encoderDrive(0.6, CENTER_DISTANCE, CENTER_DISTANCE);
        encoderDrive(0.5, TURN_DISTANCE, -TURN_DISTANCE);
        encoderDrive(0.5, -20, -20);
        encoderDrive(0.5, 14, -14);

        sleep(1000);

        encoderDrive(0.3, 20, 20);

        openGlyph();

        secondChance();

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    private void secondChance() {
        encoderDrive(0.5, -20, -20);

        robot.servoLeft.setPosition(0);
        robot.servoRight.setPosition(1);

        runtime.reset();
        robot.liftMotor.setPower(-0.15);
        while (runtime.seconds() < 0.25 && !isStopRequested()) ;
        robot.liftMotor.setPower(0);
        encoderDrive(0.5, 35, 35);
    }

    private void catchGlyph() {
        runtime.reset();
        while (runtime.seconds() < 2.0 && !isStopRequested()) {
            robot.servoLeft.setPosition(0);
            robot.servoRight.setPosition(1);
        }

        runtime.reset();
        robot.liftMotor.setPower(0.2);
        while (runtime.seconds() <= 0.5 && !isStopRequested()) ;
        robot.liftMotor.setPower(0);
    }

    private void encoderDrive(double speed, double leftCM, double rightCM) {
        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int newLeftTarget;
        int newRightTarget;

        if (opModeIsActive()) {

            newLeftTarget = robot.leftMotor.getCurrentPosition() + (int) (leftCM * COUNTS_PER_CM);
            newRightTarget = robot.rightMotor.getCurrentPosition() + (int) (rightCM * COUNTS_PER_CM);

            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);

            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));

            while (opModeIsActive() && (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {
                if (ARM_UP) {
                    if (robot.leftMotor.getCurrentPosition() > 200 && robot.rightMotor.getCurrentPosition() > 200) {
                        runtime.reset();
                        robot.armMotor.setPower(0.1);
                        while (runtime.seconds() < 1 && !isStopRequested()) ;
                        robot.armMotor.setPower(0);
                        ARM_UP = false;
                    }
                }
                telemetry.addData("LEFT ", " %7d / %7d", robot.leftMotor.getCurrentPosition(), newLeftTarget);
                telemetry.addData("RIGHT ", " %7d / %7d", robot.rightMotor.getCurrentPosition(), newRightTarget);
                telemetry.update();
            }

            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);
        }
    }

    private void takeBall() {
        if (!opModeIsActive())
            return;

        robot.color.enableLed(true);
        runtime.reset();
        robot.armMotor.setPower(-0.1);
        while (runtime.seconds() < 0.5 && !isStopRequested()) ;
        robot.armMotor.setPower(0);

        Color.RGBToHSV(robot.color.red() * 8, robot.color.green() * 8, robot.color.blue() * 8, hsvValues);
        runtime.reset();
        while (runtime.seconds() < 2 && !isStopRequested()) {
            telemetry.addData("Red value ", robot.color.red());
            telemetry.addData("Blue value", robot.color.blue());
            telemetry.update();
        }
        if (robot.color.blue() < robot.color.red()) {
            encoderDrive(0.3, -8, -8);
            runtime.reset();

            robot.armMotor.setPower(0.1);
            while (runtime.seconds() < 0.8 && !isStopRequested()) ;
            robot.armMotor.setPower(0);
            sleep(1000);
            encoderDrive(0.3, 8, 8);
        } else {
            ARM_UP = true;
        }
    }

    private void openGlyph() {
        runtime.reset();
        while (runtime.seconds() < 0.5 && !isStopRequested()) {
            robot.servoLeft.setPosition(1);
            robot.servoRight.setPosition(0);
        }
    }
}

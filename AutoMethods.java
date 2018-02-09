package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by Phoebe Taylor on 1/26/2018.
 *
 * This class is a compilation of autonomous methods that we have done thus far in the season. It is
 * not a teleop or autonomous code on its own, these methods are only meant to be referenced in
 * autonomous programs so you don't have to copy/paste the methods into every program you write.
 * It extends LinearOpMode only because it needs to so that the code doesn't give back a ton of errors.
 * This code is written so that you can simply reference it and use them.
 */

public class AutoMethods extends LinearOpMode{

    @Override public void runOpMode(){

    }
    //all the methods reference from my hardware class (MecanumHardware), so I'll create and object of it
    //below called 'robot'
    MecanumHardware robot = new MecanumHardware();

    //declare an object of the ElapsedTime class to allow you to calculate how long you've been driving
    private ElapsedTime runtime = new ElapsedTime();

    /*Variables you will need to calculate the circumference of the wheel and how long it takes to
    spin the wheel once. 'final' means that it cannot be changed anywhere else in the program,
    which denotes that this variable is permanent (you can't change the hardware dimensions, so the
    program shouldn't either) These will later be used inside the encoderDrive method. If your motors
    are not NevRest 60s, you will need to change the counts_per_motor_rev for your motor and then
    the wheel diameter depending on your drive train setup*/
    static public final double COUNTS_PER_MOTOR_REV = 1680;
    static public final double WHEEL_DIAMETER_INCHES = 4.0;
    static public final double COUNTS_PER_INCH =  (COUNTS_PER_MOTOR_REV) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    static public final double DRIVE_SPEED = 0.6;
    static public final double TURN_SPEED = 0.5;

    /*the following variables are arguments for the encoderDrive method, each for a different action.
    RIGHT variables denote values that are applicable for the right wheels, LEFT for left wheels.
    Rotating on the Z axis means all wheels are spinning in the same direction, so only one TURN variable is necessary.
    We will not be going diagonally in this code, so you will only need to use one axis
    (just y axis for forward, just z for rotation, just x for laterally) */
    /**For forward/backward motion with our wheel configuration, the right side needs to be negative, and the right
     needs to be positive (opposite of the driver class's logic because we reverse Y in the driver but not here)
     All measurements are in inches
     */
    //s1 will be moving forward (this is just a sample for what your variables should look like in your
    //program, I will not be implementing it here. The first two variables set stage one to go foward
    //25 inches, the third variable will rotate 13 inches to the right (about 90 deg with our setup)
    //static final double s1_RIGHT = -25;
    //static final double S1_LEFT = 25;

    //All Z axis values are positive, so you only need one variable for all the wheels
    //s2 is to turn to the right
    //static final double S2_TURN = 13;
    /**the following line is an example of what your call statement should look like for the encoderDrive method */
    //encoderDrive(DRIVE_SPEED, S1_LEFT, s1_RIGHT, 10.0, "forward");  // S1: Forward 25 inches with 10 Sec timeout




    /* the encoderDrive method takes in 5 arguments: speed you want it to drive, leftInches is the
    number of inches that you want the left side wheels to move forward, rightInches is the same but
    for the right side wheels, timoutS is how long the loop should run as a safety measure against infinite loops
    (I usually put 10, bc at that point it really should be over), and msg is just for when you
    want telemetry to print from the method for troubleshooting.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS, String msg) {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            telemetry.addData("very beginning", "encoder");
            telemetry.update();

            // Determine new target position, and pass to motor controller
            newFrontLeftTarget = robot.frontLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newFrontRightTarget = robot.frontRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newBackLeftTarget = robot.backLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newBackRightTarget = robot.backRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

            telemetry.addLine("we out here");
            telemetry.update();

            robot.frontLeft.setTargetPosition(newFrontLeftTarget);
            robot.frontRight.setTargetPosition(newFrontRightTarget);
            robot.backLeft.setTargetPosition(newBackLeftTarget);
            robot.backRight.setTargetPosition(newBackRightTarget);



            // Turn On RUN_TO_POSITION
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);



            // reset the timeout time and start motion.
            runtime.reset();
            double power = Math.abs(speed);

            robot.frontLeft.setPower(power);
            robot.backLeft.setPower(power);
            robot.frontRight.setPower(power);
            robot.backRight.setPower(power);

            telemetry.addData("Current Stage: ", msg);
            telemetry.update();


            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.frontLeft.isBusy() && robot.frontRight.isBusy())) {

                // Display it for the driver.
                //telemetry.addData("Path1",  "Running to %7d :%7d", newFrontLeftTarget,  newFrontRightTarget);
                //telemetry.addData("Path2",  "Running at %7d :%7d",
                //        robot.frontLeft.getCurrentPosition(),
                //        robot.frontRight.getCurrentPosition());
                //telemetry.addData(msg, "autonomous");
                int whereFrontRightThinks = robot.frontRight.getCurrentPosition();
                int whereFrontLeftThinks = robot.frontLeft.getCurrentPosition();
                int whereBackRightThinks = robot.backRight.getCurrentPosition();
                int whereBackLeftThinks = robot.backLeft.getCurrentPosition();

                /*telemetry.addData("front right position: ", whereFrontRightThinks);
                telemetry.addData("front left position: ", whereFrontLeftThinks);
                telemetry.addData("back right position: ", whereBackRightThinks);
                telemetry.addData("back left position: ", whereBackLeftThinks);*/
                telemetry.addData("current stage: ", msg);
                telemetry.update();
            }

            // Stop all motion;
            robot.frontLeft.setPower(0);
            robot.frontRight.setPower(0);
            robot.backRight.setPower(0);
            robot.backLeft.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



            //  sleep(250);   // optional pause after each move
        }
    }

    /* this method takes 3 variables: PositionRight is the position I am sending to the right arm of
    our vertical lift (Servo), PositionLeft is the same but for the left arm, and a msg for debugging
     */
    public void glyphPlacement (double PositionRight, double PositionLeft, String msg){
            robot.left_grab.setPosition(PositionLeft);
            robot.right_grab.setPosition(PositionRight);


        telemetry.addData(msg,"something");
        telemetry.update();
    }

    /* the following method only really exists because it needs to set up the sensor, read in and process
    the data from it, and THEN actually read the color of the glyph. The code responsible for actually
    deciding what color it's seeing and what to do with what color is relatively small in and of itself
     */
    public void colorSense (String allianceColor, String msg) {
        telemetry.addData("color sense", "0");
        telemetry.update();

        //put the arm down (1 is up), 0.43 is how far down i want it to get
        double maxArm = 0.95;
        telemetry.addData("color sense", "1");
        telemetry.update();

        while (maxArm > 0.5) {
            maxArm -= 0.002;
            robot.jewel_arm.setPosition(maxArm);
        }
        telemetry.addData("color sense", "2");
        telemetry.update();

        // values is a reference to the hsvValues array.
        float[] hsvValues = new float[3];
        final float values[] = hsvValues;

        //this just makes sure the light on the sensor is on... it should be by default but just in case
        if (robot.colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)robot.colorSensor).enableLight(true);
        }


        runtime.reset();
        while (runtime.seconds() < 5) {
            //this should read in the color values in RGB from the sensor
            NormalizedRGBA colors = robot.colorSensor.getNormalizedColors();

            telemetry.addData("Should be down", "arm");
            telemetry.addLine()
                    .addData("Hue", "%.2f", hsvValues[0]);
            telemetry.update();

            float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
            colors.red /= max;
            colors.green /= max;
            colors.blue /= max;
            int color = colors.toColor();

            /*change the color values from RGB to HSV, because HSV is a more reliable spectrum of color data.
            If you aren't sure why, google it for a full description, but basically HSV is Hue, Saturation,
            and Vibrancy. The Hue is the "color" you're seeing, and the other two values essentially measure
            the light in the room. This is good because if you are reading in values in two rooms with different
            lighting, the RGB values will be drastically different, but the Hue value will remain similar
            because HSV can correct for different lighting. This means that, when we later decide what color
            the sensor is looking at, we will use just the H value.
            */
            Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsvValues);
        }

        /* the following code will actually execute and move the robot. First it will put the arm down,
        then read the color, and based upon the color it reads and what alliance color it is given,
        it will knock one of the jewels off and then pull the arm back up. Then it will rotate back so
        as not to disrupt the rest of the code for placing the glyph. After that, it will exit
        this method. I will use the encoderDrive method to turn the robot the appropriate way, because
        there's no point in rewriting all that code in here when it already exists in that method
         */

        //sleep(1000);

        telemetry.addData("Should be down", "arm");
        telemetry.addLine()
                .addData("Hue", "%.2f", hsvValues[0]);
        telemetry.update();

        //I use sleeps to troubleshoot so I can stop all the movement for a time and see what the robot is doing
        //sleep(3000);

        if (allianceColor == "red") {
            //1-3 is probably red (I'm making the range really big just in case, so I'll use 50 instead),
            //so if this is true, then the robot should turn right to
            //knock the opposite jewel off (blue jewel). Positive values for the direction of movement
            //will make the robot turn right, and negative values will make it go left
            if (hsvValues[0] < 60) {
                encoderDrive(DRIVE_SPEED, 2, 2, 10.0, "knock jewel off right"); //turns 2 inches right
                robot.jewel_arm.setPosition(0.94);
                encoderDrive(DRIVE_SPEED, -2, -2, 10.0, "reset position"); //turns 2 inches right

            } else {
                encoderDrive(DRIVE_SPEED, -2, -2, 10.0, "knock jewel off left"); //turns 2 inches left
                robot.jewel_arm.setPosition(0.94);
                encoderDrive(DRIVE_SPEED, 2, 2, 10.0, "reset position"); //turns 2 inches right
            }
        }

        if (allianceColor == "blue") {
            //if the first loop (greater than 60) is true, then the sensor is looking at the blue jewel
            //while in the blue alliance, so it should rotate right to knock off the red jewel
            if (hsvValues[0] > 60) {
                encoderDrive(DRIVE_SPEED, 2, 2, 10.0, "knock jewel off right"); //turns 2 inches left
                robot.jewel_arm.setPosition(0.94);
                encoderDrive(DRIVE_SPEED, -2, -2, 10.0, "reset position"); //turns 2 inches right

            } else {
                encoderDrive(DRIVE_SPEED, -2, -2, 10.0, "knock jewel off left"); //turns 2 inches right
                robot.jewel_arm.setPosition(0.94);
                encoderDrive(DRIVE_SPEED, 2, 2, 10.0, "reset position"); //turns 2 inches right
            }
        }

    }




}

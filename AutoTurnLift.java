package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Phoebe Taylor on 12/4/2017.
 * This code is for parking in the triangle zones with a mecanum drive train using encoders. Hopefully,
 * it will also put one glyph in the cryptobox
 */

@Autonomous(name = "AutoRedTurnPark", group = "Autonomous Mecanum")
public class AutoRedTurnLift extends LinearOpMode{
    //make object of mecanum hardware class
    MecanumHardware robot = new MecanumHardware();
    //declare an object of the ElapsedTime class to allow you to calculate how long you've been driving
    private ElapsedTime runtime = new ElapsedTime();

    //Variables you will need to calculate the circumference of the wheel and how long it takes to
    //spin the wheel once. 'final' means that it cannot be changed anywhere else in the program,
    //which denotes that this variable is permanent (you can't change the hardware dimensions, so the
    //program shouldn't either)
    static final double COUNTS_PER_MOTOR_REV = 1680;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH =  (COUNTS_PER_MOTOR_REV) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;


    /*the following variables are arguments for the encoderDrive method, each for a different action.
    RIGHT variables denote values that are applicable for the right wheels, LEFT for left wheels.
    Rotating on the Z axis means all wheels are spinning in the same direction, so only one TURN variable is necessary.
    We will not be going diagonally in this code, so you will only need to use one axis
    (just y axis for forward, just z for rotation, just x for laterally) */

    /**For forward/backward motion with our wheel configuration, the right side needs to be negative, and the right
     needs to be positive (opposite of the driver class's logic because we reverse Y in the driver but not here)
     All measurements are in inches
     */
    //s1 will be moving forward
    static final double s1_RIGHT = -25;
    static final double S1_LEFT = 25;

    //All Z axis values are positive, so you only need one variable for all the wheels
    //s2 is to turn to the right
    static final double S2_TURN = 13;

    //These values will go to the grabbers
    static final double DROP_RIGHT = 1;
    static final double DROP_LEFT = 0;

    //s3 will be moving forward into the cryptobox
    static final double s3_RIGHT = -10;
    static final double s3_LEFT = 10;

    //s4 will move back just a little bit so that the glyph is not in contact with the robot
    static final double s4_RIGHT = 4;
    static final double s4_LEFT = -4;

    @Override
    public void runOpMode(){
        //start the init method in the hardware class
        robot.init(hardwareMap);

        //send some telemetry messages to tell you that it's running
        telemetry.addData("IT'S RUNNING WTF", "autonomous");
        telemetry.update();

        //set the grabbers to be closed at start so we can put the glyph in it
        robot.left_grab.setPosition(0.52);
        robot.right_grab.setPosition(0.48);

        //tell the encoders to reset for a hot sec
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        idle();

        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        //the LEFT and RIGHT in the variables denotes the wheel or side to which it is assigned, not the direction it aims to move the robot
        encoderDrive(DRIVE_SPEED, S1_LEFT, s1_RIGHT, 10.0, "forward");  // S1: Forward 25 inches with 5 Sec timeout
        encoderDrive(TURN_SPEED, S2_TURN, S2_TURN, 10.0, "turn");  // S2: Turn Right 9 inches with 4 Sec timeout

        glyphPlacement(DROP_RIGHT, DROP_LEFT, 0, "open grabber"); //open grabber to drop the glyph

        encoderDrive(DRIVE_SPEED, s3_LEFT, s3_RIGHT, 10.0, "forward push");  // S3: Forward 10 inches with 10 Sec timeout
        encoderDrive(DRIVE_SPEED, s4_LEFT, s4_RIGHT, 10.0, "Retreat");  // S3: Forward 10 inches with 10 Sec timeout


    }

    /*
 *  Method to perfmorm a relative move, based on encoder counts.
 *  Encoders are not reset as the move is based on the current position.
 *  Move will stop if any of three conditions occur:
 *  1) Move gets to the desired position
 *  2) Move runs out of time
 *  3) Driver stops the opmode running.
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

            // Determine new target position, and pass to motor controller
            newFrontLeftTarget = robot.frontLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newFrontRightTarget = robot.frontRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newBackLeftTarget = robot.backLeft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newBackRightTarget = robot.backRight.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

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

                telemetry.addData("front right position: ", whereFrontRightThinks);
                telemetry.addData("front left position: ", whereFrontLeftThinks);
                telemetry.addData("back right position: ", whereBackRightThinks);
                telemetry.addData("back left position: ", whereBackLeftThinks);

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

    public void glyphPlacement (double PositionRight, double PositionLeft, int control, String msg){
        //control is set to 0 when you want the lift to open
        if (control == 0){
            robot.left_grab.setPosition(PositionLeft);
            robot.right_grab.setPosition(PositionRight);
        }

        //lift is set to 1 if you want the lift to open all the way
        if (control == 1){
            robot.left_grab.setPosition(PositionLeft);
            robot.right_grab.setPosition(PositionRight);
        }

        telemetry.addData(msg,"something");
        telemetry.update();
    }

}


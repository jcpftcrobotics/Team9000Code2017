/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 *
 * 'Test 160 This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="A_T_Jewels_1", group="Linear Opmode")
//@Disabled
public class A_T_Jewels_1 extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private Servo servo1 = null;
    NormalizedColorSensor colorSensor;
    /** The relativeLayout field is used to aid in providing interesting visual feedback
     * in this sample application; you probably *don't* need something analogous when you
     * use a color sensor on your robot */
    View relativeLayout;


    @Override
    public void runOpMode() {

        // Get a reference to the RelativeLayout so we can later change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        float[] hsvValues = new float[3];
        final float values[] = hsvValues;
        float move_forward=0;
        double drive = 0;
        double turn = 0;

        // Get a reference to our sensor object.
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color");

        // If possible, turn the light on in the beginning (it might already be on anyway,
        // we just make sure it is if we can).
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)colorSensor).enableLight(true);
        }

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        servo1=hardwareMap.get(Servo.class,"servo");
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // servo down by
        double time1=1.0;
        // Average Colors until
        double time2=5.0;
        // Move forward or backward until
        double time3=5.5;
        // Raise arm
        double time4=6.5;
        //if backward move forward again
        double time5=7.0;


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Setup a variable for each drive wheel to save power level for telemetry

            telemetry.addData("Status1", "Run Time: " + runtime.toString());
            telemetry.update();
            // Step 1: Put servo down
            while (runtime.time() < time1) {
                servo1.setPosition(1);
                telemetry.addData("Status1", "Run Time: " + runtime.toString());
                telemetry.addData("Servo1", servo1.getPosition());
                telemetry.update();
            }
            /* STep 2: Read color for 1 sec, accumulate total red and blue; */

            // Read the sensor
            while ((runtime.time() >= time1) & (runtime.time()< time2)) {
                NormalizedRGBA colors = colorSensor.getNormalizedColors();

                /** Use telemetry to display feedback on the driver station. We show the conversion
                 * of the colors to hue, saturation and value, and display the the normalized values
                 * as returned from the sensor.
                 * @see <a href="http://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html">HSV</a>*/

                Color.colorToHSV(colors.toColor(), hsvValues);
                telemetry.addLine()
                        .addData("H", "%.3f", hsvValues[0])
                        .addData("S", "%.3f", hsvValues[1])
                        .addData("V", "%.3f", hsvValues[2]);
                telemetry.addLine()
                        .addData("a1", "%.3f", colors.alpha)
                        .addData("r1", "%.3f", colors.red)
                        .addData("g1", "%.3f", colors.green)
                        .addData("b1", "%.3f", colors.blue);

                /** We also display a conversion of the colors to an equivalent Android color integer.
                 * @see Color */
                int color = colors.toColor();
                telemetry.addLine("raw Android color: ")
                        .addData("a2", "%02x", Color.alpha(color))
                        .addData("r2", "%02x", Color.red(color))
                        .addData("g2", "%02x", Color.green(color))
                        .addData("b2", "%02x", Color.blue(color));

                // Balance the colors. The values returned by getColors() are normalized relative to the
                // maximum possible values that the sensor can measure. For example, a sensor might in a
                // particular configuration be able to internally measure color intensity in a range of
                // [0, 10240]. In such a case, the values returned by getColors() will be divided by 10240
                // so as to return a value it the range [0,1]. However, and this is the point, even so, the
                // values we see here may not get close to 1.0 in, e.g., low light conditions where the
                // sensor measurements don't approach their maximum limit. In such situations, the *relative*
                // intensities of the colors are likely what is most interesting. Here, for example, we boost
                // the signal on the colors while maintaining their relative balance so as to give more
                // vibrant visual feedback on the robot controller visual display.
                float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
                colors.red /= max;
                colors.green /= max;
                colors.blue /= max;
                color = colors.toColor();

                telemetry.addLine("normalized color:  ")
                        .addData("a3", "%02x", Color.alpha(color))
                        .addData("r3", "%02x", Color.red(color))
                        .addData("g3", "%02x", Color.green(color))
                        .addData("b3", "%02x", Color.blue(color));

                // take an average

                // convert the RGB values to HSV values.
                Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsvValues);

                // change the background color to match the color detected by the RGB sensor.
                // pass a reference to the hue, saturation, and value array as an argument
                // to the HSVToColor method.
                // Figure this out - supposed to run at end

                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setBackgroundColor(Color.WHITE);
                    }
                });
                if (colors.red > colors.blue) {move_forward=1;} else {move_forward=0;}
                telemetry.addData("Move", move_forward);
                telemetry.addLine()
                        .addData("a1", "%.3f", colors.alpha)
                        .addData("r1", "%.3f", colors.red)
                        .addData("g1", "%.3f", colors.green)
                        .addData("b1", "%.3f", colors.blue);
                telemetry.update();
            }


            //drive forward or backward
            while ((runtime.time() >= time2) & (runtime.time()< time3)) {
                servo1.setPosition(1);
                if (move_forward > 0.5) {
                    drive = 0.5;
                    turn = 0;
                }
                else
                {
                    drive = -0.5;
                    turn = 0;

                }
                double leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
                double rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

                leftDrive.setPower(leftPower);
                rightDrive.setPower(rightPower);

                telemetry.addData("Status1", "Run Time: " + runtime.toString());
                telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
                telemetry.addData("Servo1", servo1.getPosition());
                telemetry.addData("Move", move_forward);
                telemetry.update();
            }

            //Raise Servo
            while ((runtime.time() >= time3) & (runtime.time()< time4)) {
                drive = 0;
                turn = 0;
                double leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
                double rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

                leftDrive.setPower(leftPower);
                rightDrive.setPower(rightPower);

                servo1.setPosition(0);

                telemetry.addData("Status1", "Run Time: " + runtime.toString());
                telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
                telemetry.addData("Servo1", servo1.getPosition());
                telemetry.update();
            }
            while ((runtime.time() >= time4) & (runtime.time()< time5)) {
                if (move_forward < 0.5) {
                    drive = 0.5;
                    turn = 0;
                }
                else
                {
                    drive = 0;
                    turn = 0;

                }
                double leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
                double rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
                leftDrive.setPower(leftPower);
                rightDrive.setPower(rightPower);
                telemetry.addData("Status1", "Run Time: " + runtime.toString());
                telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
                telemetry.addData("Servo1", servo1.getPosition());
                telemetry.update();
            }
            while ((runtime.time() >= time5) & (runtime.time()< 30)) {
                servo1.setPosition(0);
                drive = 0.0;
                turn = 0;
                double leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
                double rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
                leftDrive.setPower(leftPower);
                rightDrive.setPower(rightPower);
                telemetry.addData("Status1", "Run Time: " + runtime.toString());
                telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
                telemetry.addData("Servo1", servo1.getPosition());
                telemetry.update();
            }

            drive = 0;
            turn  = 0;
            double leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            double rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}


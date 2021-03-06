package org.usfirst.frc.team5212.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.cscore.UsbCamera;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * Created by explosivecoin on 2/20/2017.
 */
public class Robot extends IterativeRobot {
    CANTalon rightWheelF = new CANTalon(7);
    CANTalon leftWheelF = new CANTalon(6);
    CANTalon rightWheelB = new CANTalon(4);
    CANTalon leftWheelB = new CANTalon(3);
    CANTalon feed = new CANTalon(5);
    CANTalon flyWheel = new CANTalon(1);
    CANTalon winch = new CANTalon(2);
    CANTalon winch2 = new CANTalon(37);
    RobotDrive myRobot = new RobotDrive(rightWheelF, rightWheelB, leftWheelF, leftWheelB);
    Joystick stick = new Joystick(0);
    Timer timer = new Timer();
    AnalogInput ultraSonic1 = new AnalogInput(0); // This is the front one
    AnalogInput ultraSonic2 = new AnalogInput(1); // This is the back one
    int autoTimeToDrive = 2;
    long timeInAutonomous = 0;
    long timeInAutonomousStart = 0;
    boolean reversed = true;
    boolean shouldUseCameraServer = false;
    CameraServer cameraServer = CameraServer.getInstance();
    int winginIt = 0;
    Spark conveyor = new Spark(0);
    Spark eaterOfSouls = new Spark(1);//--------------Need to check
    NetworkTable diningTable;
    UsbCamera cammy = new UsbCamera("cam0", 0);
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */

    public void robotInit() {

        System.out.println("initializing");

        //Begin capturing images to send to dashboard
        cameraServer.startAutomaticCapture(cammy);

        //Set to server mode
        NetworkTable.setServerMode();

        //Initialize All NetworkTables
        NetworkTable.initialize();

        //Initializes network tables as well
        diningTable = NetworkTable.getTable("Yo");


        //FORTESTING_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+
        flyWheel.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Absolute);
        flyWheel.reverseSensor(true);
        flyWheel.reverseOutput(true);
        flyWheel.configNominalOutputVoltage(+0.0f, -0.0f);
        flyWheel.configPeakOutputVoltage(+12.0f, -12.0f);
        flyWheel.setProfile(0);
        flyWheel.setF(0.0296);
        flyWheel.setP(0.238);
        flyWheel.setI(0);
        flyWheel.setD(0);
        //FORTESTING_+_+_+_+_+_+_+_++++____+_+_+__+_++++____+_+__+_+++_____+__+_+
    }

    /**
     * This function is run once each time the robot enters autonomous mode
     */

    public void autonomousInit() {
        timeInAutonomousStart = System.currentTimeMillis();
        timer.reset();
        timer.start();
    }

    /**
     * This function is called periodically during autonomous
     * Called when packet is received from driver station (about once every 20ms)
     */

    public void autonomousPeriodic() {

        //Calculate the current number of Millis that we have been in Autonomous
        timeInAutonomous = System.currentTimeMillis() - timeInAutonomousStart;
        // Drive autoTimeToDrive number of seconds
        if(timeInAutonomous < autoTimeToDrive*1000) {
            //Don't try to access Camera if it shouldn't be used
            if (shouldUseCameraServer)
                lineUp();
            myRobot.tankDrive(1, 1);
        }
    }

    /**
     * This function is called once each time the robot enters tele-operated
     * mode
     */

    public void teleopInit() {
        System.out.println("called teleopInit");
        //Don't try to access Camera if it shouldn't be used
        if(shouldUseCameraServer)
            cammy.setFPS(15);
    }

    /**
     * This function is called periodically during operator control
     */

    public void teleopPeriodic() {
        //System.out.println(rightWheelF.getEncPosition());
        //System.out.println(rightWheelB.getEncPosition());
        //System.out.println(leftWheelF.getEncPosition());
        System.out.println("spd: " + flyWheel.getSpeed());

        //Don't try to access Camera if it shouldn't be used
        if(shouldUseCameraServer)
            System.out.println(diningTable.getNumber("cameraServer", -37));
        else
            System.out.println("Camera Server has been disabled");

        //Converting user input to motor controls
        if (stick.getRawButton(2)) {
            myRobot.tankDrive(-1, 1);
            Timer.delay(.1);
            myRobot.tankDrive(1, -1);
            Timer.delay(.175);
            myRobot.tankDrive(1, 1);
            Timer.delay(.05);
        } else if(!reversed) {
            myRobot.tankDrive(stick.getRawAxis(5) * .9, stick.getRawAxis(1));
        } else {
            myRobot.tankDrive(-stick.getRawAxis(1), -stick.getRawAxis(5) * .9);
        }

        double dist = 512 * (ultraSonic1.getVoltage() / 5);
        double dist2 = 512 * (ultraSonic2.getVoltage() / 5);

//		System.out.print("dist: ");
//		System.out.println(dist);
//		System.out.print("volts: ");
//		System.out.println(range.getVoltage());

        if(stick.getRawButton(4)) {
            winch.set(-1);
            winch2.set(-1);
        } else {
            winch.set(0);
            winch2.set(0);
        }

        if (stick.getRawButton(1)) {
            feed.set(-.75);
            conveyor.set(-.70);
        } else {
            feed.set(0);
            conveyor.set(0);
        }

        //Things to cameraServer:
        //Camera



		/*if (stick.getRawButton(2)) {
			flyWheel.setControlMode(CANTalon.TalonControlMode.PercentVbus.value);
			flyWheel.set(1);
		} else*/ if (stick.getRawButton(3)) {
            flyWheel.setControlMode(CANTalon.TalonControlMode.Speed.value);
            flyWheel.set(-2400); //Delta pos / 10 ms
		/*	if (flyWheel.getSpeed() < -2250) {
				feed.set(-.75);
				conveyor.set(-.70);
			}*/
        }
        else {
            flyWheel.set(0);
        }


		/*
		if(stick.getRawButton(3)) {
			cervantes.set(1);
		} else if (stick.getRawButton(4)) {
			cervantes.set(0);
		}
		*/
        if(stick.getRawButton(5)) {
            reversed = false;
        } else if(stick.getRawButton(6)) {
            reversed = true;
        }



        if (stick.getRawAxis(3) > .5) {
            eaterOfSouls.set(1);
        } else {
            eaterOfSouls.set(0);
        }

        if (stick.getRawAxis(2) > .5) {
            lineUp();
        }



    }

    void lineUp() {
        //Don't try to access Camera if it shouldn't be used
        if (shouldUseCameraServer)
            while (diningTable.getNumber("cameraServer", -37) > 0 && (diningTable.getNumber("cameraServer", -37) > 250 || diningTable.getNumber("cameraServer", -37) < 150 )) {
                //myRobot.tankDrive(-.7,-.7); // forward
                if(diningTable.getNumber("cameraServer", -37) > 250) {
                    myRobot.tankDrive(0,-.5);
                } else {
                    myRobot.tankDrive(-.5,  0);
                }
            }
    }
    void driveTo() {
        while (512 * (ultraSonic1.getVoltage() / 5) > 30) {
            myRobot.drive(-.5, -.5);
        }
    }

    /**
     * This function is called periodically during cameraServer mode
     */

    public void testPeriodic() {
        LiveWindow.run();
    }
}

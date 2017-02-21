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
    AnalogInput range = new AnalogInput(0);

    //TEST CHANGE
}

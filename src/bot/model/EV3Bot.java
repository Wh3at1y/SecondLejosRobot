package bot.model;

import lejos.robotics.chassis.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;

public class EV3Bot
	{
		private Chassis baseChassis;
		private Wheel leftWheel;
		private Wheel rightWheel;
		private long waitTime;
		private float[] ultrasonicSamples;
		private float[] touchSamples;

		private MovePilot botPilot;
		private EV3UltrasonicSensor distanceSensor;
		private EV3TouchSensor touchSensor;

		public EV3Bot()
			{

				waitTime = 4000;
				displayMessage("Sam's Dumbot! \nLess go!!!");
				setupWheels();
				setupSensors();
				setupSpeed(5000, 5000);
				driveRobot();
			}

		private void setupWheels()
			{
				baseChassis = new WheeledChassis(new Wheel[]{ leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
				botPilot = new MovePilot(baseChassis);
				leftWheel = WheeledChassis.modelWheel(Motor.A, 50).offset(-72);
				rightWheel = WheeledChassis.modelWheel(Motor.B, 50).offset(72);
			}

		private void setupSensors()
			{
				touchSensor = new EV3TouchSensor(LocalEV3.get().getPort("S2"));
				distanceSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
				ultrasonicSamples = new float[distanceSensor.sampleSize()];
				touchSamples = new float[touchSensor.sampleSize()];
				distanceSensor.fetchSample(ultrasonicSamples, 0);
			}

		private void setupSpeed(int xSpeed, int ySpeed)
			{
				baseChassis.setSpeed(xSpeed, ySpeed);
			}

		private void driveLong()
			{
				botPilot.travel(4550.00);
				leftWheel = WheeledChassis.modelWheel(Motor.A, 50).offset(-72);
				botPilot.rotate(-78);
				botPilot.travel(7000.00);
				botPilot.rotate(70);
				botPilot.travel(3400);
				botPilot.rotate(-85);
				botPilot.travel(1250);
			}

		public void driveRobot()
	{
		boolean isTouched = false;
		
		while(isTouched == false)
			{
				if(touchSamples[0] == 0)
					{
						botPilot.travel(-50);
						touchSensor.fetchSample(touchSamples,0);
						isTouched = false;
					}
				else
					{
						isTouched = true;
						moveFromDoor();
					}
			}
	}
		private void moveFromDoor()
		{
			botPilot.travel(4550.00);
			rotateBot("left");
		}
		
		private void moveDownClass()
		{
			
		}
		
		private void rotateBot(String direction)
		{
			if(direction.equalsIgnoreCase("left"))
				botPilot.rotate(-85);
			else if(direction.equalsIgnoreCase("right"))
				botPilot.rotate(85);
		}
		
		private void displayMessage(String message)
			{
				LCD.drawString(message, 0, 1);
				Delay.msDelay(waitTime);
				LCD.clear();
			}
	}

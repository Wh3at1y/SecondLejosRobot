package bot.model;

//-----Imports-----
import lejos.robotics.chassis.*;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.*;
//--------------------

public class EV3Bot
	{
		//-----Declaration Section-----
		private Chassis baseChassis;
		private Wheel leftWheel;
		private Wheel rightWheel;
		private long waitTime;
		private float[] ultrasonicSamples;
		private float[] touchSamples;
		//--------------------
		
		//-----Class Declaration-----
		private MovePilot botPilot;
		private EV3UltrasonicSensor distanceSensor;
		private EV3TouchSensor touchSensor;
		//--------------------
		
		//-----Constructor-----
		public EV3Bot()
			{
				waitTime = 3000;
				displayMessage("Sam's Dumbot!");
				setupWheels();
				setupSensors();
				setupSpeed(5000, 7000);
				driveRobot();
			}
		//--------------------

		public void driveRobot()
			{

				while (touchSamples[0] == 0)
					{
								displayMessage("Wall = touched.");
								moveFromDoor();
								touchSensor.fetchSample(ultrasonicSamples, 0);
					}
								botPilot.stop();
				}

		private void moveFromDoor()
			{
				botPilot.travel(4550.00);
				touchSensor.fetchSample(ultrasonicSamples, 0);
				rightWheel = WheeledChassis.modelWheel(Motor.B, 60).offset(60);
				rotateBot("left");
				displayMessage("Moving down Class");
				touchSensor.fetchSample(ultrasonicSamples, 0);
				moveDownClass();
			}

		private void moveDownClass()
			{
				touchSensor.fetchSample(ultrasonicSamples, 0);
				botPilot.travel(7000.00);
				touchSensor.fetchSample(ultrasonicSamples, 0);
				rotateBot("right");
			}

		private void rotateBot(String direction)
			{
				if (direction.equals("left"))
					botPilot.rotate(-85);
				else if (direction.equals("right"))
					botPilot.rotate(85);
			}

		private void displayMessage(String message)
			{
				LCD.drawString(message, 0, 1);
				Delay.msDelay(waitTime);
				LCD.clear();
			}

		private void setupWheels()
			{
				leftWheel = WheeledChassis.modelWheel(Motor.A, 60).offset(-60);
				rightWheel = WheeledChassis.modelWheel(Motor.B, 60).offset(65);
				baseChassis = new WheeledChassis(new Wheel[]{ leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
				botPilot = new MovePilot(baseChassis);
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
	}

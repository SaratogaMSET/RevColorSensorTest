/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public ColorSensorV3 colorSensor;
  
  private ColorMatch colorMatcher = new ColorMatch();

  // These values should be changed based on lighting and distance from the color strip
  private final Color BLUE_TARGET = ColorMatch.makeColor(0.170, 0.447, 0.383);
  private final Color GREEN_TARGET = ColorMatch.makeColor(0.207, 0.547, 0.248);
  private final Color RED_TARGET = ColorMatch.makeColor(0.442, 0.390, 0.168);
  private final Color YELLOW_TARGET = ColorMatch.makeColor(0.313, 0.536, 0.167);

  private String colorString;
  private double colorChanges;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    // m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    // m_chooser.addOption("My Auto", kCustomAuto);
    // SmartDashboard.putData("Auto choices", m_chooser);
    colorSensor = new ColorSensorV3(I2C.Port.kOnboard);

    colorMatcher.addColorMatch(BLUE_TARGET);
    colorMatcher.addColorMatch(GREEN_TARGET);
    colorMatcher.addColorMatch(RED_TARGET);
    colorMatcher.addColorMatch(YELLOW_TARGET);

    colorString = "Unknown";
    colorChanges = 0;
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    // m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    // System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    // switch (m_autoSelected) {
    //   case kCustomAuto:
    //     // Put custom auto code here
    //     break;
    //   case kDefaultAuto:
    //   default:
    //     // Put default auto code here
    //     break;
    // }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Color currentColor = colorSensor.getColor();

    String currentColorString;
    ColorMatchResult match = colorMatcher.matchClosestColor(currentColor);

    double confidence = match.confidence;

    if (match.color == BLUE_TARGET) {
      currentColorString = "Blue";
    } else if (match.color == RED_TARGET) {
      currentColorString = "Red";
    } else if (match.color == GREEN_TARGET) {
      currentColorString = "Green";
    } else if (match.color == YELLOW_TARGET) {
      currentColorString = "Yellow";
    } else {
      currentColorString = "Unknown";
    }

    if (!currentColorString.equals(colorString) && !currentColorString.equals("Unknown") && confidence > 0.95) {
      colorChanges++;
      colorString = currentColorString;
    }

    SmartDashboard.putNumber("Red", currentColor.red);
    SmartDashboard.putNumber("Green", currentColor.green);
    SmartDashboard.putNumber("Blue", currentColor.blue);
    SmartDashboard.putString("Detected Color", colorString);
    SmartDashboard.putNumber("Confidence", confidence);
    SmartDashboard.putNumber("Color Changes", colorChanges);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}

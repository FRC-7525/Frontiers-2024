// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Shooter;
import frc.robot.commands.AutoCommands;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  public Drive drive;
  public Shooter shooter;

	private SendableChooser<String> autoChooser;
	private AutoCommands autoCommands = new AutoCommands(this);
	private Command autonomousCommand;

  @Override
  public void robotInit() {
    drive = new Drive();
    shooter = new Shooter();

    NamedCommands.registerCommand("Shooting", autoCommands.shooting());

    autoChooser = new SendableChooser<String>();
    autoChooser.addOption("1: Start Scource Speaker | Cross Line", "Shoot and Move");

    SmartDashboard.putData("Auto autoChooser", autoChooser);
  }

  @Override
  public void robotPeriodic() {
    shooter.periodic();
  }

  @Override
  public void autonomousInit() {
    autonomousCommand = new PathPlannerAuto(autoChooser.getSelected());

		if (autonomousCommand != null) {
			autonomousCommand.schedule();
		}
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
  }

  @Override
  public void teleopPeriodic() {
        drive.periodic();

  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}

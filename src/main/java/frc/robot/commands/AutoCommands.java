package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;

public class AutoCommands {

	Robot robot = null;

	public AutoCommands(Robot robot) {
		this.robot = robot;
	}

	public Command shooting() {
		return new InstantCommand(() -> robot.shooter.shooting());
	}
}

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;

public class Shoot extends Command {

	// Kinda an auto command, but it has a is finished condition soooo too bad so sad

	Robot robot = null;
	public boolean shot = false;

	public Shoot(Robot robot) {
		this.robot = robot;
	}

	@Override
	public void initialize() {  
        robot.shooter.shooting();
	}

	@Override
	public void execute() {
	}

	@Override
	public boolean isFinished() {
        return robot.shooter.finishedShooting();
	}
}

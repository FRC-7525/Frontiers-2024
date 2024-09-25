package frc.robot.subsystems;

import java.io.File;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public class Drive extends SubsystemBase {
    double maximumSpeed = Units.feetToMeters(12);
    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
    public SwerveDrive swerveDrive;
    public XboxController controller;

    public Drive() {
        controller = new XboxController(1);
        try {
            swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(maximumSpeed);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void periodic() {
        double xMovement = MathUtil.applyDeadband(controller.getLeftY(), 0.5);
        double rotation = MathUtil.applyDeadband(controller.getRightX(), 0.5);
        double yMovement = MathUtil.applyDeadband(controller.getLeftX(), 0.5);
        swerveDrive.drive(new Translation2d(xMovement, yMovement), rotation, false, false);    
    }
}

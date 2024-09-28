package frc.robot.subsystems;

import java.io.File;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class Drive {
    private double maximumSpeed;
    private File swerveJsonDirectory;
    private SwerveDrive swerveDrive;
    private XboxController controller;

    public Drive() {
        controller = new XboxController(0);
        maximumSpeed = Units.feetToMeters(12);
        try {
            swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
            swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(maximumSpeed);
            SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
            System.out.println("swerve config success");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("womp womp womp");
        }
    }

    public void periodic() {
        double xMovement = MathUtil.applyDeadband(controller.getLeftY(), 0.5);
        double rotation = MathUtil.applyDeadband(controller.getRightX(), 0.5);
        double yMovement = MathUtil.applyDeadband(controller.getLeftX(), 0.5);
        swerveDrive.drive(new Translation2d(xMovement, yMovement), rotation, false, false);    
    }
}

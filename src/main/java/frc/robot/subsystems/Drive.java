package frc.robot.subsystems;

import java.io.File;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import swervelib.math.SwerveMath;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;
import frc.robot.Constants;

public class Drive extends SubsystemBase {
    private double maximumSpeed;
    private File swerveJsonDirectory;
    private SwerveDrive swerveDrive;
    private XboxController controller;

    private final double MAX_LINEAR_SPEED = Units.feetToMeters(16.0);
    private final double TRACK_WIDTH_X = Units.inchesToMeters(24.0);
	private final double TRACK_WIDTH_Y = Units.inchesToMeters(24.0);
	private final double DRIVE_BASE_RADIUS = Math.hypot(
			TRACK_WIDTH_X / 2.0,
			TRACK_WIDTH_Y / 2.0
		);
	private final double MAX_ANGULAR_SPEED = MAX_LINEAR_SPEED / DRIVE_BASE_RADIUS;

    private final double translationMultiplier = 1.0;
    // Rotates toooo fast
    private final double rotaitonMultiplier = 0.5;


    public Drive() {
        try {
            swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
            swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(MAX_LINEAR_SPEED);
            SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
            System.out.println("swerve config success");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("womp womp womp");
        }

		pathPlannerInit();
	}

    public void periodic() {
        double xMovement = MathUtil.applyDeadband(controller.getLeftY(), 0.1);
        double rotation = MathUtil.applyDeadband(controller.getRightX(), 0.1);
        double yMovement = MathUtil.applyDeadband(controller.getLeftX(), 0.1);
        swerveDrive.drive(new Translation2d(xMovement * MAX_LINEAR_SPEED * translationMultiplier, yMovement * MAX_LINEAR_SPEED * translationMultiplier), rotation * MAX_ANGULAR_SPEED * rotaitonMultiplier, false, false);    
    }

    public void pathPlannerInit() {
		AutoBuilder.configureHolonomic(
			this::getPose, // Robot pose supplier
			this::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
			this::getRobotVelocity, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
			this::setChassisSpeeds, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
			new HolonomicPathFollowerConfig(
				// HolonomicPathFollowerConfig, this should likely live in
				// your Constants class
				Constants.AutonConstants.TRANSLATION_PID, // Translation PID constants
				Constants.AutonConstants.ANGLE_PID, // Rotation PID constants
				Constants.AutonConstants.MAX_MODULE_SPEED, // Max module speed, in m/s
				swerveDrive.swerveDriveConfiguration.getDriveBaseRadiusMeters(), // Drive base radius in meters. Distance from robot center to
				// furthest module.
				// Replans path if vision or odo detects errors (S tier)
				new ReplanningConfig(false, false) // Default path replanning config. See the API for the options
				// here
			),
			() -> {
				// Boolean supplier that controls when the path will be mirrored for the red alliance
				// This will flip the path being followed to the red side of the field.
				// THE ORIGIN WILL REMAIN ON THE BLUE SIDE

				var alliance = DriverStation.getAlliance();
				if (alliance.isPresent()) {
					return alliance.get() == DriverStation.Alliance.Red;
				}
				return false;
			},
			this // Reference to this subsystem to set requirements
		);
	}

	private Pose2d getPose() {
		return swerveDrive.getPose();
	}

	public void resetOdometry(Pose2d initialHolonomicPose)
	{
	  swerveDrive.resetOdometry(initialHolonomicPose);
	}  

	public ChassisSpeeds getRobotVelocity()
	{
	  return swerveDrive.getRobotVelocity();
	}

	public void setChassisSpeeds(ChassisSpeeds chassisSpeeds)
	{
	  swerveDrive.setChassisSpeeds(chassisSpeeds);
	}
}

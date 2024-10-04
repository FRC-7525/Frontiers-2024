package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;

public class Shooter {

    private enum ShooterStates {
        OFF,
        SHOOTING,
        SPINNING_UP,
        INTAKING
    }

    private TalonFX shooterMotor;
    private TalonFX shooterMotorBottom;
    private ShooterStates state;
    private XboxController controller;
    
    // TODO: Test, disable if not working
    private final boolean CURRENT_SENSING_ON = true;
    private final boolean AUTOMATIC_SPEED_TRANSITION = true;  
    private boolean finishedShooting = false;

    private final double SHOOTER_CURRENT_LIMIT = 4.0;
    private final double VELOCITY_TARGET_RPS = 60.0;
    private Timer commandTimer = new Timer();

    public Shooter() {
        controller = new XboxController(0);
        shooterMotor = new TalonFX(13);
        shooterMotorBottom = new TalonFX(14);
        shooterMotor.setInverted(true);
        state = ShooterStates.OFF;
    }

    public void periodic() {
        if (state == ShooterStates.OFF) {
            if (controller.getAButtonPressed()) {
                state = ShooterStates.SPINNING_UP;
            } 
            if (controller.getBButtonPressed()) {
                state = ShooterStates.INTAKING;
            }
            shooterMotor.set(0);
            shooterMotorBottom.set(0);
        } else if (state == ShooterStates.SPINNING_UP) {
            if (controller.getAButtonPressed()) {
                state = ShooterStates.SHOOTING;
            }
            if (shooterMotor.getVelocity().getValueAsDouble() > VELOCITY_TARGET_RPS && AUTOMATIC_SPEED_TRANSITION) {
                state = ShooterStates.SHOOTING;
            }
            shooterMotor.set(1);
            shooterMotorBottom.set(0.0);
        } else if (state == ShooterStates.SHOOTING) {
            if (controller.getAButtonPressed()) {
                state = ShooterStates.OFF;
            }
            if (DriverStation.isAutonomous()) {
                commandTimer.start();
                if (commandTimer.get() > Constants.Shooter.SHOOT_TIME) {
                    state = ShooterStates.OFF;
                    finishedShooting = true;
                }
            }
            shooterMotor.set(1);
            shooterMotorBottom.set(1);
        } else if (state == ShooterStates.INTAKING) {
            if (controller.getBButtonPressed()) {
                state = ShooterStates.OFF;
            }
            // TODO: Tune current limit and check what motor to use
            if (shooterMotor.getSupplyCurrent().getValueAsDouble() > SHOOTER_CURRENT_LIMIT && CURRENT_SENSING_ON) {
                state = ShooterStates.OFF;
            }
            shooterMotor.set(-0.5);
            shooterMotorBottom.set(-0.5);
        }

        if (commandTimer.get() > Constants.Shooter.SHOOT_TIME) {
            state = ShooterStates.OFF;
        }
    }

    public void shooting() {
        // commandTimer.reset();
        finishedShooting = false;
        state = ShooterStates.SPINNING_UP;
        // commandTimer.start();
    }

    public boolean finishedShooting() {
        return finishedShooting;
    }
}

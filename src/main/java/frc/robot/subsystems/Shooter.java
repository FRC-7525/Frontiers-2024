package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;

public class Shooter {

    enum ShooterStates {
        OFF,
        SHOOTING,
        INTAKING
    }

    TalonFX shooterMotor;
    TalonFX shooterMotorBottom;
    ShooterStates state;
    XboxController controller;
    Timer commandTimer;

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
                state = ShooterStates.SHOOTING;
            } 
            if (controller.getBButtonPressed()) {
                state = ShooterStates.INTAKING;
            }
            shooterMotor.set(0);
            shooterMotorBottom.set(0);
        } else if (state == ShooterStates.SHOOTING) {
            if (controller.getAButtonPressed()) {
                state = ShooterStates.OFF;
            }
            shooterMotor.set(1);
            shooterMotorBottom.set(1);
        } else if (state == ShooterStates.INTAKING) {
            if (controller.getBButtonPressed()) {
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
        commandTimer.reset();
        state = ShooterStates.SHOOTING;
        commandTimer.start();
    }
}

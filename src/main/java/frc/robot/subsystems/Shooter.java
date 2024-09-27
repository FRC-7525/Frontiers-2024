package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.XboxController;

public class Shooter {

    enum ShooterStates {
        OFF,
        SHOOTING,
        INTAKING
    }

    TalonFX shooterMotor;
    ShooterStates state;
    XboxController controller;

    public Shooter() {
        controller = new XboxController(0);
        shooterMotor = new TalonFX(13);
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
        } else if (state == ShooterStates.SHOOTING) {
            if (controller.getAButtonPressed()) {
                state = ShooterStates.OFF;
            }
            shooterMotor.set(1);
        } else if (state == ShooterStates.INTAKING) {
            if (controller.getBButtonPressed()) {
                state = ShooterStates.OFF;
            }
            shooterMotor.set(-0.5);
        }
    }
}

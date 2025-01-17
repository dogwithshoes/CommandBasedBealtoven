package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.ControlType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

//import frc.robot.subsystems.Shooter.ShooterSetPoint;

public class Shooter extends SubsystemBase{
 
    private CANSparkMax leftMotor;
    private CANSparkMax rightMotor;

    private RelativeEncoder m_leftEncoder;
    private RelativeEncoder m_rightEncoder;
    public ShooterSetpoint sendSetpoint;

    public Shooter(){
        leftMotor = new CANSparkMax(62, MotorType.kBrushless);
        rightMotor = new CANSparkMax(61, MotorType.kBrushless);

        leftMotor.restoreFactoryDefaults();
        rightMotor.restoreFactoryDefaults();

        leftMotor.setInverted(true);

        leftMotor.getEncoder().setVelocityConversionFactor(1.0);
        leftMotor.getEncoder().setPositionConversionFactor(1.0 / 1.0);

        rightMotor.getEncoder().setVelocityConversionFactor(1.0);
        rightMotor.getEncoder().setPositionConversionFactor(1.0 / 1.0);

        leftMotor.setSmartCurrentLimit(20);
        rightMotor.setSmartCurrentLimit(20);

        m_leftEncoder = leftMotor.getEncoder();
        m_rightEncoder = rightMotor.getEncoder();

        var pidController = leftMotor.getPIDController();
        pidController.setP(0.000012);
        pidController.setD(0.0002);
        pidController.setFF(0.000172);
        pidController.setOutputRange(-1, 1);

        pidController = rightMotor.getPIDController();
        pidController.setP(0.000012);
        pidController.setD(0.0002);
        pidController.setFF(0.000172);
        pidController.setOutputRange(-1, 1);

        // leftMotor.getEncoder().setVelocityConversionFactor(1.0);
        // rightMotor.getEncoder().setVelocityConversionFactor(1.0);
    }

    public void startShooter(ShooterSetpoint setpoint){
        setTargetVelocity(setpoint);
    }

    public enum ShooterSetpoint {
            zero(0, 0),
            plopSetpoint(1500, 1500),
            speakerSetpoint(5200, 5200),
            podiumSetpoint(5200, 5200),
            ampSetpoint(2000, 2000);

            private final double leftVelocity;
            private final double rightVelocity;

            ShooterSetpoint(double leftVelocity, double rightVelocity) {
                this.leftVelocity = leftVelocity;
                this.rightVelocity = rightVelocity;
            }

            public double getLeftVelocity() {
                return leftVelocity;
            }

            public double getRightVelocity() {
                return rightVelocity;
            }
        }

    public void setTargetVelocity(ShooterSetpoint setpoint){
        if (setpoint == null){
            setpoint = ShooterSetpoint.zero;
        }

        sendSetpoint = setpoint;

        leftMotor.getPIDController().setReference(setpoint.getLeftVelocity(), ControlType.kVelocity);
        rightMotor.getPIDController().setReference(setpoint.getRightVelocity(), ControlType.kVelocity);
    }
}

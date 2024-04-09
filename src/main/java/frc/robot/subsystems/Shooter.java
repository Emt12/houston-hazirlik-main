package frc.robot.subsystems;

import frc.robot.Constants.CanIds;
import frc.robot.Constants.ShooterConstants;
import frc.robot.custom.CustomEncoder;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends SubsystemBase {


    private final int kWristCanId = CanIds.kWristCanId;
    private final double kSlewLimit = ShooterConstants.kSlewLimit;

    private final double kRad = ShooterConstants.kRad;

    private final double kLimitUp = ShooterConstants.kLimitUp;
    private final double kLimitDown = ShooterConstants.kLimitDown;

    private final CANSparkFlex mLeft = new CANSparkFlex(CanIds.kShooterLeft, MotorType.kBrushless);
    private final CANSparkFlex mRight = new CANSparkFlex(CanIds.kShooterRight, MotorType.kBrushless);

    private final CANSparkMax mWrist = new CANSparkMax(kWristCanId, MotorType.kBrushless);

    private final CustomEncoder customEncoder = new CustomEncoder(mWrist.getEncoder(), kRad);
    private final SlewRateLimiter slewLimit = new SlewRateLimiter(kSlewLimit);

    public Shooter() {
        mWrist.setIdleMode(IdleMode.kBrake);
    }

    public void wristSet(double speed) {
        mWrist.set(speed);
    }

    public void wristMove(double speed) {
        speed = slewLimit.calculate(speed);
        double distance = customEncoder.getDistance();

        if ((speed > 0 && distance <= kLimitUp) || (speed < 0 && distance >= kLimitDown))// MARK
            wristSet(speed);
        else
            wristSet(0);
    }

    public RelativeEncoder giveWristEncoder() {
        return mWrist.getEncoder();
    }


    public void shoot(double speed){
        mLeft.set(speed);
        mRight.set(-speed);
    }

    public void driveWithJoystick(double joystickInput) {
        wristMove(joystickInput);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("encoder-wrist", customEncoder.getDistance());
        super.periodic();
    }

}

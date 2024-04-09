// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants.OIConstants;

//Commad Class
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

//Subsystems
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;

//Commands
import frc.robot.commands.ShooterCommands.ShootCmd;
import frc.robot.commands.DriveCommands.ResetRelative;
import frc.robot.commands.DriveCommands.SetX;
import frc.robot.commands.IntakeCommands.IntakeGiveCmd;
import frc.robot.commands.IntakeCommands.IntakeTakeCmd;

public class RobotContainer {

    //Subsystems
    Swerve  swerve                          = new Swerve();
    Shooter shooter                         = new Shooter();
    Intake  intake                          = new Intake();
    Index   index                           = new Index();

    //Driver Axis-Button Definitions
    XboxController driverJoystick           = new XboxController(0);
    Joystick       operatorJoystick         = new Joystick(1);
    Joystick       testJoystick             = new Joystick(2);

    JoystickButton driverA                  = new JoystickButton(driverJoystick, 1);
    JoystickButton driverB                  = new JoystickButton(driverJoystick, 2);
    JoystickButton driverX                  = new JoystickButton(driverJoystick, 3);
    JoystickButton driverY                  = new JoystickButton(driverJoystick, 4);
    JoystickButton driverRB                 = new JoystickButton(driverJoystick, 5);
    
    double         driverRightX             = driverJoystick.getRightX() / 1.5;
    double         driverRightY             = driverJoystick.getRightY() / 1.5;
    double         driverTurn               = (driverJoystick.getRightTriggerAxis() - driverJoystick.getLeftTriggerAxis()) / 1.5;
    
    
    //Operator Axis-Button Definitions
    JoystickButton operatorA                = new JoystickButton(operatorJoystick, 1);
    JoystickButton operatorB                = new JoystickButton(operatorJoystick, 2);
    JoystickButton operatorX                = new JoystickButton(operatorJoystick, 3);
    JoystickButton operatorY                = new JoystickButton(operatorJoystick, 4);
    double         operatorRightY           = operatorJoystick.getRawAxis(4);
    
    
    //Commands
    ShootCmd       Shoot                    = new ShootCmd(shooter, index);
    ResetRelative  ResetRelative            = new ResetRelative(swerve);
    SetX           SetX                     = new SetX(swerve);
    IntakeGiveCmd  IntakeGive               = new IntakeGiveCmd(intake, index);
    IntakeTakeCmd  IntakeTake               = new IntakeTakeCmd(intake, index);

    //Robot
    Robot          robot                    = new Robot();

    public RobotContainer() {
        configureButtonBindings();

        shooter.setDefaultCommand(
                new RunCommand(
                        () -> shooter.driveWithJoystick(
                                MathUtil.applyDeadband(operatorRightY, OIConstants.kWristDeadband)),
                        shooter));

        swerve.setDefaultCommand(
                new RunCommand(
                        () -> swerve.drive(
                                MathUtil.applyDeadband(driverRightY, OIConstants.kDriveDeadband),
                                MathUtil.applyDeadband(driverRightX, OIConstants.kDriveDeadband),
                                -MathUtil.applyDeadband(driverTurn, OIConstants.kDriveDeadband),
                                true, true),
                        swerve));

    }

    private void configureButtonBindings() {

        // Driver
        driverX.whileTrue(SetX);
        driverRB.whileTrue(ResetRelative);

        // Operator
        operatorA.whileTrue(IntakeTake);
        operatorB.whileTrue(IntakeGive);
        operatorY.whileTrue(Shoot);
    }

    /**
     * Just for disappearing the error: <b>m_robotContainer not used</b>
     */
    public void errorSolver() {
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return new PrintCommand("auto");
    }
}

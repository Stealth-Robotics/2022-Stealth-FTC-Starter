package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class Arm extends SubsystemBase {
    final DcMotorEx armMotor;
    final DigitalChannel armLimitSwitch;
    final DigitalChannel armLimitSwitch2;
    final Telemetry telemetry;
    List<LynxModule> lynxModules;

    public static final int MIN_POSITION = 0;
    public static final int MAX_POSITION = 2000;

    public Arm(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        armLimitSwitch = hardwareMap.get(DigitalChannel.class, "armLimitSwitch");
        armLimitSwitch2 = hardwareMap.get(DigitalChannel.class, "armLimitSwitch2");

        armMotor = hardwareMap.get(DcMotorEx.class, "armMotor");
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setTargetPositionTolerance(10);
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setVelocityPIDFCoefficients(5.0, 0.01, 0.0, 7.0);
        // armDrive.setPositionPIDFCoefficients(50.0);

        // TODO: feels like this ought to be somewhere more general. A overall robot class or subsystem?
        lynxModules = hardwareMap.getAll(LynxModule.class);
    }

    // Work to do after before any OpMode starts
    public void initialize() {
        resetMinimumPosition();
    }

    @Override
    public void periodic() {
        telemetry.addData("Arm", "Position: %d", armMotor.getCurrentPosition());
        telemetry.addData("Arm", "Limit switch: %s", armLimitSwitch.getState());
        telemetry.addData("Arm", "Limit switch 2: %s", armLimitSwitch2.getState());
    }

    public void setPosition(int position) {
        armMotor.setTargetPosition(position);
        armMotor.setPower(1.0);
    }

    public int getCurrentPosition() {
        return armMotor.getCurrentPosition();
    }

    // Used when concurrent commands may have populated the bulk cache with an old motor position.
    public int getCurrentPositionNoCache() {
        resetPositionCache();
        return armMotor.getCurrentPosition();
    }

    private void resetPositionCache() {
        for (LynxModule module : lynxModules) {
            module.clearBulkCache();
        }
    }

    // mmmfixme: I don't like this. Would prefer a real command to do this and to have that commend execute while we're waiting to start.
    private void resetMinimumPosition() {
        // A little timeout, just in case the limit switch is broken.
        ElapsedTime runtime = new ElapsedTime();
        while (armLimitSwitch.getState() && runtime.seconds() < 4) {
            armMotor.setTargetPosition(-5000); // ~5000 encoder ticks in a full revolution.
            armMotor.setPower(0.5);
        }
        armLimitSwitchReset();
    }

    private void armLimitSwitchReset() {
        armMotor.setPower(0.0);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public boolean isArmAtLowerLimit() {
        return !armLimitSwitch.getState();
    }
}

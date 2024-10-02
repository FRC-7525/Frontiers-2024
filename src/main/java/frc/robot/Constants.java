package frc.robot;

import com.pathplanner.lib.util.PIDConstants;

public class Constants {
    public static final class AutonConstants
    {
  
      public static final PIDConstants TRANSLATION_PID = new PIDConstants(0.7, 0, 0);
      public static final PIDConstants ANGLE_PID = new PIDConstants(0.4, 0, 0.01);
      public static final double MAX_MODULE_SPEED = 4.5;
    }
 
    public static final class Shooter {
      public static final double SHOOT_TIME = 1;
    }
}

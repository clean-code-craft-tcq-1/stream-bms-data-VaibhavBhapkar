using System;
using System.Collections.Generic;
using System.Text;

namespace BMS_Streaming
{
    public class BatteryParameterLimits:IBatteryLimits
    {       
        public int minTemperatureDelta => 0;

        public int maxTemperatureDelta => 100;

        public double minStateOfCharge => 0;

        public double maxStateOfCharge => 9.8;
    }
}

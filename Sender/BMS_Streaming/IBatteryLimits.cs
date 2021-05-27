using System;
using System.Collections.Generic;
using System.Text;

namespace BMS_Streaming
{
    public interface IBatteryLimits
    {
        int minTemperatureDelta { get; }
        int maxTemperatureDelta { get; }
        double minStateOfCharge { get; }
        double maxStateOfCharge { get; }
    }
}

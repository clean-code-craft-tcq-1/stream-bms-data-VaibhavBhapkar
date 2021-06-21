using System;
using System.Collections.Generic;
using System.Text;

namespace BMS_Streaming
{
    public class BatteryDataGenerator : IStreamingInput
    {
        private readonly IBatteryLimits batteryLimits;
        private readonly Random randomNumberGenerator = new Random();
        public BatteryDataGenerator(IBatteryLimits _batteryLimits)
        {
            batteryLimits = _batteryLimits;
        }
        public int ReadTempratureInput()
        {
            int min = batteryLimits.minTemperatureDelta;
            int max = batteryLimits.maxTemperatureDelta;
            return randomNumberGenerator.Next(min, max);
        }
        public double ReadStateOfChargeInput()
        {
            double min = batteryLimits.minStateOfCharge;
            double max = batteryLimits.maxStateOfCharge;
            double range = max - min;
            double sampleNumber = randomNumberGenerator.NextDouble();
            double scaledNumber = (sampleNumber * range) + min;
            return Math.Round(scaledNumber, 2);
        }
    }
}

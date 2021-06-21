using Newtonsoft.Json.Linq;
using System;
using System.Threading.Tasks;
using Xunit;

namespace BMS_Streaming.Test
{
    public class BatteryDataStreamingTest
    {
         BatteryDataStreaming batteryDataStreaming;
        public BatteryDataStreamingTest()
        {
            IBatteryLimits batteryLimits = new BatteryParameterLimits();
            IStreamingInput batteryStreamingInput = new BatteryDataGenerator(batteryLimits);
            batteryDataStreaming = new BatteryDataStreaming(batteryStreamingInput);
        }

        [Fact]
        public void WhenGetBatteryReadingIsCalled_ThenReturnValidBatteryReading()
        {
            int result = 0;
            double socResult = 0;
            string batteryReadings = batteryDataStreaming.GetBatteryReadings();
            dynamic details = JObject.Parse(batteryReadings);
            string temperature=details.temperature;
            string stateOfCharge = details.stateOfCharge;
            Assert.True((int.TryParse(temperature, out result)) && (double.TryParse(stateOfCharge, out socResult)));
        }

        [Fact]
        public void WhenGetBatteryReadingIsCalled_ThenReturnValidBatteryReadingWithinLimits()
        {
            IBatteryLimits batteryLimits = new BatteryParameterLimits();
            string batteryReadings = batteryDataStreaming.GetBatteryReadings();
            dynamic details = JObject.Parse(batteryReadings);
            string temperature = details.temperature;
            string stateOfCharge = details.stateOfCharge;
            Assert.True((Convert.ToInt32(temperature) <= batteryLimits.maxTemperatureDelta) && (Convert.ToDouble(stateOfCharge) <= batteryLimits.maxStateOfCharge));
        }
    }
}

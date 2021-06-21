using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace BMS_Streaming
{
    public class BatteryDataStreaming
    {
        private readonly IStreamingInput batteryStreamingInput;
        public BatteryDataStreaming(IStreamingInput _streamingInput)
        {
            batteryStreamingInput = _streamingInput;
        }
        public string GetBatteryReadings()
        {
            BatteryParameters batteryParameters = new BatteryParameters();
            batteryParameters.temperature = batteryStreamingInput.ReadTempratureInput();
            batteryParameters.stateOfCharge = batteryStreamingInput.ReadStateOfChargeInput();
            string batteryReadings = JsonConvert.SerializeObject(batteryParameters);
            return batteryReadings;
        }

        
    }
}

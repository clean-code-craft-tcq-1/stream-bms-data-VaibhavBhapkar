using System;
using System.Threading;
using System.Threading.Tasks;

namespace BMS_Streaming
{
    class Program
    {
        private static readonly CancellationTokenSource cancellationToken = new CancellationTokenSource();

        static async Task Main(string[] args)
        {
            Console.WriteLine("Application has started - Getting Battery Parameters data.\nPress Ctrl-C to end");
            Console.CancelKeyPress += (sender, eventArgs) =>
            {
                Console.WriteLine("Streaming stop event is triggered");
                cancellationToken.Cancel();
                eventArgs.Cancel = true;
            };
            await GetBatteryHealthData();
            Console.WriteLine("Now shutting down Stream");
            await Task.Delay(1000);
        }

        async static Task GetBatteryHealthData()
        {
            IBatteryLimits batteryLimits = new BatteryParameterLimits();
            IStreamingInput batteryStreamingInput = new BatteryDataGenerator(batteryLimits);
            BatteryDataStreaming batteryDataStreaming = new BatteryDataStreaming(batteryStreamingInput);
            while (!cancellationToken.IsCancellationRequested)
            {
                Console.WriteLine(batteryDataStreaming.GetBatteryReadings());
                await Task.Delay(1000);
            }
        }
    }
}

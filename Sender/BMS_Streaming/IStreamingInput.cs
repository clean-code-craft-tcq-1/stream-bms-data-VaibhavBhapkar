using System;
using System.Collections.Generic;
using System.Text;

namespace BMS_Streaming
{
    public interface IStreamingInput
    {
        int ReadTempratureInput();
        double ReadStateOfChargeInput();
    }
}

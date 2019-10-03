
package led_matrix

import spinal.core._
import spinal.lib._

import scala.util.Random

class LedMatrixTop extends Component {
    val io = new Bundle {
        val LED_R_   = out(Bool)
        val LED_G_   = out(Bool)
        val LED_B_   = out(Bool)
    }

    noIoPrefix()

    val leds = new Area {
        io.LED_R_ := True
        io.LED_G_ := False
        io.LED_B_ := True
    }
}


//Generate the MyTopLevel's Verilog
object LedMatrixTopVerilog {
    def main(args: Array[String]) {
        SpinalVerilog(new LedMatrixTop)
    }
}


//Define a custom SpinalHDL configuration with synchronous reset instead of the default asynchronous one. This configuration can be resued everywhere
object MySpinalConfig extends SpinalConfig(defaultConfigForClockDomains = ClockDomainConfig(resetKind = SYNC))


//Generate the MyTopLevel's Verilog using the above custom configuration.
object LedMatrixTopVerilogWithCustomConfig {
    def main(args: Array[String]) {
        MySpinalConfig.generateVerilog(new LedMatrixTop)
    }
}


package led_matrix

import spinal.core._
import spinal.lib._

import scala.util.Random

class LedMatrixTop extends Component {
    val io = new Bundle {
        val led_r   = out(Bool)
        val led_g   = out(Bool)
        val led_b   = out(Bool)
    }

    val leds = new Area {
        io.led_r := False
        io.led_g := False
        io.led_b := True
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

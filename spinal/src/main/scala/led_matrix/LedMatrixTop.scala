
package led_matrix

import spinal.core._
import spinal.lib._

import ice40._

class LedMatrixTop extends Component {
    val io = new Bundle {
        val OSC_CLK_IN  = in(Bool)

        val LED_R_   = out(Bool)
        val LED_G_   = out(Bool)
        val LED_B_   = out(Bool)
    }

    noIoPrefix()

/*
    val osc_clk_raw = Bool

    val u_osc = new SB_HFOSC(clkhf_div = "0b10")
    u_osc.io.CLKHFPU    <> True
    u_osc.io.CLKHFEN    <> True
    u_osc.io.CLKHF      <> osc_clk_raw
*/

    val oscClkRawDomain = ClockDomain(
        clock = io.OSC_CLK_IN,
        frequency = FixedFrequency(12 MHz),
        config = ClockDomainConfig(
                    resetKind = BOOT
        )
    )

    //============================================================
    // Create osc clock reset
    //============================================================
    val osc_reset_ = Bool

    val osc_reset_gen = new ClockingArea(oscClkRawDomain) {
        val reset_unbuffered_ = True

        val reset_cntr = Reg(UInt(5 bits)) init(0)
        when(reset_cntr =/= U(reset_cntr.range -> true)){
            reset_cntr := reset_cntr + 1
            reset_unbuffered_ := False
        }

        osc_reset_ := RegNext(reset_unbuffered_)
    }


    val osc_clk    = Bool
    osc_clk       := io.OSC_CLK_IN

    val oscClkDomain = ClockDomain(
        clock = osc_clk,
        reset = osc_reset_,
        config = ClockDomainConfig(
            resetKind = SYNC,
            resetActiveLevel = LOW
        )
    )


    val led_red = Bool

    val core = new ClockingArea(oscClkDomain) {

        val led_counter = Reg(UInt(24 bits))
        led_counter := led_counter + 1
        //led_red := led_counter.msb

        val u_cpu = new CpuTop()


        u_cpu.io.led_mem_rd       := True
        u_cpu.io.led_mem_rd_addr  := 0

        led_red := u_cpu.io.led_mem_rd_data.orR
    }


    val leds = new Area {
        io.LED_R_ := ~led_red
        io.LED_G_ := ~core.u_cpu.io.led_green
        io.LED_B_ := ~core.u_cpu.io.led_blue
    }

}


//Generate the MyTopLevel's Verilog
object LedMatrixTopVerilog {
    def main(args: Array[String]) {
        
        val config = SpinalConfig(anonymSignalUniqueness = true)
        config.generateVerilog(new LedMatrixTop)
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

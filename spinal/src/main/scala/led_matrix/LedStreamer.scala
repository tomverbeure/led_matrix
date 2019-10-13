
package led_matrix

import spinal.core._
import spinal.lib._
import spinal.lib.io._
import spinal.lib.bus.misc._

class LedStreamer extends Component {

    val io = new Bundle {
        val led_stream    = master(Stream(Bits(24 bits)))
    }

    object FsmState extends SpinalEnum {
        val Idle            = newElement()
        val LoadLedVal      = newElement()
        val ShiftLedTh      = newElement()
        val ShiftLedTl      = newElement()
        val LedReset        = newElement()
    }

    val cur_state = Reg(FsmState()) init(FsmState.Idle)

    val led_cntr  = Reg(UInt(8 bits))

    io.led_stream.valid   := False
    io.led_stream.payload := led_cntr(5 downto 0).asBits.resize(24)

    switch(cur_state){
        is(FsmState.Idle){
            io.led_stream.valid   := False

            led_cntr              := 63
            cur_state             := FsmState.LoadLedVal
        }

        is(FsmState.LoadLedVal){

            io.led_stream.valid   := True

            when(io.led_stream.ready){
                when(led_cntr === 0){
                    cur_state     := FsmState.Idle
                }
                .otherwise{
                    led_cntr      := led_cntr - 1
                }
            }
        }
    }

}

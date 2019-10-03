
package ice40

import spinal.core._

class SB_HFOSC(
        clkhf_div         : Int = 0
    ) extends BlackBox {

    val generic = new Generic {
        val CLKHF_DIV     = clkhf_div 
    }

    val io = new Bundle {
        val CLKHFPU       = in(Bool)
        val CLKHFEN       = in(Bool)
        val CLKHF         = out(Bool)
    }

    noIoPrefix()
}



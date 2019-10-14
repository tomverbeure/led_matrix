#include <stdint.h>
#include <math.h>

#include "reg.h"
#include "top_defines.h"

#define NR_LEDS     384

static inline uint32_t rdcycle(void) {
    uint32_t cycle;
    asm volatile ("rdcycle %0" : "=r"(cycle));
    return cycle;
}

static inline int nop(void) {
    asm volatile ("addi x0, x0, 0");
    return 0;
}

void wait(int cycles)
{
#if 1
    volatile int cnt = 0;

    for(int i=0;i<cycles/20;++i){
        ++cnt;
    }
#else
    int start;

    start = rdcycle();
    while ((rdcycle() - start) <= cycles);
#endif
}


#define WAIT_CYCLES 4000000

int main() {

    REG_WR(LED_DIR, 0xff);

    for(int i=0;i<NR_LEDS;++i){
        MEM_WR(LED_MEM, i, 0);
    }

    int cntr = 0;

    while(1){
        REG_WR(LED_STREAMER_CONFIG, 1);
        REG_WR(LED_STREAMER_CONFIG, 0);

#if 1
        for(int panel=0; panel<6; ++panel){
            for(int x=0; x<8; ++x){
                for(int y=0; y<8; ++y){
                    int led_nr = panel*64 + y*8 + x;

                    MEM_WR(LED_MEM, led_nr, (((panel & 3)<<4) << 16) | ((y<<3)<<8) | (((x+(cntr>>4))<<3) & 0x3f));
                }
            }
        }
#endif

#if 0
        for(int i=0;i<NR_LEDS;++i){
            MEM_WR(LED_MEM, i, ((i + (cntr>>4)) & 0x3f) | ((63-((i>>2) & 0x3f))<<8) );
        }
#endif

        while(REG_RD(LED_STREAMER_STATUS) == 1)
            ;

        cntr += 1;
    }

    while(0){
        REG_WR(LED_WRITE, 0x00);
        REG_WR(LED_MEM, 0x01);
        wait(WAIT_CYCLES);
        REG_WR(LED_MEM, 0x00);

        REG_WR(LED_WRITE, 0x02);
        wait(WAIT_CYCLES);
        REG_WR(LED_WRITE, 0x04);
        wait(WAIT_CYCLES);
    }

    while(1);
}

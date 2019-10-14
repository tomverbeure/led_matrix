#include <stdint.h>
#include <math.h>

#include "reg.h"
#include "top_defines.h"

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

    for(int i=0;i<64;++i){
        MEM_WR(LED_MEM, i, i);
    }

    int cntr = 0;

    while(1){
        REG_WR(LED_STREAMER_CONFIG, 1);
        REG_WR(LED_STREAMER_CONFIG, 0);

        for(int i=0;i<64;++i){
            MEM_WR(LED_MEM, i, (i + (cntr>>5)) & 0x3f);
        }

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

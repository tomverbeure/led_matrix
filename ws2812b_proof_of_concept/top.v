
module top(
    input       osc_clk,
    output reg  led
);

    localparam  osc_clk_mhz     = 50;
    localparam  nr_leds         = 64;

    localparam  led_t0l_ns      = 850;
    localparam  led_t0h_ns      = 400;

    localparam  led_t1l_ns      = 450;
    localparam  led_t1h_ns      = 800;

    /*
    localparam  led_t0l_ns      = 800;
    localparam  led_t0h_ns      = 350;

    localparam  led_t1l_ns      = 600;
    localparam  led_t1h_ns      = 700;
    */


    localparam  res_ns          = 50000;

    localparam  led_t0l_cyc      = led_t0l_ns * osc_clk_mhz / 1000;
    localparam  led_t0h_cyc      = led_t1l_ns * osc_clk_mhz / 1000;

    localparam  led_t1l_cyc      = led_t1l_ns * osc_clk_mhz / 1000;
    localparam  led_t1h_cyc      = led_t1h_ns * osc_clk_mhz / 1000;

    localparam  res_cyc          = res_ns * osc_clk_mhz / 1000;

    reg reset_;

    initial reset_ = 1'b0;

    always @(posedge osc_clk) begin
        reset_  <= 1'b1;
    end

    wire [7:0] led_r;
    wire [7:0] led_g;
    wire [7:0] led_b;

    localparam FSM_IDLE             = 0;     
    localparam FSM_LOAD_LED_VAL     = 1;     
    localparam FSM_SHIFT_LED_TH     = 2;     
    localparam FSM_SHIFT_LED_TL     = 3;     
    localparam FSM_LED_RESET        = 4;     

    assign led_r        = led_cntr << 2;
    assign led_g        = ~(led_cntr << 2);
    assign led_b        = led_cntr;

    wire [23:0] led_val = { led_g, led_r, led_b };

    reg [2:0] cur_state, nxt_state;

    reg        led_nxt;
    reg [7:0]  led_cntr, led_cntr_nxt;
    reg [5:0]  bit_cntr, bit_cntr_nxt;
    reg [12:0] t_cntr, t_cntr_nxt;
    reg [23:0] led_shift, led_shift_nxt;

    always @(*) begin
        nxt_state   = cur_state;

        led_nxt         = led;
        led_cntr_nxt    = led_cntr;
        bit_cntr_nxt    = bit_cntr;
        t_cntr_nxt      = t_cntr;
        led_shift_nxt   = led_shift;


        case(cur_state) 
            FSM_IDLE: begin
                led_cntr_nxt    = nr_leds-1;
                nxt_state       = FSM_LOAD_LED_VAL;
            end
            FSM_LOAD_LED_VAL: begin
                led_shift_nxt   = led_val;
                bit_cntr_nxt    = 23;
                t_cntr_nxt      = 0;
                nxt_state       = FSM_SHIFT_LED_TH;
            end
            FSM_SHIFT_LED_TH: begin
                led_nxt         = 1'b1;
                t_cntr_nxt      = t_cntr + 1;

                if ((led_shift[23] && t_cntr == led_t1h_cyc) || (!led_shift[23] && t_cntr == led_t0h_cyc)) begin
                    t_cntr_nxt  = 0;
                    nxt_state   = FSM_SHIFT_LED_TL;
                end
            end
            FSM_SHIFT_LED_TL: begin
                led_nxt         = 1'b0;
                t_cntr_nxt      = t_cntr + 1;

                if ((led_shift[23] && t_cntr == led_t1l_cyc) || (!led_shift[23] && t_cntr == led_t0l_cyc)) begin
                    t_cntr_nxt  = 0;

                    if (bit_cntr != 0) begin
                        bit_cntr_nxt    = bit_cntr - 1;
                        led_shift_nxt   = {led_shift[22:0], 1'b0};

                        nxt_state   = FSM_SHIFT_LED_TH;
                    end
                    else if (led_cntr != 0) begin
                        led_cntr_nxt    = led_cntr - 1;

                        nxt_state   = FSM_LOAD_LED_VAL;
                    end
                    else begin
                        t_cntr_nxt  = 0;
                        nxt_state   = FSM_LED_RESET;
                    end
                end
            end
            FSM_LED_RESET: begin
                t_cntr_nxt  = t_cntr + 1;

                if (t_cntr == res_cyc) begin
                    nxt_state   = FSM_IDLE;
                end
            end

        endcase
    end

    always @(posedge osc_clk) begin
        if (!reset_) begin
            cur_state   <= FSM_IDLE;
        end
        else begin
            cur_state   <= nxt_state;

            led         <= led_nxt;
            led_cntr    <= led_cntr_nxt;
            bit_cntr    <= bit_cntr_nxt;
            t_cntr      <= t_cntr_nxt;
            led_shift   <= led_shift_nxt;
        end
    end

endmodule

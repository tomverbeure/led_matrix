

module tb;

    reg clk;
    wire led;

    initial begin
        $dumpfile("waves.vcd");
        $dumpvars(0);

        clk = 0;

        #1000000;
        $finish;
    end

    always
        #5 clk = !clk;

    top u_top(clk, led);

endmodule

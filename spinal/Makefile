
#sim:
#	sbt "test-only rt.PanoTester"

.PHONY: submodule
submodule:
	cd ../ && git submodule update --init

syn: submodule
	sbt "runMain led_matrix.LedMatrixTopVerilog"


waves:
	gtkwave -o simWorkspace/PanoCoreDut/test.vcd &
    

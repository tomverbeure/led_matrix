
NEXTPNR_PARAMS = --up5k --package sg48 --json chip.json --pcf upduino_v2.pcf --asc chip.syn.asc --freq 6

HEX2BIN = ../misc/hex2bin.py

all: chip.bin


#============================================================
# Fake ROM files for icestorm init replacement flow
#============================================================

ROM_FILES_SYN = LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol0.bin \
			    LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol1.bin \
			    LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol2.bin \
			    LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol3.bin

LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol0.bin: LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol0.hex
	$(HEX2BIN) < $< > $@

LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol1.bin: LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol1.hex
	$(HEX2BIN) < $< > $@

LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol2.bin: LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol2.hex
	$(HEX2BIN) < $< > $@

LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol3.bin: LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol3.hex
	$(HEX2BIN) < $< > $@

LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol0.hex \
LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol1.hex \
LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol2.hex \
LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol3.hex:
	icebram -g 8 1024 > $@

#============================================================
# Final ROM files
#============================================================

ROM_FILES = LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol0.final.hex \
			LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol1.final.hex \
			LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol2.final.hex \
			LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol3.final.hex 

LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol0.final.hex: ../sw/progmem.bin
	../misc/create_mif.rb  -f hex -o 0 -i 4 $< > $@

LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol1.final.hex: ../sw/progmem.bin
	../misc/create_mif.rb  -f hex -o 1 -i 4 $< > $@

LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol2.final.hex: ../sw/progmem.bin
	../misc/create_mif.rb  -f hex -o 2 -i 4 $< > $@

LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol3.final.hex: ../sw/progmem.bin
	../misc/create_mif.rb  -f hex -o 3 -i 4 $< > $@

#============================================================

chip.bin: chip.asc
	icepack $< $@

chip.asc: chip.syn.asc $(ROM_FILES)
	icebram LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol0.hex LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol0.final.hex  < $< | \
	icebram LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol1.hex LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol1.final.hex       | \
	icebram LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol2.hex LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol2.final.hex       | \
	icebram LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol3.hex LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol3.final.hex       > $@

chip.syn.asc: chip.json upduino_v2.pcf
	nextpnr-ice40 $(NEXTPNR_PARAMS)

chip.json: ../spinal/LedMatrixTop.v $(ROM_FILES_SYN)
	yosys -q -p "synth_ice40 -json $@" $<

.PHONY: flash
flash:
	iceprog chip.bin

gui: chip.json upduino_v2.pcf
	nextpnr-ice40 $(NEXTPNR_PARAMS) --gui

.PHONY: clean
clean:
	$(RM) -f chip.json *.asc *.bin *.hex

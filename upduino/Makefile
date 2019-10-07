
ROM_FILES = ../spinal/LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol0.bin \
			../spinal/LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol1.bin \
			../spinal/LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol2.bin \
		   	../spinal/LedMatrixTop.v_toplevel_core_u_cpu_u_cpu_ram_ram_symbol3.bin

NEXTPNR_PARAMS = --up5k --package sg48 --json chip.json --pcf upduino_v2.pcf --asc chip.asc --freq 6

chip.bin: chip.asc
	icepack $< $@

chip.asc: chip.json upduino_v2.pcf
	nextpnr-ice40 $(NEXTPNR_PARAMS)

chip.json: ../spinal/LedMatrixTop.v $(ROM_FILES)
	cp $(ROM_FILES) .
	yosys -q -p "synth_ice40 -json $@" $<

.PHONY: flash
flash:
	iceprog chip.bin

gui: chip.json upduino_v2.pcf
	nextpnr-ice40 $(NEXTPNR_PARAMS) --gui

.PHONY: clean
clean:
	$(RM) -f chip.json chip.asc chip.bin

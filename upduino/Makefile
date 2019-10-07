

NEXTPNR_PARAMS = --up5k --package sg48 --json chip.json --pcf upduino_v2.pcf --asc chip.asc --freq 6

chip.bin: chip.asc
	icepack $< $@

chip.asc: chip.json upduino_v2.pcf
	nextpnr-ice40 $(NEXTPNR_PARAMS)

chip.json: ../spinal/LedMatrixTop.v
	yosys -q -p "synth_ice40 -json $@" $<

.PHONY: flash
flash:
	iceprog chip.bin

gui: chip.json upduino_v2.pcf
	nextpnr-ice40 $(NEXTPNR_PARAMS) --gui

.PHONY: clean
clean:
	$(RM) -f chip.json chip.asc chip.bin

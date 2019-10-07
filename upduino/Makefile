
chip.bin: chip.asc
	icepack $< $@

chip.asc: chip.json upduino_v2.pcf
	nextpnr-ice40 --up5k --package sg48 --json chip.json --pcf upduino_v2.pcf --asc chip.asc   # run place and route

chip.json: ../spinal/LedMatrixTop.v
	yosys -q -p "synth_ice40 -json $@" $<

.PHONY: flash
flash:
	iceprog chip.bin

.PHONY: gui
gui:
	nextpnr-ice40 --up5k --package sg48 --json chip.json --pcf upduino_v2.pcf --asc chip.asc --gui   # run place and route

.PHONY: clean
clean:
	$(RM) -f chip.json chip.asc chip.bin

module riscv_soc(
    input   wire  clk,
    input   wire  rst_n
);

wire [31:0] riscv_inst_addr_o;  //riscv2rom
wire [31:0] rom_inst_o;         //rom2riscv

risc_v risc_v_inst(
    .clk        (clk),
    .rst_n      (rst_n),
    .inst_i     (rom_inst_o),
    .inst_addr_o(riscv_inst_addr_o)
);


rom rom_inst(
    .inst_addr_i(riscv_inst_addr_o),
    .inst_o     (rom_inst_o) 
);
endmodule

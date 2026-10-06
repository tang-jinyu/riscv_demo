/*
date:2026.10.4
author:tangjinyu
description:主要对地址和数据进行打拍
*/
`include "defines.v"
module  if_id(

input   wire  clk,
input   wire  rst_n,
input   wire [31:0]     inst_i,
input   wire [31:0]     inst_addr_i,
output  wire [31:0]     inst_addr_o,
output  wire [31:0]     inst_o

);

dff_set#(32) dff1(clk,rst_n,`INST_NOP,inst_i,inst_o);
dff_set#(32) dff2(clk,rst_n,32'b0,inst_addr_i,inst_addr_o);

endmodule 
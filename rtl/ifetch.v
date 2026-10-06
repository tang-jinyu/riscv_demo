/*
date:2026.10.4
author:tangjinyu
description:实现取指模块的功能，一方面从pc端取指令地址，另一方面
将地址给到rom，同时将从rom端接收的指令以及指令地址一并给到if_id模块


*/

module ifetch(
    //from  pc
    input       wire [31:0]     pc_addr_i    ,
    //from  rom
    input       wire [31:0]     rom_inst_i   ,
    //to rom
    output      wire [31:0]     if2rom_addr_o,//指令地址
    //to if_id
    output      wire [31:0]     inst_addr_o  ,
    output      wire [31:0]     inst_o       //指令
);

//pc的值原封不动给到rom，
assign  if2rom_addr_o = pc_addr_i;
assign  inst_addr_o = pc_addr_i;//key:此处是流水线设计的重要习惯：指令和数据成对流动，数据往后走需时刻带上自己的地址
assign  inst_o = rom_inst_i;

endmodule
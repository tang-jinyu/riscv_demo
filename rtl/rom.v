/*
date:2026.10.4
autor:tangjinyu
description:实现指令地址输入以及指令输出
*/

module  rom(
    input      wire [31:0] inst_addr_i,
    output     reg  [31:0] inst_o
);

reg [31:0] rom_mem [0:4095];//12个32位的寄存器综合成一个348bit的rom

always@(*)begin
    inst_o = rom_mem[inst_addr_i>>2];
    //因为在pc按字节来编址，而数组每个元素32位=4字节，所以pc的字节
    //是4，4，8，...但是数组下标是第几条指令：0，1，2，3，4
    //下标= 字节地址➗4（二进制里面右移2位）
end

endmodule
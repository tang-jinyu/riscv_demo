//pc_reg模块

module pc_reg(
    input       wire        clk,
    input       wire        rst_n,
    output      reg [31:0]  pc_o  //32位寄存器
);

always@(posedge clk) begin 
    if(!rst_n)
    pc_o<= 32'b0;
    else
    pc_o  <= pc_o + 3'd4;
end

endmodule
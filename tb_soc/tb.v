module tb;
    reg  clk;
    reg  rst_n;
 
 //时钟相关
    always #10 clk = ~clk;
    initial begin
        rst_n <= 1'b0;
        clk   <= 1'b1;

        #30;
        rst_n <= 1'b1;
    end 

//rom初始化
    initial begin
        $readmemb("D:/2026/riscv/tb/inst_add_test.txt",tb.riscv_soc_inst.rom_inst.rom_mem);
    end

    //验证：①一方面是是否正确的寄存器里面是否放入了正确的数值；另一方面加法这条指令是否正确执行
    initial begin
        while(1)begin
            @(posedge clk)
            $display("x27 register value is %d",tb.riscv_soc_inst.risc_v_inst.regs_inst.regs[27]);
            $display("x28 register value is %d",tb.riscv_soc_inst.risc_v_inst.regs_inst.regs[28]);
            $display("x29 register value is %d",tb.riscv_soc_inst.risc_v_inst.regs_inst.regs[29]);
            $display("========================");
            $display("========================");
    end
    end

    riscv_soc riscv_soc_inst(
    .clk  (clk  ),
    .rst_n(rst_n)
);



endmodule 
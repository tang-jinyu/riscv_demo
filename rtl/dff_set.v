module dff_set#(
    parameter   DW = 32
)(
    input   wire  clk,
    input   wire  rst_n,
    input   wire  [DW-1:0]  set_data,
    input   wire  [DW-1:0]  data_i,
    output  reg   [DW-1:0]  data_o
    
    );

    always@(posedge clk)begin
        if(!rst_n)
        data_o <= set_data;
        else  
        data_o <= data_i;
    end

    endmodule
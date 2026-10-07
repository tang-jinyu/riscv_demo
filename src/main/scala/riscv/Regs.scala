package riscv

import chisel3._

class RegsIO extends Bundle{
    //from id
    val  reg1Raddr  =  Input(UInt(5.W))
    val  reg2Raddr  =  Input(UInt(5.W))
    //to id
    val  reg1Rdata  =  Output(UInt(32.W))
    val  reg2Rdata  =  Output(UInt(32.W))
    //from ex
    val  regWaddr   =  Input(UInt(5.W))
    val  regWdata   =  Input(UInt(32.W))
    val  regWen     =  Input(Bool())
}

/** 通用寄存器堆：32x32位 2读1写
读组合  写时序
*/
class Regs extends Module{
    val io = IO(new RegsIO)
    
    //32个32bit寄存器  复位时全部清0
    //Seq.fill(32)(0.U(32.W)) 是 Scala 生成式：批量造 32 个 0
    val regs = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))
    
    //==========写端口：时序逻辑 对应第三个always=====
    when(io.regWen && io.regWaddr =/= 0.U){
        regs(io.regWaddr) := io.regWdata
    }

    //===========读口：组合逻辑============
    // Mux(cond, a, b) 就是三目运算符 cond ? a : b
    io.reg1Rdata := Mux(io.reg1Raddr === 0.U,
                    0.U,
                    Mux(io.regWen && io.reg1Raddr === io.regWaddr,
                        io.regWdata,
                        regs(io.reg1Raddr)))
io.reg2Rdata := Mux(io.reg2Raddr === 0.U,
                    0.U,
                    Mux(io.regWen && io.reg2Raddr === io.regWaddr,
                        io.regWdata,
                        regs(io.reg2Raddr)))

}


/*
1.VecInit(Seq.fill(32)(0.U(32.W))) 用 Scala 的序列生成器批量造出 32 个零，一次喂给 RegInit
对应Verilog 里 for(i=0;i<31;i=i+1) 的复位循环Verilog 里 for(i=0;i<31;i=i+1) 的复位循环





*/
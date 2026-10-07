package riscv

import chisel3._
import chisel3.util._

class ExIO extends Bundle{
    //from ic_ex
    val     Inst     = Input(UInt(32.W))
    val     InstAddr = Input(UInt(32.W))
    val     Op1      = Input(UInt(32.W))
    val     Op2      = Input(UInt(32.W))
    val     RdAddrIn = Input(UInt(5.W))
    val     RegWen   = Input(Bool())
    //to regs
    val     RdAddrOut= Output(UInt(5.W))
    val     RdData   = Output(UInt(32.W))
    val     RdWen    = Output(Bool())
}

class Ex extends Module{
    val io = IO(new ExIO)

    //=======字段解析===========//
    val opcode  = io.Inst(6,0) 
    val rd      = io.Inst(11,7)
    val func3   = io.Inst(14,12)
    val rs1     = io.Inst(19,15)
    val rs2     = io.Inst(24,20)
    val func7   = io.Inst(31,25)
    val imm     = io.Inst(31,20)

    //=========默认值===========//
    io.RdData   := 0.U(32.W)
    io.RdAddrOut := 0.U(5.W)
    io.RdWen    := false.B

    //==========执行逻辑=========//
    switch(opcode){
        is(Consts.TypeI){
            switch(func3){
                is(Consts.Func3AddI){
                    io.RdData := io.Op1 + io.Op2
                    io.RdAddrOut := io.RdAddrIn  
                    io.RdWen  := true.B
                }
            }
        }
        is(Consts.TypeR){
            switch(func3){
                is(Consts.Func3AddSub){
                    io.RdData    := Mux(func7 === 0.U(7.W), io.Op1 + io.Op2, io.Op1 - io.Op2)
                    io.RdAddrOut := io.RdAddrIn
                    io.RdWen     := true.B
                }
            }
        }
    }

}


/*
1.if else在chisel里面用mux
case用switch 


*/
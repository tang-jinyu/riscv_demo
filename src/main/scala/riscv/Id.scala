package riscv

import chisel3._   // UInt/Module/switch 基础
import chisel3.util._  // switch/is/Cat/Fill/Mux 工具库

/**常量集中地（对应defines.v）*/


class IdIO extends Bundle{
    //from if_id
    val instIn      = Input(UInt(32.W))
    val instAddrIn  = Input(UInt(32.W))
    //from regs
    val rs1Data     = Input(UInt(32.W))
    val rs2Data     = Input(UInt(32.W))
    //to regs
    val rs1Addr     = Output(UInt(5.W))
    val rs2Addr     = Output(UInt(5.W))
    //to id_ex
    val instOut     = Output(UInt(32.W))
    val instAddrOut = Output(UInt(32.W))
    val op1         = Output(UInt(32.W))
    val op2         = Output(UInt(32.W))
    val rdAddr      = Output(UInt(5.W))
    val regWen      = Output(Bool())
}

/**译码器：if_id →(regs,id_ex)
*纯组合逻辑
*/

class Id extends Module{
    val io = IO(new IdIO)

    //============字段解析，对应assign========
    val opcode  = io.instIn(6,0)
    val rd      = io.instIn(11, 7)
    val func3   = io.instIn(14, 12)
    val rs1     = io.instIn(19, 15)
    val rs2     = io.instIn(24, 20)
    val imm     = io.instIn(31, 20)

    //===========默认值=====================//
      io.instOut     := io.instIn           // 透传：指令和地址继续旅行
  io.instAddrOut := io.instAddrIn
  io.rs1Addr     := 0.U(5.W)
  io.rs2Addr     := 0.U(5.W)
  io.op1         := 0.U(32.W)
  io.op2         := 0.U(32.W)
  io.rdAddr      := 0.U(5.W)
  io.regWen      := false.B             // 默认不写寄存器（安全取向）

  //==============译码逻辑===============//
  switch(opcode){
    is(Consts.OpTypeI){
        switch(func3){
            is(Consts.Func3AddSub){
                io.rs1Addr  := rs1
                io.op1      := io.rs1Data    //源操作数1
                io.op2      := Cat(Fill(20,imm(11)),imm) //操作数2 符号扩展
                io.rdAddr   := rd
                io.regWen   := true.B
            }
        }
    }
    is(Consts.OpTypeR){
        switch(func3){
            is(Consts.Func3AddSub){
                io.rs1Addr  := rs1
                io.rs2Addr  := rs2
                io.op1      := io.rs1Data
                io.op2      := io.rs2Data
                io.rdAddr   := rd
                io.regWen   := true.B
            }
        }
    }
  }
}

/*
1.switch/is:对应verilog的case。is遗漏的opcode会落在默认值上
2.cat对应拼接 cat(a,b) = {a,b}
3.Bool 是 1 位信号类型，true.B / false.B 是它的字面量——对应 Verilog 的 1'b1 / 1'b0
4.()负责传参数、元组、位切片
{}负责传参数、元组、位切片


*/
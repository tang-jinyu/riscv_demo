package riscv

import chisel3._

class IdExIO extends Bundle{
    //from id
  val instIn     = Input(UInt(32.W))   // 指令
  val instAddrIn = Input(UInt(32.W))   // 指令地址
  val op1In      = Input(UInt(32.W))   // 操作数 1（寄存器值或立即数）
  val op2In      = Input(UInt(32.W))   // 操作数 2
  val rdAddrIn   = Input(UInt(5.W))    // 目的寄存器地址
  val regWenIn   = Input(Bool())       // 写使能
  // to ex（输出侧）
  val instOut     = Output(UInt(32.W))
  val instAddrOut = Output(UInt(32.W))
  val op1Out      = Output(UInt(32.W))
  val op2Out      = Output(UInt(32.W))
  val rdAddrOut   = Output(UInt(5.W))
  val regWenOut   = Output(Bool())
}

class IdEx extends Module{
    val io = IO(new IdExIO)

     io.instOut     := RegNext(io.instIn, Consts.InstNop)  // dff_set #(32)，复位 NOP
     //"每来一个时钟沿，就把 io.instIn 的值装进一个复位值为 NOP 的寄存器，这个寄存器的输出接到 io.instOut 端口上
     io.instAddrOut := RegNext(io.instAddrIn, 0.U(32.W))   // dff_set #(32)，复位 0
     io.op1Out      := RegNext(io.op1In, 0.U(32.W))        // dff_set #(32)
     io.op2Out      := RegNext(io.op2In, 0.U(32.W))        // dff_set #(32)
     io.rdAddrOut   := RegNext(io.rdAddrIn, 0.U(5.W))      // dff_set #(5)
     io.regWenOut   := RegNext(io.regWenIn, false.B)       // dff_set #(1)，复位无效

}

/*
1.RegNext(输入信号, 复位值)
└─┬─┘ └────┬────┘ └───┬───┘
函数名 每拍要装的数据 复位时被赋予的初值
①它在内部创建了一个寄存器
②每个时钟上升沿，寄存器装入输入的当前值，输出比输入晚一拍


*/
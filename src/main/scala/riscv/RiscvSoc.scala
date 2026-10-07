package riscv

import chisel3._

class RiscvSocIO extends Bundle {
  // 调试观测口：把 ex→regs 的写回总线引到顶层，供仿真监测
  val debugRdWen  = Output(Bool())
  val debugRdAddr = Output(UInt(5.W))
  val debugRdData = Output(UInt(32.W))
}

class RiscvSoc extends Module{
    val io = IO(new RiscvSocIO)

    val riscV = Module(new RiscV)
    val rom   = Module(new Rom)
    rom.io.instAddr := riscV.io.instAddr
    riscV.io.inst   := rom.io.inst

      // 中转一级：riscV 是直接子模块，合法
  io.debugRdWen  := riscV.io.debugRdWen
  io.debugRdAddr := riscV.io.debugRdAddr
  io.debugRdData := riscV.io.debugRdData
}
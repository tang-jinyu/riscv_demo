package riscv

import chisel3._
import circt.stage.ChiselStage

/** 通用 Verilog 导出器
  * 用法: sbt "runMain riscv.GenVerilog 模块名"
  * 例:   sbt "runMain riscv.GenVerilog PcReg"
  */
object GenVerilog extends App {
  val moduleName = args(0)
  val dut = Class.forName(s"riscv.$moduleName")
    .getConstructor().newInstance().asInstanceOf[chisel3.RawModule]
  ChiselStage.emitSystemVerilogFile(dut, Array("--target-dir", "generated"))
}

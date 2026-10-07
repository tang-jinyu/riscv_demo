package riscv

import chisel3._

object Consts {
  // ===== 指令编码（ex 级译码用）=====
  val InstNop     = "h00000013".U(32.W)  // NOP = addi x0, x0, 0（流水线清空/复位填充）
  val TypeI       = "b0010011".U(7.W)    // I 型操作码（OP-IMM：addi 家族）
  val TypeR       = "b0110011".U(7.W)    // R 型操作码（OP：add/sub 家族）
  val Func3AddI   = "b000".U(3.W)        // funct3 = ADDI
  val Func3AddSub = "b000".U(3.W)        // funct3 = ADD/SUB（靠 func7 区分）

  // ===== id 级译码用（沿用 Id.scala 里的定义=====
  val OpTypeI = 1.U(2.W)                 // 操作数类型：I 型（op2 来自立即数）
  val OpTypeR = 2.U(2.W)                 // 操作数类型：R 型（op2 来自寄存器）
}
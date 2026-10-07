package riscv
// scala 强制包名 = 路径名，本文件位于 src/main/scala/riscv/，故包名 riscv

import chisel3._  // 导入 chisel3 的全部公开 API

/** author: tangjinyu  date: 2026/10/7
  * description: pc_reg 的 Chisel 版本（对应 rtl/pc_reg.v）
  * 知识点：Module 隐含 clock/reset；UInt(32.W) 32位无符号类型；
  *         RegInit 带复位值的寄存器；:= 是硬件连线/赋值符
  */

/** PC 寄存器的端口集合（riscv-mini 风格：IO 与模块解耦） */
class PcRegIO extends Bundle {
  val pc = Output(UInt(32.W))
}

/** PC 寄存器：每拍 PC <- PC + 4（RISC-V 指令定长 4 字节，按字节编址） */
class PcReg extends Module {
  val io = IO(new PcRegIO)
  val pcReg = RegInit(0.U(32.W))  // 复位时装 0
  pcReg := pcReg + 4.U            // 定义下一拍的值
  io.pc := pcReg                  // 当前值连到输出
}

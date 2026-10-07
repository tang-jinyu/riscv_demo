package riscv

import chisel3._
import chisel3.simulator.EphemeralSimulator._
import org.scalatest.flatspec.AnyFlatSpec

class PcRegSpec extends AnyFlatSpec {
  "PcReg" should "复位后为0，之后每拍加4" in {
    simulate(new PcReg) { c =>
      c.reset.poke(true.B)     // ① 手动把复位拉高
      c.clock.step(2)          // ② 让复位维持两拍
      c.reset.poke(false.B)    // ③ 释放复位
      c.io.pc.expect(0.U)      // ④ 复位时装入的 0 此刻保持
      c.clock.step()           // ⑤ 释放后的第一个沿：0 + 4
      c.io.pc.expect(4.U)
      c.clock.step(5)
      c.io.pc.expect(24.U)
    }
  }
}
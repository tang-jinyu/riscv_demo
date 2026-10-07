/*package riscv

import chisel3._
import chisel3.simulator.EphemeralSimulator._
import org.scalatest.flatspec.AnyFlatSpec

class RiscvSocSpec extends AnyFlatSpec {

  "RiscvSoc" should "执行 inst_add_test：x27=26, x28=27, x29=53" in {
    simulate(new RiscvSoc) { dut =>
      // 手动复位
      dut.reset.poke(true.B)
      dut.clock.step(2)
      dut.reset.poke(false.B)

      // 逐拍监测写回总线，最多跑 20 拍
            for (_ <- 0 until 20) {
        dut.clock.step(1)
        if (dut.io.debugRdWen.peek().litValue == 1) {      // ← 改成顶层调试口
          val addr = dut.io.debugRdAddr.peek().litValue.toInt
          val data = dut.io.debugRdData.peek().litValue.toInt
          addr match {
            case 27 => assert(data == 26, s"x27 应为 26，实际 $data")
            case 28 => assert(data == 27, s"x28 应为 27，实际 $data")
            case 29 => assert(data == 53, s"x29 应为 53，实际 $data")
            case _  =>
          }
        }
      }
    }
  }
}*/
package riscv

import chisel3._
import chisel3.simulator.EphemeralSimulator._
import org.scalatest.flatspec.AnyFlatSpec
import scala.collection.mutable.Map   // 可变 Map，用于记录写回历史

class RiscvSocSpec extends AnyFlatSpec {

  "RiscvSoc" should "执行 inst_add_test：x27=26, x28=27, x29=53" in {
    simulate(new RiscvSoc) { dut =>
      // === 手动复位 ===
      dut.reset.poke(true.B)
      dut.clock.step(2)
      dut.reset.poke(false.B)

      // 记录写回历史：寄存器号 -> (值, 第几拍写入)
      val writes = Map[Int, (Int, Int)]()

      println("==== 仿真开始：逐拍监测写回总线 ====")

      // === 逐拍监测 ===
      for (i <- 0 until 20) {
        dut.clock.step(1)
        val wen  = dut.io.debugRdWen.peek().litValue == 1
        val addr = dut.io.debugRdAddr.peek().litValue.toInt
        val data = dut.io.debugRdData.peek().litValue.toInt

        if (wen) {
          writes(addr) = (data, i)   // 登记这次写回
          println(f"第 $i%2d 拍: [写回] x$addr%-3d <= $data%-6d (0x$data%08x)")
        } else {
          println(f"第 $i%2d 拍: 无写回")
        }
      }

      // === 汇总打印 ===
      println("\n==== 寄存器最终状态 ====")
      println("寄存器   值(十进制)   值(十六进制)   写入拍数")
      for (reg <- Seq(27, 28, 29)) {
        writes.get(reg) match {
          case Some((data, cycle)) =>
            println(f"x$reg%-6d $data%-12d 0x$data%08x      第 $cycle 拍")
          case None =>
            println(f"x$reg      未被写入！!")
        }
      }

      // === 断言（仍然保留，打印只是给人看，断言才是给机器看的裁判）===
      assert(writes.get(27).map(_._1).contains(26), s"x27 应为 26，实际 ${writes.get(27)}")
      assert(writes.get(28).map(_._1).contains(27), s"x28 应为 27，实际 ${writes.get(28)}")
      assert(writes.get(29).map(_._1).contains(53), s"x29 应为 53，实际 ${writes.get(29)}")

      println("\n==== 全部断言通过 ====")
    }
  }
}
    

    /*
    1.simulate(new RiscvSoc) { dut => ... }：把整机交给仿真器
    花括号里是测试激励——相当于 tb.v 的 initial 块；
    2.dut.clock.step(10)：打 10 个时钟沿
    3.dut.regs.regs(1).expect(1.U)：直窥内部信号——
    第一个 regs 是例化的寄存器堆模块，第二个 regs(1) 是里面的 x1
    
    */
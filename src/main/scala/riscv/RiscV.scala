package riscv

import chisel3._

class RiscVIO extends Bundle{
    val    inst     = Input(UInt(32.W))
    val    instAddr = Output(UInt(32.W))

      // 调试观测口：写回总线引出
  val debugRdWen  = Output(Bool())
  val debugRdAddr = Output(UInt(5.W))
  val debugRdData = Output(UInt(32.W))

}

class RiscV extends Module{
    val    io      = IO(new RiscVIO)

    //===========例化7个模块==========//
    val pcReg = Module(new PcReg)
    val ifetch = Module(new Ifetch)
    val ifId    = Module(new IfId)
    val id      = Module(new Id)
    val regs    = Module(new Regs)
    val idEx    = Module(new IdEx)
    val ex      = Module(new Ex)

    //===========pc_reg → ifetch，ifetch ↔ rom（顶层端口）=====//
    ifetch.io.pcAddr    := pcReg.io.pc
    ifetch.io.romInst := io.inst
    io.instAddr       := ifetch.io.if2romAddr
 // ===== ifetch → if_id（打一拍）=====
  ifId.io.instIn     := ifetch.io.inst
  ifId.io.instAddrIn := ifetch.io.instAddr

  // ===== if_id → id → regs（译码 + 读寄存器）=====
  id.io.instIn     := ifId.io.instOut
  id.io.instAddrIn := ifId.io.instAddrOut
  regs.io.reg1Raddr := id.io.rs1Addr    // id 给出读地址
  regs.io.reg2Raddr := id.io.rs2Addr
  id.io.rs1Data     := regs.io.reg1Rdata // regs 返回读数据
  id.io.rs2Data     := regs.io.reg2Rdata

  // ===== id → id_ex（打一拍）=====
  idEx.io.instIn     := id.io.instOut
  idEx.io.instAddrIn := id.io.instAddrOut
  idEx.io.op1In      := id.io.op1
  idEx.io.op2In      := id.io.op2
  idEx.io.rdAddrIn   := id.io.rdAddr
  idEx.io.regWenIn   := id.io.regWen

  // ===== id_ex → ex（执行）=====
  ex.io.Inst     := idEx.io.instOut
  ex.io.InstAddr := idEx.io.instAddrOut
  ex.io.Op1      := idEx.io.op1Out
  ex.io.Op2      := idEx.io.op2Out
  ex.io.RdAddrIn := idEx.io.rdAddrOut
  ex.io.RegWen   := idEx.io.regWenOut

  // ===== ex → regs（写回）=====
  regs.io.regWaddr := ex.io.RdAddrOut
  regs.io.regWdata := ex.io.RdData
  regs.io.regWen   := ex.io.RdWen

    // 写回总线引出到本模块端口（ex 是直接子模块，合法）
  io.debugRdWen  := ex.io.RdWen
  io.debugRdAddr := ex.io.RdAddrOut
  io.debugRdData := ex.io.RdData

}
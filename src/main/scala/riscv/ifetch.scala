package riscv

import chisel3._

class IfetchIO extends Bundle{
    val pcAddr      = Input(UInt(32.W)) //from pc
    val romInst     = Input(UInt(32.W)) //来自rom的指令
    val if2romAddr  = Output(UInt(32.W)) //给rom的取指地址
    val instAddr    = Output(UInt(32.W)) //给if_id
    val inst        = Output(UInt(32.W)) 
}

class Ifetch extends Module{
    val  io = IO(new IfetchIO)
    io.if2romAddr   := io.pcAddr  
    io.instAddr     := io.pcAddr
    io.inst         := io.romInst
}
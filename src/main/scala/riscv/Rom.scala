/*
author:tangjinyu
date:2026/10/7
description:rom的chisel版

*/


package riscv

import chisel3._

import chisel3.util.experimental.loadMemoryFromFileInline //内存加载工具

class RomIO extends Bundle{
    val instAddr = Input(UInt(32.W))  //指令地址
    val inst     = Output(UInt(32.W)) //指令
}

class Rom extends Module{
    val io = IO(new RomIO)
    
    val romMem = Mem(4096,UInt(32.W)) //存储器
    io.inst    := romMem(io.instAddr(31,2)) //丢掉低两位，给地址同时出数据
    loadMemoryFromFileInline(romMem,"src/test/resources/rom.hex")
    }

    /*
    右移 2 位 = 丢掉低 2 根地址线 = 字节地址换算成指令序号
    
    
    
    
    */
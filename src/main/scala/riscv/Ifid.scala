package riscv

import chisel3._



class IfIdIO extends Bundle {
    val  instIn     = Input(UInt(32.W))//from ifetch inst
    val  instAddrIn = Input(UInt(32.W))//from ifetch inst_addr
    val  instOut    = Output(UInt(32.W)) //to id
    val  instAddrOut= Output(UInt(32.W))  //to id

}

/** 流水线寄存器  if→id
*对应If_id.v,dff_set在chisel里面被吸收为两个regnext
*/
class IfId extends Module{
    val io = IO(new IfIdIO)

    //指令寄存器：复位必须是NOP不是0
    val inst = RegNext(io.instIn,Consts.InstNop)

    //地址寄存器，复位值0即可
    val instAddr = RegNext(io.instAddrIn,0.U(32.W))
    io.instOut   := inst
    io.instAddrOut := instAddr
}

/*
1.object Consts——宏的 Chisel 替代品
verilog的define INST_NOP是预处理期文本替换，没有命名空间 、类型等
object是单例对象  全局唯一一份  集中放常量

2.RegNext是下一拍的复位值；io.instIn第一个参数是次态来源对应data_i 第二个参数对应set_data




*/


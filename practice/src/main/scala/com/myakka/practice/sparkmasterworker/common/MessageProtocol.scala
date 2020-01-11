package com.myakka.practice.sparkmasterworker.common

//样例类, 注册的协议，包含 id ,cpu, ram(内存)
case class RegisterWorkerInfo(id: String, cpu: Int, ram: Int)

//WorkerInfo 是用于保存 worker 信息的对象, 它不在网络传输,他是普通类
//后面会加入扩展内容，比如心跳时间
class WorkerInfo(val id: String, val cpu: Int, val ram: Int) {
  //默认的心跳时间
  var lastHeartBeatTime:Long = System.currentTimeMillis()
}

//如果注册成功后，返回的协议信息,因为不需要属性，因此我直接使用的 case object
//后面直接返回的是 RegisteredWorkerInfo 对象： 类型 RegisteredWorkerInfo$
case object RegisteredWorkerInfo


//worker 在注册成功后，通过定时器，每隔 3s 发送一个消息给自己
//SendHeartBeat
case object SendHeartBeat
//当定时器发送了一个 SendHeartBeat 消息后，worker 发送一个消息
// (HearBeat(id: String))给 Master
case class HeartBeat(id: String)

case class StartTimeOutWorker()
case class RemoveTimeOutWorker()
package com.myakka.practice.yellowchickenserver.common

//ClientMessage 客户端发送给服务器的协议数据(对象)
case class ClientMessage(msg:String)
//ServerMessage 服务端发送给客户端的协议数据(对象)
case class ServerMessage(msg:String)

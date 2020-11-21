package com.myakka.design

/**
 * 单例模式
 */
class Instance{

}

object Singleton{
  /**
   * 添加 lazy 关键字 懒汉模式，反之饿汉模式
   */
  lazy val INSTANCE = new Instance
}
package com.myakka.decorator

/**
 * 装饰器模式:允许向一个现有的对象添加新功能,同时又不改变其结构
 */

trait Shap{
  def draw():Unit
}

class Circle extends Shap{
  override def draw(): Unit = {
    println("Circle # draw")
  }
}

class Rectengle extends Shap{
  override def draw(): Unit = {
    println("Rectengle # draw")
  }
}

object MixinPattern extends App{

  trait ShapeDecorator{ self:Shap =>
    private def setRedBorder(decoratorShap:Shap): Unit = {
      println("Border Color: Red")
    }

    override def draw(): Unit = {
      self.draw()
      setRedBorder(self)
    }
  }

  val circle = new Circle() with ShapeDecorator
  val rectengle = new Rectengle() with ShapeDecorator
}
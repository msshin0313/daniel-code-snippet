package scala

object HelloWorld {
  def main(args: Array[String]) {
    println("Hello, world!")
    println(getMessage())
    val a = new ClassA(2, 2)
    println(a.getA())
    println(a.aa)
  }

  def getMessage(): String = {
    return "Haha"
  }
}

class ClassA(a: Int, b: Int) {
  println(a + b)
  private val aa = a

  def getA(): Int = a
}
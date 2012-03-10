package scala

object HelloWorld {
  def main(args: Array[String]) {
    //println("Hello, world!")
    //println(getMessage())
    //val a = new ClassA(2, 2)
    println(classOf[ClassA].toString)
    val cons = classOf[ClassA].getConstructor(classOf[Int], classOf[Int])
    val a1: ClassA = cons.newInstance(7: java.lang.Integer, 6: java.lang.Integer);
    println(a1.getA())
    //println(a.getA())
    //println(a.aa)
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
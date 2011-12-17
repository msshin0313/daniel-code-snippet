from jython import JavaApp
from java.lang import Object


class A(Object):
  def identifier(self):
    return self.__class__.__name__

class SubClass(JavaApp):
  def printme(self):
    print self.a

#app = JavaApp()
##app.reflectClass(A.__class__)
##print A.__class__.__base__
#print dir(app)
#print app.a
#print app.b
#
#d = SubClass()
#d.printme()


a = JavaApp('100')
a.printme('hello, world')
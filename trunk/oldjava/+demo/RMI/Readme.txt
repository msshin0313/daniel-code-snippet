还要按照tutorial/rmi中关于running的要求才能运行，简言之：

1. rmic需要被remote access的class
2. 启动rmiregistry。需要set classpath=(空)
3. 运行remote object server端的注册程序，要提供policy文件，用java -D...执行
4. 运行client调用，也要提供policy文件

参照tutorial/rmi最后一节的说明
import java.util.logging.*;
import java.io.*;

class Logging {
  
  // 用下面的语句生成一个logger，注意getLogger()中最好使用package.class进行命名。
  private static Logger logger = Logger.getLogger("Logging");
  
  public static void main(String[] args) {
  	// 设置要显示的level，比如设置成warning,info就不显示了
  	logger.setLevel( Level.WARNING );
	// 发出Level.WARNING级的信息
  	logger.warning( "HAHA, devil is here" );
	// 发出Level.INFO级的信息
    logger.info("Logging an INFO-level message");

	// 通常只用上面的就足够了，下面显示较为复杂的应用情况

	// 发出自定义的详细信息
	logger.logp(Level.INFO, "Logging", "main", "Logging an INFO-level message");
	// 增加一个输出到文件的handler，且是xml格式
	logger.addHandler(new FileHandler("LogToFile.xml"));
	// 下面显示的是利用Formatter格式化Handler的输出信息，这里的SimpleFormatter是用来生成普通的文本格式
	FileHandler logFile= new FileHandler("LogToFile2.txt");
    logFile.setFormatter(new SimpleFormatter());
    logger.addHandler(logFile);
    logger.info("A message logged to the file");
	// 设置为不使用logger("")的默认ConsoleHandler，
	logger.setUseParentHandlers(false);
	// 若extends Handler来自定义handler，或者implements Filter来定义比Level更复杂的过滤选项，会用到LogRecord的信息
	// 这里就不做demo了
  }
}
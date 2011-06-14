import java.util.logging.*;
import java.io.*;

class Logging {
  
  // ��������������һ��logger��ע��getLogger()�����ʹ��package.class����������
  private static Logger logger = Logger.getLogger("Logging");
  
  public static void main(String[] args) {
  	// ����Ҫ��ʾ��level���������ó�warning,info�Ͳ���ʾ��
  	logger.setLevel( Level.WARNING );
	// ����Level.WARNING������Ϣ
  	logger.warning( "HAHA, devil is here" );
	// ����Level.INFO������Ϣ
    logger.info("Logging an INFO-level message");

	// ͨ��ֻ������ľ��㹻�ˣ�������ʾ��Ϊ���ӵ�Ӧ�����

	// �����Զ������ϸ��Ϣ
	logger.logp(Level.INFO, "Logging", "main", "Logging an INFO-level message");
	// ����һ��������ļ���handler������xml��ʽ
	logger.addHandler(new FileHandler("LogToFile.xml"));
	// ������ʾ��������Formatter��ʽ��Handler�������Ϣ�������SimpleFormatter������������ͨ���ı���ʽ
	FileHandler logFile= new FileHandler("LogToFile2.txt");
    logFile.setFormatter(new SimpleFormatter());
    logger.addHandler(logFile);
    logger.info("A message logged to the file");
	// ����Ϊ��ʹ��logger("")��Ĭ��ConsoleHandler��
	logger.setUseParentHandlers(false);
	// ��extends Handler���Զ���handler������implements Filter�������Level�����ӵĹ���ѡ����õ�LogRecord����Ϣ
	// ����Ͳ���demo��
  }
}
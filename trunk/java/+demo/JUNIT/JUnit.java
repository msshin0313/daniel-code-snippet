/**
 * @author = cobra
 * Copyright GPL
 */
import junit.framework.*;

class SimpleClass {
	int value1() { return 1; }
	int value2() { return 2; }
}

// JUnit这个类必须是public
public class JUnit extends TestCase {
	private SimpleClass sc = new SimpleClass();	// object being tested
	// this constructor is imperative
	public JUnit( String name ) { super(name); }
	public void test1() {
		System.out.println( "Testing value1()" );
		assertEquals( 1, sc.value1() );
	}
	// 若不以test..开头，则不自动执行
	public void test2() {
		System.out.println( "Testing value2()" );
		assertEquals( 1, sc.value2() );
	}
	
	public static void main( String args[] ) {
		System.out.println( "Starting JUnit..." );
		// 用来启动unit测试
		junit.textui.TestRunner.run( JUnit.class );
	}
}

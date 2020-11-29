import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CUITest extends TestCase {

	public CUI cui;
	
	public CUITest() {
		cui = new CUI();
	}
	
	public void testReadStr() {
		InputStream ist = new ByteArrayInputStream("hello".getBytes());
		OutputStream ost = new ByteArrayOutputStream();
		cui.init(ist, ost);
		String str = cui.readStr();
		assertEquals("hello", str);
	}

	public void testReadInt() {
		InputStream ist = new ByteArrayInputStream("114514".getBytes());
		OutputStream ost = new ByteArrayOutputStream();
		cui.init(ist, ost);
		int i = cui.readInt();
		assertEquals(114514, i);
	}


	public void testYesNo() {
		InputStream ist = new ByteArrayInputStream("y".getBytes());
		OutputStream ost = new ByteArrayOutputStream();
		cui.init(ist, ost);
		InputStream ist2 = new ByteArrayInputStream("n".getBytes());
		cui.init(ist, ost);
		boolean f = cui.yesNo();
		assertEquals(true, f);
		cui.init(ist2, ost);
		boolean f2 = cui.yesNo();
		assertEquals(false, f2);
	}

	public void testReadStrInt() {
		InputStream ist = new ByteArrayInputStream("hello 360".getBytes());
		OutputStream ost = new ByteArrayOutputStream();
		cui.init(ist, ost);
		Pair<String, Integer> p = cui.readStrInt();
		assertEquals("hello", p.getT());
		assertEquals(Integer.valueOf(360), p.getU());
	}

	public void testParseCustomer() {
		InputStream ist = new ByteArrayInputStream("hello 360 do".getBytes());
		OutputStream ost = new ByteArrayOutputStream();
		cui.init(ist, ost);
		Customer c = cui.parseCustomer();
		assertEquals("hello", c.getName());
		assertEquals(360, c.getCustomerID());
		assertEquals("do", c.getPassword());
	}

}

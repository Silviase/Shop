import junit.framework.TestCase;

public class CustomerTest extends TestCase {
	
	Customer c;
	
	public CustomerTest() {
		c = new Customer("test", 0, "testpassword");
	}

	public void testGetCustomerID() {
		assertEquals(c.getCustomerID(), 0);
	}

	public void testGetPassword() {
		assertEquals(c.getPassword(), "testpassword");
	}

	public void testGetName() {
		assertEquals(c.getName(), "test");
	}

	public void testToString() {
		assertEquals(c.toString(), "test 0");
	}

}

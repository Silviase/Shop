import java.io.IOException;
import java.util.HashSet;

import junit.framework.TestCase;

public class MemberListTest extends TestCase {
	
	MemberList ml;
	private final Customer admin = new Customer("Admin", 0, "admin");
	private final Customer guest = new Customer("guest", 1, "guest");
	private final Customer added = new Customer("add"  , 2, "adpas");
	private final Customer not   = new Customer("hoge", 114, "fuga");
	
	public MemberListTest() throws IOException {
		ml = new MemberList();
		ml.init();
	}

	public void testExists() {
		assertEquals(ml.exists(guest), true);
		assertEquals(ml.exists(not), false);
	}

	public void testGetMemberList() {
		HashSet<Customer> hs = ml.getMemberList();
		assertEquals(hs.contains(admin), true);
		assertEquals(hs.contains(guest), true);
		assertEquals(hs.contains(not), false);
		assertEquals(hs.contains(null), false);
	}

	public void testSearchFromID() {
		assertEquals(ml.searchFromID(0), admin);
		assertEquals(ml.searchFromID(1), guest);
		assertEquals(ml.searchFromID(114514), null);
	}

	public void testAddMember() {
		ml.addMember("add", "adpas");
		assertEquals(ml.exists(added), true);
		assertEquals(ml.searchFromID(2), added);
	}

}

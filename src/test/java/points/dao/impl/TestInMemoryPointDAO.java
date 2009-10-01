package points.dao.impl;

import org.junit.After;
import org.junit.Before;

/**
 * A unit test for InMemoryPointDAO that uses TestPointDAO as a Testcase Superclass. 
 */
public class TestInMemoryPointDAO extends TestPointDAO {

	@Before
	public void setUp() throws Exception {
		setPointDAO(new InMemoryPointDAO());
	}

	@After
	public void tearDown() throws Exception {
		setPointDAO(null);
	}
}

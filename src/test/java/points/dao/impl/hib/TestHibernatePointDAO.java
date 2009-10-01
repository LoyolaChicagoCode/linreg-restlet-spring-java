package points.dao.impl.hib;

// http://raibledesigns.com/wiki/Wiki.jsp?page=CreateDAO
// look into DBUnit for populating the DB

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import points.dao.PointDAO;
import points.dao.PointDAOAware;
import points.dao.impl.TestPointDAO;

/**
 * A Spring-supported integration test of HibernatePointDAO that uses
 * TestPointDAO as a test helper. This is the best we can do in terms of reusing
 * the test methods because Java does not support mixins and we already have to
 * extend from the Spring test class. As a consequence, every time we add a test
 * method to TestPointDAO, we have to add a corresponding wrapper test method
 * here.
 */
public class TestHibernatePointDAO extends
		AbstractTransactionalDataSourceSpringContextTests implements
		PointDAOAware {

	private PointDAO dao;

	private TestPointDAO testHelper = new TestPointDAO();

	public void setPointDAO(PointDAO dao) {
		this.dao = dao;
		testHelper.setPointDAO(dao);
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "classpath:applicationContextHibernateDAO.xml",
				"classpath:applicationContextBasicDataSource.xml" };
	}

	@Override
	protected void onSetUpInTransaction() throws Exception {
		dao.init();
		// System.out.println("before OSUIT: " + dao.findAll());
		// deleteFromTables(new String[] { "POINTS", "COLORS" });
		// System.out.println("after OSUIT: " + dao.findAll());
	}

	public void testEmpty() {
		testHelper.testEmpty();
	}

	public void testInit() {
		testHelper.testInit();
	}

	public void testColors() {
		testHelper.testColors();
	}

	public void testCreateFind() {
		testHelper.testCreateFind();
	}

	public void testCreateRemove() {
		testHelper.testCreateRemove();
	}

	public void testCreateUpdate() {
		testHelper.testCreateUpdate();
	}

	public void testCreateMultiple() {
		testHelper.testCreateMultiple();
	}

	public void testCreateRemoveMultiple() {
		testHelper.testCreateRemoveMultiple();
	}
}

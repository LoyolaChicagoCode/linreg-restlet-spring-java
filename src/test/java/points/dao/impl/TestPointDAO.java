package points.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Ignore;
import org.junit.Test;

import points.dao.PointDAO;
import points.dao.PointDAOAware;
import points.domain.Point;

/**
 * A Testcase Superclass (see xUnit Patterns) for testing a PointDAO. Also
 * serves as a Test Helper for test cases such as TestHibernatePointDAO that
 * want to reuse these test methods but have to extend one of the Spring support
 * classes. We use the Ignore annotation so that JUnit does not try to execute
 * this class as a test case.
 */
@Ignore
public class TestPointDAO implements PointDAOAware {

	private PointDAO dao;

	public void setPointDAO(PointDAO dao) {
		this.dao = dao;
	}

	@Test
	public void testEmpty() {
		assertTrue(dao.findAll().isEmpty());
	}

	@Test
	public void testInit() {
		dao.init();
		assertTrue(dao.findAll().isEmpty());
	}

	@Test
	public void testColors() {
		Collection<Color> colors = dao.findColors();
		assertEquals(3, colors.size());
		assertTrue(colors.contains(Color.RED));
		assertTrue(colors.contains(Color.BLUE));
		assertTrue(colors.contains(Color.GREEN));
	}

	@Test
	public void testCreateFind() {
		int size = dao.findAll().size();
		Point p = dao.create(2, 3, Color.RED);
		assertEquals(size + 1, dao.findAll().size());
		assertEquals(p.getId(), dao.find(p.getId()).getId());
		assertTrue(dao.findAll().contains(p));
	}

	@Test
	public void testCreateRemove() {
		int size = dao.findAll().size();
		Point p = dao.create(2, 3, Color.RED);
		assertEquals(size + 1, dao.findAll().size());
		assertEquals(p.getId(), dao.find(p.getId()).getId());
		assertTrue(dao.findAll().contains(p));
		dao.remove(p.getId());
		assertEquals(size, dao.findAll().size());
		assertTrue(dao.find(p.getId()) == null);
		assertFalse(dao.findAll().contains(p));
	}

	@Test
	public void testCreateUpdate() {
		int size = dao.findAll().size();
		Point p = dao.create(2, 3, Color.RED);
		assertEquals(size + 1, dao.findAll().size());
		assertEquals(p.getId(), dao.find(p.getId()).getId());
		assertTrue(dao.findAll().contains(p));
		dao.update(p.getId(), 4, 5, Color.GREEN);
		Point q = dao.find(p.getId());
		assertEquals(p.getId(), q.getId());
	}

	@Test
	public void testCreateMultiple() {
		Collection<Point> points = new HashSet<Point>(dao.findAll());
		int size = points.size();
		Point p1 = dao.create(2, 3, Color.RED);
		Point p2 = dao.create(3, 4, Color.BLUE);
		Point p3 = dao.create(4, 5, Color.GREEN);
		assertTrue(p1.getId() != p2.getId());
		assertTrue(p2.getId() != p3.getId());
		assertTrue(p1.getId() != p3.getId());
		assertEquals(size + 3, dao.findAll().size());
		for (Point p : new Point[] { p1, p2, p3 }) {
			assertEquals(p.getId(), dao.find(p.getId()).getId());
			assertTrue(dao.findAll().contains(p));
			assertFalse(points.contains(p));
		}
	}

	@Test
	public void testCreateRemoveMultiple() {
		Collection<Point> points = new HashSet<Point>(dao.findAll());
		int size = points.size();
		Point p1 = dao.create(2, 3, Color.RED);
		Point p2 = dao.create(3, 4, Color.BLUE);
		Point p3 = dao.create(4, 5, Color.GREEN);
		assertTrue(p1.getId() != p2.getId());
		assertTrue(p2.getId() != p3.getId());
		assertTrue(p1.getId() != p3.getId());
		assertEquals(size + 3, dao.findAll().size());
		for (Point p : new Point[] { p1, p2, p3 }) {
			assertEquals(p.getId(), dao.find(p.getId()).getId());
			assertTrue(dao.findAll().contains(p));
			assertFalse(points.contains(p));
		}
		for (Point p : new Point[] { p1, p2, p3 }) {
			dao.remove(p.getId());
		}
		assertTrue(points.containsAll(dao.findAll()));
		assertTrue(dao.findAll().containsAll(points));
	}
}

package points.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import points.domain.Point;
import points.service.RegressionResult;
import points.service.RegressionService;
import points.service.RegressionServiceAware;

/**
 * A Testcase Superclass (see xUnit Patterns) for testing a RegressionService.
 * Also serves as a Test Helper for test cases such as
 * TestRegressionServiceSpringHibernate that want to reuse these test methods
 * but have to extend one of the Spring support classes. We use the Ignore
 * annotation so that JUnit does not try to execute this class as a test case.
 */
@Ignore
public class TestRegressionService implements RegressionServiceAware {

	private RegressionService service;

	public void setRegressionService(RegressionService service) {
		this.service = service;
	}

	@Test
	public void testEmpty() {
		RegressionResult result = service.getResult();
		assertTrue(result.getPoints().isEmpty());
		assertTrue(Double.isNaN(result.getSlope()));
		assertTrue(Double.isNaN(result.getYIntercept()));
		try {
			result.getPoints().clear();
			fail("RegressionResult.getPoints() " + "should be read-only");
		} catch (Throwable ex) {
		}
	}

	@Test
	public void testColors() {
		Collection<Color> colors = service.getColors();
		assertEquals(3, colors.size());
		assertTrue(colors.contains(Color.RED));
		assertTrue(colors.contains(Color.BLUE));
		assertTrue(colors.contains(Color.GREEN));
	}

	@Test
	public void testAddOne() {
		service.addPoint(1, 2, Color.RED);
		RegressionResult result = service.getResult();
		assertEquals(1, result.getPoints().size());
		Point p = result.getPoints().iterator().next();
		assertEquals(1.0, p.getX(), Double.MIN_NORMAL);
		assertEquals(2.0, p.getY(), Double.MIN_NORMAL);
		assertEquals(Color.RED, p.getColor());
		assertTrue(Double.isNaN(result.getSlope()));
		assertTrue(Double.isNaN(result.getYIntercept()));
	}

	@Test
	public void testAddTwo() {
		service.addPoint(1, 3, Color.RED);
		service.addPoint(2, 4, Color.BLUE);
		RegressionResult result = service.getResult();
		assertEquals(2, result.getPoints().size());
		assertEquals(2.0, result.getYIntercept(), Double.MIN_NORMAL);
		assertEquals(1.0, result.getSlope(), Double.MIN_NORMAL);
	}

	@Test
	public void testAddRemoveOne() {
		service.addPoint(1, 2, Color.RED);
		RegressionResult r1 = service.getResult();
		assertEquals(1, r1.getPoints().size());
		Point p = r1.getPoints().iterator().next();
		int id = p.getId();
		service.removePoint(id);
		RegressionResult r2 = service.getResult();
		assertEquals(0, r2.getPoints().size());
	}

	@Test
	public void testAddUpdateOne() {
		service.addPoint(1, 2, Color.RED);
		RegressionResult r1 = service.getResult();
		assertEquals(1, r1.getPoints().size());
		Point p1 = r1.getPoints().iterator().next();
		int id1 = p1.getId();
		service.editPoint(id1, 3, 4, Color.GREEN);
		RegressionResult r2 = service.getResult();
		assertEquals(1, r2.getPoints().size());
		Point p2 = r2.getPoints().iterator().next();
		int id2 = p2.getId();
		assertEquals(id1, id2);
	}

	@Test
	public void testAddRemoveMultiple() {
		service.addPoint(1, 2, Color.RED);
		service.addPoint(3, 4, Color.RED);
		service.addPoint(4, 5, Color.RED);
		RegressionResult r1 = service.getResult();
		assertEquals(3, r1.getPoints().size());
		for (Point p : new ArrayList<Point>(r1.getPoints())) {
			service.removePoint(p.getId());
		}
		RegressionResult r2 = service.getResult();
		assertEquals(0, r2.getPoints().size());
	}

	@Test
	public void testReset() {
		service.addPoint(1, 2, Color.RED);
		service.addPoint(3, 4, Color.RED);
		service.addPoint(4, 5, Color.RED);
		RegressionResult r1 = service.getResult();
		assertEquals(3, r1.getPoints().size());
		service.reset();
		RegressionResult r2 = service.getResult();
		assertEquals(0, r2.getPoints().size());
	}
}

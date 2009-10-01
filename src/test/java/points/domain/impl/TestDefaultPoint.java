package points.domain.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Test;

import points.domain.Point;

public class TestDefaultPoint {

	@Test
	public void testConstructor() {
		Point p = new DefaultPoint(1, 2, Color.GREEN);
		assertEquals(1.0, p.getX(), Double.MIN_NORMAL);
		assertEquals(2.0, p.getY(), Double.MIN_NORMAL);
		assertEquals(Color.GREEN, p.getColor());
	}

	@Test
	public void testClone() {
		DefaultPoint q = new DefaultPoint(1, 2, Color.GREEN);
		Point p = q.clone();
		assertFalse(p == q);
		assertEquals(q, p);
		assertEquals(1.0, p.getX(), Double.MIN_NORMAL);
		assertEquals(2.0, p.getY(), Double.MIN_NORMAL);
		assertEquals(Color.GREEN, p.getColor());
	}

	@Test
	public void testSetId() {
		DefaultPoint p = new DefaultPoint(0, 0, Color.BLUE);
		p.setId(77);
		assertEquals(77, p.getId());
	}

	@Test
	public void testSetX() {
		DefaultPoint p = new DefaultPoint(0, 0, Color.BLUE);
		p.setX(7);
		assertEquals(7, p.getX(), 0);
	}

	@Test
	public void testSetY() {
		DefaultPoint p = new DefaultPoint(0, 0, Color.BLUE);
		p.setY(7);
		assertEquals(7, p.getY(), 0);
	}

	@Test
	public void testSetColor() {
		DefaultPoint p = new DefaultPoint(0, 0, Color.BLUE);
		p.setColor(Color.RED);
		assertEquals(Color.RED, p.getColor());
		assertEquals(Color.RED, p.getColor());
	}

	@Test
	public void testEquals() {
		DefaultPoint p1 = new DefaultPoint(1, 2, Color.BLUE);
		DefaultPoint p2 = new DefaultPoint(1, 2, Color.RED);
		DefaultPoint p3 = new DefaultPoint(0, 1, Color.BLUE);
		p1.setId(77);
		p2.setId(77);
		p3.setId(88);
		assertTrue(p1.equals(p1));
		assertTrue(p1.equals(p2));
		assertFalse(p1.equals(p3));
		assertFalse(p1.equals(null));
	}

	@Test
	public void testHashCode() {
		DefaultPoint p1 = new DefaultPoint(0, 0, Color.BLUE);
		DefaultPoint p2 = new DefaultPoint(0, 0, Color.BLUE);
		DefaultPoint p3 = new DefaultPoint(0, 0, Color.BLUE);
		p1.setId(77);
		p2.setId(77);
		p3.setId(88);
		assertTrue(p1.hashCode() == p2.hashCode());
		assertTrue(p1.hashCode() != p3.hashCode());
	}

	@Test
	public void testCompareTo() {
		DefaultPoint p1 = new DefaultPoint(1, 2, Color.BLUE);
		DefaultPoint p2 = new DefaultPoint(1, 2, Color.BLUE);
		DefaultPoint p3 = new DefaultPoint(1, 2, Color.BLUE);
		DefaultPoint p4 = new DefaultPoint(1, 2, Color.RED);
		DefaultPoint p5 = new DefaultPoint(1, 3, Color.RED);
		DefaultPoint p6 = new DefaultPoint(3, 4, Color.BLUE);
		p3.setId(77);
		assertTrue(p1.compareTo(p1) == 0);
		assertTrue(p1.compareTo(p2) == 0);
		assertTrue(p1.compareTo(p3) < 0);
		assertTrue(p3.compareTo(p1) > 0);
		assertTrue(p1.compareTo(p4) < 0);
		assertTrue(p4.compareTo(p1) > 0);
		assertTrue(p4.compareTo(p5) < 0);
		assertTrue(p5.compareTo(p4) > 0);
		assertTrue(p4.compareTo(p6) < 0);
		assertTrue(p6.compareTo(p4) > 0);
	}
}

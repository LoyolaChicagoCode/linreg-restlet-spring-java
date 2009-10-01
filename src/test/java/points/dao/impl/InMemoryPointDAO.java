package points.dao.impl;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import points.dao.PointDAO;
import points.domain.Point;
import points.domain.impl.DefaultPoint;

/**
 * An in-memory stub implementation of PointDAO for testing the upper
 * tiers without using a persistent data source. 
 */
public class InMemoryPointDAO implements PointDAO {

	private Map<Integer, Point> map = new HashMap<Integer, Point>();

	private int currentId = 0;

	@Override
	public synchronized Point create(double x, double y, Color color) {
		++currentId;
		DefaultPoint p = new DefaultPoint(x, y, color);
		p.setId(currentId);
		map.put(currentId, p);
		return p;
	}

	@Override
	public synchronized Point find(int id) {
		return map.get(id);
	}

	@Override
	public synchronized Collection<Point> findAll() {
		return Collections.unmodifiableCollection(map.values());
	}

	@Override
	public synchronized void init() {
		map.clear();
	}

	@Override
	public synchronized void remove(int id) {
		map.remove(id);
	}

	@Override
	public synchronized void update(int id, double x, double y, Color color) {
		DefaultPoint p = (DefaultPoint) map.get(id);
		p.setX(x);
		p.setY(y);
		p.setColor(color);
	}
	
	@Override
	public Collection<Color> findColors() {
		return Arrays.asList(new Color[] { Color.RED, Color.GREEN, Color.BLUE });
	}
}

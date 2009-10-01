package points.dao.impl.db4o;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import points.dao.PointDAO;
import points.domain.Point;
import points.domain.impl.DefaultPoint;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

public class Db4oPointDAO implements PointDAO {

	private ObjectContainer container;

	/**
	 * Returns the database container.
	 *
	 * @return the database container.
	 */
	protected ObjectContainer getContainer() {
		return container;
	}

	/**
	 * Injects the database container.
	 *
	 * @param container
	 *            the database container.
	 */
	public void setContainer(ObjectContainer container) {
		this.container = container;
	}

	@Override
	public Point create(double x, double y, Color color) {
		final DefaultPoint point = new DefaultPoint(x, y, color);
		getContainer().set(point);
		point.setId((int) getContainer().ext().getID(point));
		getContainer().set(point);
		return point;
	}

	@Override
	public Point find(final int id) {
		Point result = null;
		// Create the query predicate
		final Predicate<Point> predicate = new Predicate<Point>() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean match(final Point candidate) {
				return id == candidate.getId();
			}
		};
		// Query the database and get the first result
		final List<Point> points = getContainer().query(predicate);
		if ((points != null) && (points.size() > 0)) {
			result = points.get(0);
		}
		return result;
	}

	@Override
	public Collection<Point> findAll() {
		return getContainer().query(Point.class);
	}

	@Override
	public Collection<Color> findColors() {
		return getContainer().query(Color.class);
	}

	@Override
	public void init() {
		final List<Point> points = getContainer().query(Point.class);
		final List<Color> colors = getContainer().query(Color.class);
		for (final Point p : points)
			getContainer().delete(p);
		for (final Color c : colors)
			getContainer().delete(c);
		initDAO();
	}

	public void initDAO() {
		getContainer().set(Color.RED);
		getContainer().set(Color.GREEN);
		getContainer().set(Color.BLUE);
	}

	public void close() {
		getContainer().close();
	}

	@Override
	public void remove(int id) {
		getContainer().delete(find(id));
	}

	@Override
	public void update(final int id, final double x, final double y, final Color color) {
		final DefaultPoint point = (DefaultPoint) find(id);
		point.setX(x);
		point.setY(y);
		point.setColor(color);
		getContainer().set(point);
	}
}

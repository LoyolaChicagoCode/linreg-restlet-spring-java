package points.service.impl;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;

import points.dao.PointDAO;
import points.dao.PointDAOAware;
import points.domain.Point;
import points.service.RegressionResult;
import points.service.RegressionService;

/**
 * A default regression service implementation. This implementation is stateless
 * and, consequently, thread-safe.
 */
public class DefaultRegressionService implements RegressionService, PointDAOAware {

	private PointDAO dao;

	/**
	 * Sets the DAO used by this service object. This method is required for
	 * dependency injection.
	 * 
	 * @param dao
	 *            the DAO
	 */
	@Override
	public void setPointDAO(PointDAO dao) {
		this.dao = dao;
	}

	@Override
	public void reset() {
		dao.init();
	}

	@Override
	public void addPoint(double x, double y, Color color) {
		dao.create(x, y, color);
	}

	@Override
	public Point getPoint(int id) {
		return dao.find(id);
	}

	@Override
	public RegressionResult getResult() {
		final Collection<Point> points = dao.findAll();
		int n = 0;
		double sumxx = 0;
		double sumyy = 0;
		double sumxy = 0;
		double sumx = 0;
		double sumy = 0;

		for (Point p : points) {
			n++;
			double x = p.getX();
			double y = p.getY();
			sumx += x;
			sumy += y;
			sumxx += x * x;
			sumyy += y * y;
			sumxy += x * y;
		}

		double sxx = sumxx - sumx * sumx / n;
		double sxy = sumxy - sumx * sumy / n;
		final double slope = sxy / sxx;
		final double yIntercept = (sumy - slope * sumx) / n;

		return new RegressionResult() {
			public Collection<Point> getPoints() {
				return Collections.unmodifiableCollection(points);
			}

			public double getSlope() {
				return slope;
			}

			public double getYIntercept() {
				return yIntercept;
			}
		};
	}

	@Override
	public void removePoint(int id) {
		dao.remove(id);
	}

	@Override
	public void editPoint(int id, double x, double y, Color color) {
		dao.update(id, x, y, color);
	}
	
	@Override
	public Collection<Color> getColors() {
		return dao.findColors();
	}
}

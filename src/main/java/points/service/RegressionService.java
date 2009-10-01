package points.service;

import java.awt.Color;
import java.util.Collection;

import javax.jws.WebService;

import points.domain.Point;

/**
 * An interface for business service objects that provide linear regression.
 */
@WebService
public interface RegressionService {

	void reset();

	/**
	 * Creates a point object with the given fields.
	 */
	void addPoint(double x, double y, Color color);

	/**
	 * Returns the point object with the given unique id.
	 */
	Point getPoint(int id);

	/**
	 * Returns all point objects.
	 */
	RegressionResult getResult();

	/**
	 * Removes the point object with the given unique id.
	 */
	void removePoint(int id);

	/**
	 * Changes the point object with the given unique key.
	 */
	void editPoint(int id, double x, double y, Color color);

	/**
	 * Returns all known colors.
	 */
	Collection<Color> getColors();
}

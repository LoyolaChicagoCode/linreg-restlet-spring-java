package points.dao;

import java.awt.Color;
import java.util.Collection;

import points.domain.Point;

/**
 * A data access object (DAO) for managing a collection of point entities.
 * Similar to the home interface of an EJB.
 */
public interface PointDAO {
	/**
	 * Initializes the database tables.
	 */
	void init();

	/**
	 * Creates a point object with the given fields.
	 */
	Point create(double x, double y, Color color);

	/**
	 * Finds a point object with the given unique id.
	 */
	Point find(int id);

	/**
	 * Returns all point objects.
	 */
	Collection<Point> findAll();

	/**
	 * Removes the point object with the given unique id.
	 */
	void remove(int id);

	/**
	 * Updates the point object with the given unique key.
	 */
	void update(int id, double x, double y, Color color);

	/**
	 * Returns all known colors.
	 */
	Collection<Color> findColors();
}
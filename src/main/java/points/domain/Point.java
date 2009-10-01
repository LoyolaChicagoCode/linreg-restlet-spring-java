package points.domain;

import java.awt.Color;

/**
 * A simple point interface.
 */
public interface Point extends Comparable<Point> {

	/**
	 * Returns the id of the point.
	 * 
	 * @return the id
	 */
	int getId();

	/**
	 * Returns the x coordinate of the point.
	 * 
	 * @return the x coordinate
	 */
	double getX();

	/**
	 * Returns the y coordinate of the point.
	 * 
	 * @return the y coordinate
	 */
	double getY();

	/**
	 * Returns the color of the point.
	 * 
	 * @return the color
	 */
	Color getColor();
}
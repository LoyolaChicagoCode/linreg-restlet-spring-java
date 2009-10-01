package points.service;

import java.util.Collection;

import points.domain.Point;

/**
 * A transfer object for regression results.
 */
public interface RegressionResult {

	/**
	 * The points involved in the calculation.
	 * 
	 * @return the points
	 */
	Collection<Point> getPoints();

	/**
	 * The slope of the resulting line.
	 * 
	 * @return the slope
	 */
	double getSlope();

	/**
	 * The y-intercept of the resulting line.
	 * 
	 * @return the y-intercept
	 */
	double getYIntercept();
}

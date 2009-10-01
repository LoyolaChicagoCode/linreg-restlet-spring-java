package points.service;

/**
 * An interface for clients of the regression
 * service.
 */
public interface RegressionServiceAware {

	void setRegressionService(
			RegressionService regressionService);
}

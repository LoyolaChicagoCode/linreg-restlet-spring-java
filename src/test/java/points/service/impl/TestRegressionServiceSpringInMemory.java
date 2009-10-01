package points.service.impl;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import points.service.RegressionService;
import points.service.RegressionServiceAware;

/**
 * A Spring-supported integration test of DefaultRegressionService that uses
 * TestRegressionService as a test helper. This is the best we can do in terms
 * of reusing the test methods because Java does not support mixins and we
 * already have to extend from the Spring test class. As a consequence, every
 * time we add a test method to TestRegressionService, we have to add a
 * corresponding wrapper test method here.
 */
public class TestRegressionServiceSpringInMemory extends
		AbstractDependencyInjectionSpringContextTests implements
		RegressionServiceAware {

	private RegressionService service;

	private TestRegressionService testHelper = new TestRegressionService();

	public void setRegressionService(RegressionService service) {
		this.service = service;
		testHelper.setRegressionService(service);
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "classpath:applicationContextService.xml",
				"classpath:applicationContextInMemoryDAO.xml" };
	}

	@Override
	public void onSetUp() {
		service.reset();
	}

	@Override
	public void onTearDown() {
		service = null;
	}

	public void testEmpty() {
		testHelper.testEmpty();
	}

	public void testColors() {
		testHelper.testColors();
	}

	public void testAddOne() {
		testHelper.testAddOne();
	}

	public void testAddTwo() {
		testHelper.testAddTwo();
	}

	public void testAddRemoveOne() {
		testHelper.testAddRemoveOne();
	}

	public void testAddUpdateOne() {
		testHelper.testAddUpdateOne();
	}

	public void testAddRemoveMultiple() {
		testHelper.testAddRemoveMultiple();
	}

	public void testReset() {
		testHelper.testReset();
	}
}

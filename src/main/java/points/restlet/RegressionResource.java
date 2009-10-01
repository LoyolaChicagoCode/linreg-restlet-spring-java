package points.restlet;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import points.service.RegressionResult;
import points.service.RegressionService;
import freemarker.template.Configuration;

/**
 * A resource for a single game.
 *
 * GET: guess form for this game.
 *
 * POST: submit and process a guess (returns representation of game depending on
 * game state).
 */
public class RegressionResource extends Resource {

	@Override
	public void init(final Context context, final Request request,
			final Response response) {
		super.init(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_HTML));
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));

		result = getRegressionService().getResult();
		Logger.getRootLogger().info(this + ": slope = " + getResult().getSlope());
		Logger.getRootLogger().info(this + ": y-intercept = " + getResult().getYIntercept());
		Logger.getRootLogger().info(this + ": number of points = " + getResult().getPoints().size());
	}

	private RegressionResult result;

	protected RegressionResult getResult() {
		return result;
	}

	private RegressionService service;

	public void setRegressionService(final RegressionService service) {
		this.service = service;
	}

	public RegressionService getRegressionService() {
		return service;
	}

	/**
	 * The Freemarker configuration for this resource.
	 */
	private Configuration freemarkerConfig;

	/**
	 * Sets the Freemarker configuration for this resource.
	 *
	 * @param freemarkerConfig
	 *            the Freemarker configuration
	 */
	public void setFreemarkerConfig(final Configuration freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restlet.resource.Resource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(final Variant variant) {
		if (variant == null)
			return null;

		if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
			final String NL = System.getProperty("line.separator");
			final StringBuilder sb = new StringBuilder();
			sb.append("regression line");
			sb.append(NL);
			sb.append("slope: ");
			sb.append(getResult().getSlope());
			sb.append(NL);
			sb.append("y-intercept: ");
			sb.append(getResult().getYIntercept());
			sb.append(NL);
			sb.append("points: ");
			sb.append("<a href=\"");
			sb.append(getRequest().getResourceRef().getParentRef());
			sb.append("\">go here</a>");
			return new StringRepresentation(sb, MediaType.TEXT_HTML);
		}

		if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
			final JSONObject json = new JSONObject();
			try {
				json.put("slope", getResult().getSlope());
				json.put("yIntercept", getResult().getYIntercept());
				json.put("points", getRequest().getResourceRef().getParentRef());
			} catch (final JSONException ex) { }
			return new JsonRepresentation(json);
		}

		// TODO change to Freemarker

		return null;
	}
}

package points.restlet;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import points.domain.Point;
import points.service.RegressionResult;
import points.service.RegressionService;
import freemarker.template.Configuration;

/**
 * A resource for initiating new games.
 *
 * GET: welcome page (with button to start game).
 *
 * POST: create and start game (with redirect to representation of the new
 * game).
 */
public class PointsResource extends Resource {

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
	 * @see org.restlet.resource.Resource#init(org.restlet.Context,
	 *      org.restlet.data.Request, org.restlet.data.Response)
	 */
	@Override
	public void init(final Context context, final Request request,
			final Response response) {
		super.init(context, request, response);
		Logger.getRootLogger().info(this + ": init");
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_HTML));
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
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
			final RegressionResult result = getRegressionService().getResult();
			Logger.getRootLogger().info(
					this + ": service result " + result.getPoints());
			sb.append("list of points:");
			sb.append(NL);
			for (final Point p : result.getPoints()) {
				sb.append(p.getId());
				sb.append(NL);
			}
			return new StringRepresentation(sb);
		}

		if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
			final JSONArray json = new JSONArray();
			for (final Point p : getRegressionService().getResult().getPoints()) {
				json.put(PointResource.pointAsJSON(p));
			}
			return new JsonRepresentation(json);
		}

		// TODO change to Freemarker

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restlet.resource.Resource#acceptRepresentation(org.restlet.resource.Representation)
	 */
	@Override
	public void acceptRepresentation(final Representation entity) {
		if (entity.getMediaType().equals(MediaType.APPLICATION_WWW_FORM, true)) {
			// Parse the entity as a web form
			Form form = new Form(entity);
			Logger.getRootLogger().info(this + ": form data " + form);
			final double x = Double.parseDouble(form.getFirstValue("x"));
			final double y = Double.parseDouble(form.getFirstValue("y"));
			final Color color = new Color(Integer.parseInt(form
					.getFirstValue("color")));
			getRegressionService().addPoint(x, y, color);
			getResponse().setStatus(Status.SUCCESS_CREATED);
		} else {
			getResponse().setStatus(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
		}
	}
}

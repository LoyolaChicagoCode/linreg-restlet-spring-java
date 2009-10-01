package points.restlet;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
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
public class PointResource extends Resource {

	@Override
	public void init(final Context context, final Request request,
			final Response response) {
		super.init(context, request, response);
		setModifiable(true);

		getVariants().add(new Variant(MediaType.TEXT_HTML));
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));

		final int id = Integer.parseInt((String) getRequest().getAttributes()
				.get("id"));
		point = service.getPoint(id);
		Logger.getRootLogger().info(this + ": point " + point);
	}

	private Point point;

	/**
	 * Returns the unique ID of this game.
	 *
	 * @return the unique ID of this game
	 */
	protected Point getPoint() {
		return point;
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

		if (getPoint() == null) {
			Logger.getRootLogger().info(this + ": point not found");
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND, "can't find the damn thing");
			return null;
		}

		if (variant.getMediaType().equals(MediaType.TEXT_HTML))
			return new StringRepresentation(getPoint().toString(),
					MediaType.TEXT_HTML);

		if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
			return new JsonRepresentation(pointAsJSON(getPoint()));
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
	public void storeRepresentation(final Representation entity) {
		if (point == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return;
		}

		if (entity.getMediaType().equals(MediaType.APPLICATION_WWW_FORM, true)) {
			// Parse the entity as a web form
			Form form = new Form(entity);
			Logger.getRootLogger().info(this + ": form data " + form);
			final double x = Double.parseDouble(form.getFirstValue("x"));
			final double y = Double.parseDouble(form.getFirstValue("y"));
			final Color color = new Color(Integer.parseInt(form
					.getFirstValue("color")));
			getRegressionService().editPoint(getPoint().getId(), x, y, color);
			getResponse().setStatus(Status.SUCCESS_OK);
		} else {
			getResponse().setStatus(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
		}
	}

	@Override
	public void removeRepresentations() {
		getRegressionService().removePoint(getPoint().getId());
		getResponse().setStatus(Status.SUCCESS_OK);
	}

	public static JSONObject pointAsJSON(final Point p) {
		final JSONObject json = new JSONObject(p);
		json.remove("color");
		try {
			json.put("color", p.getColor().getRGB() & 0xffffff);
		} catch (final JSONException ex) { }
		return json;
	}
}

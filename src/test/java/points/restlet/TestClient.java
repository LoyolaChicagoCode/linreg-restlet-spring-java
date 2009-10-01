package points.restlet;

import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * A test at the Restlet client level. Starts up the service and expects it to
 * be running at the given URI.
 *
 * @author laufer
 */

public class TestClient {

    private final static String APPLICATION_URI = "http://localhost:3000/linear-regression-restlet/";

    @BeforeClass
    public static void beforeClass() throws Exception {
        // load the Spring application context
        ApplicationContext springContext = new ClassPathXmlApplicationContext(
                new String[] { "applicationContextDb4oMemory.xml",
                        "applicationContextDb4oDAO.xml",
                        "applicationContextService.xml",
                        "applicationContextRouter.xml",
                        "applicationContextServer.xml" });

        // obtain the Restlet component from the Spring context and start it
        ((Component) springContext.getBean("top")).start();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEmpty() throws Exception {
        final Response response = new Client(Protocol.HTTP).handle(createJsonRequest(Method.GET, APPLICATION_URI + "points/"));
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        final JSONArray json = new JSONArray(response.getEntity().getText());
        assertEquals(0, json.length());
    }

    @Test
    public void testAdd() throws Exception {
        final Response response = new Client(Protocol.HTTP).handle(createJsonRequest(Method.GET, APPLICATION_URI + "points/"));
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        final int count = new JSONArray(response.getEntity().getText()).length();
        final Form form = new Form();
        form.add("x", "3");
        form.add("y", "4");
        form.add("color", "255");
        final Request request1 = createJsonRequest(Method.POST, APPLICATION_URI + "points/");
        request1.setEntity(form.getWebRepresentation());
        final Response response1 = new Client(Protocol.HTTP).handle(request1);
        assertEquals(Status.SUCCESS_CREATED, response1.getStatus());
        final Response response2 = new Client(Protocol.HTTP).handle(createJsonRequest(Method.GET, APPLICATION_URI + "points/"));
        assertEquals(Status.SUCCESS_OK, response2.getStatus());
        final JSONArray points = new JSONArray(response2.getEntity().getText());
        assertEquals(count + 1, points.length());
        final JSONObject point = points.getJSONObject(0);
        assertEquals(3, point.get("x"));
        assertEquals(4, point.get("y"));
        assertEquals(255, point.get("color"));
    }

    @Test
    public void testAddDelete() throws Exception {
        final Response response = new Client(Protocol.HTTP).handle(createJsonRequest(Method.GET, APPLICATION_URI + "points/"));
        assertEquals(Status.SUCCESS_OK, response.getStatus());
        final int count = new JSONArray(response.getEntity().getText()).length();
        final Form form = new Form();
        form.add("x", "3");
        form.add("y", "4");
        form.add("color", "255");
        final Request request1 = createJsonRequest(Method.POST, APPLICATION_URI + "points/");
        request1.setEntity(form.getWebRepresentation());
        final Response response1 = new Client(Protocol.HTTP).handle(request1);
        assertEquals(Status.SUCCESS_CREATED, response1.getStatus());
        final Response response2 = new Client(Protocol.HTTP).handle(createJsonRequest(Method.GET, APPLICATION_URI + "points/"));
        assertEquals(Status.SUCCESS_OK, response2.getStatus());
        final JSONArray points = new JSONArray(response2.getEntity().getText());
        assertEquals(count + 1, points.length());
        final JSONObject point = points.getJSONObject(0);
        assertEquals(3, point.get("x"));
        assertEquals(4, point.get("y"));
        assertEquals(255, point.get("color"));
        final int id = point.getInt("id");
        final Response response3 = new Client(Protocol.HTTP).handle(createJsonRequest(Method.DELETE, APPLICATION_URI + "points/" + id));
        assertEquals(Status.SUCCESS_OK, response3.getStatus());
        final Response response4 = new Client(Protocol.HTTP).handle(createJsonRequest(Method.GET, APPLICATION_URI + "points/"));
        assertEquals(Status.SUCCESS_OK, response4.getStatus());
        final int count4 = new JSONArray(response4.getEntity().getText()).length();
        assertEquals(count, count4);
    }

    private static Request createJsonRequest(final Method method, final String uri) {
        final Request request = new Request(method, uri);
        request.getClientInfo().setAcceptedMediaTypes(Collections.singletonList(new Preference<MediaType>(MediaType.APPLICATION_JSON)));
        return request;
    }

    /*
     * To be able to see output. Requires invoking service in points.restlet.Main separately.
     */
    public static void main(String... args) throws Exception {
        final Request request = new Request(Method.GET, APPLICATION_URI + "points/");
        request.getClientInfo().setAcceptedMediaTypes(Collections.singletonList(new Preference<MediaType>(MediaType.APPLICATION_JSON)));
        final Response response = new Client(Protocol.HTTP).handle(request);
        System.out.println(response.getStatus().getCode() + ": " + response.getEntity().getMediaType());
    }
}

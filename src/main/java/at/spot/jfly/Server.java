package at.spot.jfly;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import at.spot.jfly.demo.DemoApplication;
import spark.Request;
import spark.Response;
import spark.Spark;

public class Server implements WebSocketListener {

	private final int port;
	private final Class<? extends Window> appClass;

	public Server(final Class<? extends Window> appClass, final int port) {
		this.appClass = appClass;
		this.port = port;
	}

	public void init() {
		Spark.port(port);
		Spark.staticFileLocation("/web");
		Spark.webSocket("/com", ComponentController.instance());
		Spark.get("/", (req, res) -> render(req, res, appClass));
		Spark.exception(Exception.class, (ex, reg, res) -> ex.printStackTrace());
		Spark.init();
	}

	@Override
	public void onWebSocketClose(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWebSocketConnect(Session arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWebSocketError(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWebSocketBinary(byte[] arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWebSocketText(String arg0) {
		// TODO Auto-generated method stub

	}

	protected String render(final Request request, final Response response, final Class<? extends Window> appClass)
			throws InstantiationException, IllegalAccessException {

		Window app = request.session().attribute("application");

		if (app == null) {
			app = appClass.newInstance();

			request.session().attribute("application", app);
		}

		return app.render();
	}

	public static void main(final String[] args) {
		final Server server = new Server(DemoApplication.class, 8080);
		server.init();
	}
}

package io.hbar;

import io.hbar.fx.data.DataManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.impl.JsonObjectMessage;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.platform.Verticle;


public class Main extends Verticle {
	final static Logger logger = LogManager.getLogger(Main.class.getName());
	
	private JsonObject config;
	private DataManager dataManager;
	
	@Override
	public void start() {
		init();
	}
	
	private void startServer() {
		HttpServer server = vertx.createHttpServer();

		setupSockJSBridge(server);
		
		EventBus eb = vertx.eventBus();
		
		Handler<JsonObjectMessage> handler = new Handler<JsonObjectMessage>() {
			@Override
			public void handle(JsonObjectMessage event) {
				JsonObject request = event.body();
				
				logger.info("Incoming data request: " + request.toString());
				
				String symbol = request.getString("symbol");
				String indicator = request.getString("indicator");
				int period = request.getInteger("period");
				int startTime = request.getInteger("startTime");
				int endTime = request.getInteger("endTime");
				JsonObject options = request.getObject("options");
				
				byte[] d = dataManager.getSerialized(symbol, indicator, period, startTime, endTime, options);

				event.reply(d);
			}
		};
		
		eb.registerHandler("data", handler);
		server.listen(8080);
	}
	
	
	private SockJSServer setupSockJSBridge(HttpServer server) {
		JsonObject config = new JsonObject().putString("prefix", "/api").putBoolean("insert_JSESSIONID", false);

		JsonArray permitted = new JsonArray();
		permitted.add(new JsonObject());

		SockJSServer bridge = vertx.createSockJSServer(server).bridge(config, permitted, permitted);
		return bridge;
	}

	private void init() {
		config = container.config();
		startServer();
		dataManager = new DataManager(config.getString("dataFile"));
	}

}

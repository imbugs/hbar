package io.hbar;

import io.hbar.fx.data.SimulationDataManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.impl.JsonObjectMessage;
import org.vertx.java.core.json.JsonObject;


public class SimulationServer extends WebsocketDataServer {
	final static Logger logger = LogManager.getLogger(SimulationServer.class.getName());
	
	@Override
	protected void createDataManager() {
		dataManager = new SimulationDataManager(config.getString("dataFile"));
	}
	
	@Override
	protected void registerHandlers() {
		super.registerDataHandler();
		
		registerTickHandler();
	}

	protected void registerTickHandler() {
		Handler<JsonObjectMessage> handler = new Handler<JsonObjectMessage>() {
			@Override
			public void handle(JsonObjectMessage event) {
				JsonObject request = event.body();
				
				logger.info("Incoming tick request: " + request.toString());
				
				((SimulationDataManager) dataManager).tick();
				
				event.reply("success");
			}
		};
		
		eb.registerHandler("tick", handler);
	}
}

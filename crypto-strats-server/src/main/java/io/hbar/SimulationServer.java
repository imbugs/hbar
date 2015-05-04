package io.hbar;

import io.hbar.fx.data.SimulationDataManager;
import io.hbar.fx.strategy.TestStrategy;

import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.impl.JsonObjectMessage;
import org.vertx.java.core.json.JsonObject;

public class SimulationServer extends WebsocketDataServer {
	final static Logger logger = LogManager.getLogger(SimulationServer.class.getName());

	@Override
	protected void createDataManager() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -48);
		int endTime = (int) (cal.getTimeInMillis() / 1000);

		cal.add(Calendar.DAY_OF_YEAR, -7);
		int startTime = (int) (cal.getTimeInMillis() / 1000);
		
		dataManager = new SimulationDataManager(config.getString("dataFile"), new TestStrategy(), startTime, endTime);
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

				((SimulationDataManager) dataManager).tick(request.getInteger("steps"));

				event.reply("success");
			}
		};

		eb.registerHandler("tick", handler);
	}
}

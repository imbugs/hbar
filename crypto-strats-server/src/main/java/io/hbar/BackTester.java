package io.hbar;

import io.hbar.fx.data.SimulationDataManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BackTester extends SimulationServer {
	final static Logger logger = LogManager.getLogger(BackTester.class.getName());
	
	@Override
	public void start() {
		config = container.config();

		createDataManager();

		backtest();
	}
	
	private void backtest() {
		while(((SimulationDataManager) dataManager).tick(1)) {}
		
		logger.info("done!");
	}
}

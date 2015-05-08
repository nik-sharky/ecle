package org.ploys.ecle.terminal.ui;

import org.ploys.ecle.common.Serial;

public interface PortToolsHandler {
	boolean onConnect(boolean state);

	void onPortChange();

	void onParamsChange();
	
	void onPinChange(Serial.Pin pin, boolean state);

}

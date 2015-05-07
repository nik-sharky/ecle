package org.ploys.ecle.terminal.ui;

import org.ploys.ecle.common.SerialPin;

public interface PortToolsHandler {
	boolean onConnect(boolean state);

	void onPortChange();

	void onParamsChange();
	
	void onPinChange(SerialPin pin, boolean state);

}

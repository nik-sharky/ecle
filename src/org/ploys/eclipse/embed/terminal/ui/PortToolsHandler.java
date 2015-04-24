package org.ploys.eclipse.embed.terminal.ui;

import org.ploys.eclipse.embed.terminal.SerialPin;

public interface PortToolsHandler {
	boolean onConnect(boolean state);

	void onPortChange();

	void onParamsChange();
	
	void onPinChange(SerialPin pin, boolean state);

}

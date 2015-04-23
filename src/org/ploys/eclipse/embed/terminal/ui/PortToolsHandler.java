package org.ploys.eclipse.embed.terminal.ui;

public interface PortToolsHandler {
	void onConnect(boolean state);

	void onPortChange();

	void onParamChange();

}

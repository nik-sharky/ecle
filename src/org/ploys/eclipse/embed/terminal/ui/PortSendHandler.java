package org.ploys.eclipse.embed.terminal.ui;

public interface PortSendHandler {
	void sendData(byte[] data, boolean hdup);
}

package org.ploys.eclipse.embed.terminal;

import jssc.SerialPortEvent;

import org.ploys.eclipse.embed.terminal.ui.PortStatus;

public enum SerialPin {
	DTR, RTS, RXD, TXD, CTS, DCD, DSR, RNG, BREAK, ERROR;

	static SerialPin[] statusPins = { RXD, TXD, CTS, DCD, DSR, RNG, BREAK, ERROR };

	public static SerialPin[] statusValues() {
		return statusPins;
	}

	public static SerialPin fromSerialEventType(int se) {
		switch (se) {
		case SerialPortEvent.CTS:
			return CTS;
		case SerialPortEvent.DSR:
			return DSR;
		case SerialPortEvent.RING:
			return RNG;
		case SerialPortEvent.RLSD:
			return DCD;
		case SerialPortEvent.ERR:
			return ERROR;
		case SerialPortEvent.BREAK:
			return BREAK;
		}

		return null;
	}
}

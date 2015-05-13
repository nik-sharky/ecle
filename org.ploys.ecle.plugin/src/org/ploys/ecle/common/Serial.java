package org.ploys.ecle.common;

import jssc.SerialPortEvent;
import jssc.SerialPortListExt;

public class Serial {
	public static String[] getPortNames() {
		String[] ports = SerialPortListExt.getPortNames();
		//ports = null;
		if (ports == null || ports.length == 0)
			ports = new String[] { "* Click to refresh *" };
		return ports;
	}

	public enum Pin {
		DTR, RTS, RXD, TXD, CTS, DCD, DSR, RNG, BREAK, ERROR;

		static Pin[] statusPins = { RXD, TXD, CTS, DCD, DSR, RNG, BREAK, ERROR };

		public static Pin[] statusValues() {
			return statusPins;
		}

		public static Pin fromSerialEventType(int se) {
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
}

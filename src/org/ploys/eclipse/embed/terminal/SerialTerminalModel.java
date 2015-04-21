package org.ploys.eclipse.embed.terminal;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SerialTerminalModel {
	private String portName;
	private int baudRate = SerialPort.BAUDRATE_9600;
	private int dataBits = SerialPort.DATABITS_8;
	private int stopBits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;
	
	private SerialPort serialPort;

	void status() throws SerialPortException {
		serialPort.addEventListener(new SerialPortEventListener() {			
			@Override
			public void serialEvent(SerialPortEvent e) {
				e.getEventType();
				
			}
		});
				
	}
}

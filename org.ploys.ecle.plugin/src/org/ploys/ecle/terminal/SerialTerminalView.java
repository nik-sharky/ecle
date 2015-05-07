package org.ploys.ecle.terminal;

import javax.annotation.PreDestroy;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.statushandlers.StatusManager;
import org.ploys.ecle.common.SerialPin;
import org.ploys.ecle.terminal.ui.PortMonitor;
import org.ploys.ecle.terminal.ui.PortSend;
import org.ploys.ecle.terminal.ui.PortSendHandler;
import org.ploys.ecle.terminal.ui.PortStatus;
import org.ploys.ecle.terminal.ui.PortTools;
import org.ploys.ecle.terminal.ui.PortToolsHandler;
import org.ploys.ecle.ui.Icons;
import org.ploys.ecle.ui.UI;

public class SerialTerminalView extends ViewPart {
	PortSend sender;
	PortTools portTools;
	PortMonitor monitor;
	PortStatus status;

	private SerialPort serialPort;

	public SerialTerminalView() {
	}

	@PreDestroy
	public void dispose() {
		if (isPortActive()) {
			try {
				disconnect(false);
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
	}

	public void setFocus() {
		// TODO Set the focus to control
	}

	/**
	 * Create contents of the view part.
	 */
	@Override
	public void createPartControl(Composite cParent) {
		createControllers();

		GridLayout gl_cParent = new GridLayout(1, false);
		gl_cParent.verticalSpacing = 0;
		gl_cParent.marginHeight = 0;
		gl_cParent.marginWidth = 0;
		cParent.setLayout(gl_cParent);

		Composite pToolbar = new Composite(cParent, SWT.NONE);
		GridLayout gl_pToolbar = new GridLayout(3, false);
		gl_pToolbar.marginLeft = 28;
		gl_pToolbar.marginTop = 5;
		gl_pToolbar.marginHeight = 0;
		pToolbar.setLayout(gl_pToolbar);
		pToolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		portTools.createUI(pToolbar);

		Composite pMonitor = new Composite(cParent, SWT.NONE);
		pMonitor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_pMonitor = new GridLayout(2, false);
		pMonitor.setLayout(gl_pMonitor);

		monitor.createUI(pMonitor);
		sender.createUI(pMonitor);
		status.createUI(cParent);

		initPort();
	}

	protected void createControllers() {
		monitor = new PortMonitor();
		status = new PortStatus();

		sender = new PortSend(new PortSendHandler() {
			@Override
			public void sendData(byte[] data, boolean hdup) {
				if (hdup) {
					monitor.append(new String(data));
				}

				if (!isPortActive())
					return;

				try {
					setLed(SerialPin.TXD, true);
					serialPort.writeBytes(data);
					setLed(SerialPin.TXD, false);
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
			}
		});

		portTools = new PortTools(new PortToolsHandler() {
			@Override
			public void onPortChange() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onParamsChange() {
				if (isPortActive()) {
					try {
						portTools.setPortParams(serialPort);
						updatePortStatus(true);
					} catch (SerialPortException e) {
						UI.errorDialog(StatusManager.SHOW, "Can't set params", e);
					}
				}
			}

			@Override
			public boolean onConnect(boolean state) {
				try {
					if (state) {
						portTools.disablePortSelect(true);
						connect();
						return true;
					} else {
						disconnect(true);
						portTools.disablePortSelect(false);
						return false;
					}
				} catch (Exception e) {
					UI.errorDialog(StatusManager.SHOW, "Can't connect", e);
					portTools.disablePortSelect(false);
					return false;
				}
			}

			@Override
			public void onPinChange(SerialPin pin, boolean state) {
				if (!isPortActive())
					return;

				try {
					if (pin == SerialPin.RTS)
						serialPort.setRTS(state);

					if (pin == SerialPin.DTR)
						serialPort.setDTR(state);
				} catch (SerialPortException e) {
					UI.errorDialog(StatusManager.SHOW, "Can't set pin", e);
				}
			}
		});
	}

	// region *** Initialization ***
	private int MASK_ALL = SerialPort.MASK_BREAK | SerialPort.MASK_CTS | SerialPort.MASK_DSR | SerialPort.MASK_RING | SerialPort.MASK_ERR
			| SerialPort.MASK_RLSD | SerialPort.MASK_RXCHAR;
	private int[] LINES_OFF = { 0, 0, 0, 0 };

	protected void initPort() {
		String[] ports = SerialPortList.getPortNames();
		portTools.setParams(ports, 115200);
	}

	protected boolean connect() throws SerialPortException {
		disconnect(true);

		serialPort = new SerialPort(portTools.getPort());
		try {
			serialPort.openPort();
			portTools.setPortParams(serialPort);
			serialPort.addEventListener(serialListener, MASK_ALL);

			updatePortStatus(true);
		} catch (SerialPortException e) {
			e.printStackTrace();
			updatePortStatus(false);

			throw (e);
		}

		return true;
	}

	protected void disconnect(boolean updateStatus) throws SerialPortException {
		if (isPortActive()) {
			serialPort.removeEventListener();
			serialPort.closePort();
		}

		if (updateStatus) {
			updatePortStatus(false);
		}
	}

	SerialPortEventListener serialListener = new SerialPortEventListener() {
		@Override
		public void serialEvent(SerialPortEvent e) {
			final int etype = e.getEventType();
			final int evalue = e.getEventValue();

			// System.out.println(e.getEventType());

			switch (etype) {
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RING:
			case SerialPortEvent.RLSD:
			case SerialPortEvent.ERR:
				setLed(SerialPin.fromSerialEventType(etype), evalue != 0);
				break;
			case SerialPortEvent.BREAK:
				setLed(SerialPin.fromSerialEventType(etype), true);
				break;
			case SerialPortEvent.RXCHAR:
			case SerialPortEvent.RXFLAG:
				readData(e.getEventValue());
				break;
			case SerialPortEvent.TXEMPTY:
				break;
			}
		}

	};

	private void readData(int len) {
		try {
			setLed(SerialPin.RXD, true);
			final String data = serialPort.readString(len);
			UI.runAsync(new Runnable() {
				public void run() {
					monitor.append(data);
				}
			});
			setLed(SerialPin.RXD, false);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	// endregion

	boolean isPortActive() {
		return serialPort != null && serialPort.isOpened();
	}

	private void setLed(final SerialPin led, final boolean state) {
		UI.runAsync(new Runnable() {
			public void run() {
				if (!state) {
					// wait a bit before turn-off for show fast-switching leds
					synchronized (this) {
						try {
							wait(30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				status.setLed(led, state);
			}
		});
	}

	private int[] getLinesStatus() {
		if (!isPortActive())
			return LINES_OFF;

		try {
			return serialPort.getLinesStatus();
		} catch (SerialPortException e) {
			return LINES_OFF;
		}
	}

	private void updatePortStatus(final boolean opened) {
		final String st = opened ? "Port: " + portTools.getStatus() : "Port closed";
		final int[] lines = getLinesStatus();

		UI.runAsync(new Runnable() {
			public void run() {
				setPartName(opened ? "Serial terminal " + portTools.getPort() : "Serial terminal");
				setTitleImage(Icons.ico(opened ? "terminal-on" : "terminal"));

				status.resetLeds();
				status.setPortStatus(st, opened);
				status.setLinesStatus(lines);
			}
		});
	}

}

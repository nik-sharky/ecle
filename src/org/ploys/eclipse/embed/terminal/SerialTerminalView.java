package org.ploys.eclipse.embed.terminal;

import javax.annotation.PreDestroy;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

import org.eclipse.cdt.utils.pty.PTYOutputStream;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.ploys.eclipse.embed.common.State;
import org.ploys.eclipse.embed.terminal.ui.PortMonitor;
import org.ploys.eclipse.embed.terminal.ui.PortStatus;
import org.ploys.eclipse.embed.terminal.ui.PortToolsHandler;
import org.ploys.eclipse.embed.terminal.ui.PortStatus.Led;
import org.ploys.eclipse.embed.terminal.ui.PortTools;
import org.ploys.eclipse.embed.ui.Activator;
import org.ploys.eclipse.embed.ui.ComboToolItem;
import org.ploys.eclipse.embed.ui.Icons;
import org.ploys.eclipse.embed.ui.SpacerToolItem;
import org.ploys.eclipse.embed.ui.UI;

public class SerialTerminalView extends ViewPart {
	private Combo eRequest;

	PortTools portTools;
	PortMonitor monitor;
	PortStatus status;

	private SerialPort serialPort;

	public SerialTerminalView() {
	}

	@PreDestroy
	public void dispose() {
		if (serialPort != null && serialPort.isOpened()) {
			try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setFocus() {
		// TODO Set the focus to control
	}

	public String getPID() {
		return Activator.PLUGIN_ID;
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

		ToolBar toolBar_3 = new ToolBar(pMonitor, SWT.FLAT | SWT.RIGHT);

		ToolItem fDuplex = new ToolItem(toolBar_3, SWT.CHECK);
		fDuplex.setToolTipText("Duplex mode");
		fDuplex.setHotImage(Icons.ico("monitor"));
		fDuplex.setImage(Icons.ico("monitor-off"));

		Composite composite_1 = new Composite(pMonitor, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(4, false);
		gl_composite_1.marginHeight = 0;
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);

		eRequest = new Combo(composite_1, SWT.BORDER);
		eRequest.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button aSend = new Button(composite_1, SWT.NONE);
		aSend.setText("Send");

		Button fCR = new Button(composite_1, SWT.TOGGLE);
		fCR.setText("+CR");

		Button fLF = new Button(composite_1, SWT.TOGGLE);
		fLF.setText("+LF");

		Label spcStatus = new Label(cParent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		spcStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		status.createUI(cParent);

		initPort();
	}

	protected void createControllers() {
		monitor = new PortMonitor();
		status = new PortStatus();
		portTools = new PortTools(new PortToolsHandler() {
			@Override
			public void onPortChange() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onParamChange() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onConnect(boolean state) {
				try {
					if (state) {
						portTools.disablePortSelect(true);
						connect();
					} else {
						disconnect();
						portTools.disablePortSelect(false);
					}
				} catch (Exception e) {
					UI.errorDialog(StatusManager.SHOW, "Can't connect", e);
					portTools.disablePortSelect(false);
				}

			}
		});
	}

	// region *** Initialization ***

	protected void initPort() {
		String[] ports = SerialPortList.getPortNames();
		portTools.setParams(ports, 115200);
	}

	private int MASK_ALL = SerialPort.MASK_BREAK | SerialPort.MASK_CTS | SerialPort.MASK_DSR | SerialPort.MASK_RING | SerialPort.MASK_ERR
			| SerialPort.MASK_RLSD | SerialPort.MASK_RXCHAR;
	private int[] LINES_OFF = { 0, 0, 0, 0 };

	protected boolean connect() throws SerialPortException {
		disconnect();

		serialPort = new SerialPort(portTools.getPort());
		try {
			serialPort.openPort();

			portTools.setPortParams(serialPort);

			int[] lines = serialPort.getLinesStatus();
			setLeds(lines, false, 0);
			serialPort.addEventListener(serialListener, MASK_ALL);

			updatePortStatus(portTools.getStatus());
		} catch (SerialPortException e) {
			e.printStackTrace();
			return false;
		}

		return true;
		// setRTS,
		// setDTR);

	}

	protected void disconnect() throws SerialPortException {
		if (serialPort != null) {
			if (serialPort.isOpened()) {
				serialPort.removeEventListener();
				serialPort.closePort();
			}
		}

		setLeds(LINES_OFF, false, 0);
		updatePortStatus("Closed");
	}

	SerialPortEventListener serialListener = new SerialPortEventListener() {
		@Override
		public void serialEvent(SerialPortEvent e) {
			final int etype = e.getEventType();
			final int evalue = e.getEventValue();

			System.out.println(e.getEventType());

			switch (etype) {
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RING:
			case SerialPortEvent.RLSD:
			case SerialPortEvent.ERR:
				setLed(toLed(etype), evalue != 0);
				break;
			case SerialPortEvent.BREAK:
				setLed(toLed(etype), true);
				break;
			case SerialPortEvent.RXCHAR:
			case SerialPortEvent.RXFLAG:
				readData(e.getEventValue());
				break;
			case SerialPortEvent.TXEMPTY:
				break;
			}
		}

		private PortStatus.Led toLed(int etype) {
			switch (etype) {
			case SerialPortEvent.CTS:
				return PortStatus.Led.CTS;
			case SerialPortEvent.DSR:
				return PortStatus.Led.DSR;
			case SerialPortEvent.RING:
				return PortStatus.Led.RNG;
			case SerialPortEvent.RLSD:
				return PortStatus.Led.DCD;
			case SerialPortEvent.ERR:
				return PortStatus.Led.ERROR;
			case SerialPortEvent.BREAK:
				return PortStatus.Led.BREAK;
			}

			return null;
		}
	};

	private void readData(int len) {
		try {
			UI.runAsync(new Runnable() {
				public void run() {
					status.setLed(PortStatus.Led.RXD, true);
				}
			});
			final String data = serialPort.readString(len);
			UI.runAsync(new Runnable() {
				public void run() {
					monitor.append(data);
					status.setLed(PortStatus.Led.RXD, false);
				}
			});

		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	// endregion

	boolean isPortActive() {
		return serialPort != null && serialPort.isOpened();
	}

	private void setLed(final Led led, final boolean state) {
		UI.runAsync(new Runnable() {
			public void run() {
				status.setLed(led, state);
			}
		});
	}

	private void setLeds(final int[] line, final boolean br, final int err) {
		UI.runAsync(new Runnable() {
			public void run() {
				if (!isPortActive()) {
					status.resetLeds();
					return;
				}

				status.setLinesStatus(line);
				status.setBreak(br);
				status.setError(err);
			}
		});
	}

	private void updatePortStatus(final String str) {
		UI.runAsync(new Runnable() {
			public void run() {
				status.setPortStatus("Port: " + str);
			}
		});
	}

}

package org.ploys.eclipse.embed.terminal;

import javax.annotation.PreDestroy;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.ploys.eclipse.embed.terminal.ui.DataRateToolItem;
import org.ploys.eclipse.embed.terminal.ui.ParityToolItem;
import org.ploys.eclipse.embed.ui.Activator;
import org.ploys.eclipse.embed.ui.ComboToolItem;
import org.ploys.eclipse.embed.ui.SpacerToolItem;
import org.ploys.eclipse.embed.ui.UI;

public class SerialTerminalView extends ViewPart {
	private Combo eRequest;
	private StyledText eMonitor;
	private CLabel lStatus;
	private CLabel fRxd, fTxd, fCts, fDcd, fDsr, fRng, fError;
	private ToolItem fRts, fDtr;
	
	private Image fsiOn, fsiOff, fsiError;
	ComboToolItem cPort, cDataBits, cStopBits;
	ParityToolItem cParity;
	DataRateToolItem cSpeed;

	public SerialTerminalView() {
		fsiOn = ResourceManager.getPluginImage(getPID(), "icons/full/eview16/state/green.png");
		fsiOff = ResourceManager.getPluginImage(getPID(), "icons/full/eview16/state/gray.png");
		fsiError = ResourceManager.getPluginImage(getPID(), "icons/full/eview16/state/red.png");
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
		return "org.ploys.eclipse.embed";// Activator.PLUGIN_ID;
	}

	/**
	 * Create contents of the view part.
	 */
	@Override
	public void createPartControl(Composite cParent) {
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

		ToolBar tbPort = new ToolBar(pToolbar, SWT.FLAT | SWT.RIGHT);
		GridData gd_tbPort = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_tbPort.minimumHeight = 24;
		tbPort.setLayoutData(gd_tbPort);

		aConnect = new ToolItem(tbPort, SWT.CHECK);
		aConnect.setHotImage(ResourceManager.getPluginImage(getPID(), "icons/full/eview16/plug-connect.png"));
		aConnect.setImage(ResourceManager.getPluginImage(getPID(), "icons/full/eview16/plug-disconnect.png"));
		aConnect.setText("Connect");

		new SpacerToolItem(tbPort, 10);
		cPort = new ComboToolItem(tbPort, SWT.READ_ONLY, "Serial port");
		new SpacerToolItem(tbPort, 5);
		cSpeed = new DataRateToolItem(tbPort, SWT.NONE, "Baud rate");
		new SpacerToolItem(tbPort, 5);
		cDataBits = new ComboToolItem(tbPort, SWT.READ_ONLY, "Data bits");
		new SpacerToolItem(tbPort, 5);
		cParity = new ParityToolItem(tbPort, SWT.READ_ONLY, "Parity");
		new SpacerToolItem(tbPort, 5);
		cStopBits = new ComboToolItem(tbPort, SWT.READ_ONLY, "Stop bits");

		Composite composite_2 = new Composite(pToolbar, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		composite_2.setLayout(new GridLayout(1, false));

		ToolBar tbPins = new ToolBar(pToolbar, SWT.FLAT | SWT.RIGHT);

		fRts = new ToolItem(tbPins, SWT.CHECK);
		fRts.setHotImage(fsiOn);
		fRts.setImage(fsiOff);
		fRts.setText("RTS");
		fRts.setToolTipText("Toggle RTS pin");

		new SpacerToolItem(tbPins, 5);
		
		fDtr = new ToolItem(tbPins, SWT.CHECK);
		fDtr.setHotImage(fsiOn);
		fDtr.setImage(fsiOff);
		fDtr.setText("DTR");
		fDtr.setToolTipText("Toggle DTR pin");

		Composite pMonitor = new Composite(cParent, SWT.NONE);
		pMonitor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_pMonitor = new GridLayout(2, false);
		pMonitor.setLayout(gl_pMonitor);

		createMonitor(pMonitor);

		ToolBar toolBar_3 = new ToolBar(pMonitor, SWT.FLAT | SWT.RIGHT);

		ToolItem fDuplex = new ToolItem(toolBar_3, SWT.CHECK);
		fDuplex.setToolTipText("Duplex mode");
		fDuplex.setHotImage(ResourceManager.getPluginImage(getPID(), "icons/full/eview16/monitor.png"));
		fDuplex.setImage(ResourceManager.getPluginImage(getPID(), "icons/full/eview16/monitor-off.png"));

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

		Composite pStatus = new Composite(cParent, SWT.NO_FOCUS);
		pStatus.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridLayout gl_pStatus = new GridLayout(4, false);
		gl_pStatus.marginTop = 1;
		gl_pStatus.marginBottom = 2;
		gl_pStatus.marginHeight = 0;
		gl_pStatus.verticalSpacing = 0;
		pStatus.setLayout(gl_pStatus);
		pStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lStatus = new CLabel(pStatus, SWT.NONE);
		lStatus.setImage(ResourceManager.getPluginImage(getPID(), "icons/full/eview16/plug-disconnect.png"));
		lStatus.setLeftMargin(2);
		lStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lStatus.setRightMargin(5);
		lStatus.setTopMargin(2);
		lStatus.setBottomMargin(1);
		lStatus.setText("Port: Closed");

		createIndicators(pStatus);

		initListeners();
		initPort();
	}

	private void createMonitor(Composite parent) {
		ToolBar tbMonitor = new ToolBar(parent, SWT.FLAT | SWT.RIGHT | SWT.VERTICAL);
		tbMonitor.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

		createMonitorButton(tbMonitor, MonitorAction.SAVE, "Save content to file...", "disk.png");
		createMonitorButton(tbMonitor, MonitorAction.HOME, "Scroll to home", "atop.png");
		createMonitorButton(tbMonitor, MonitorAction.END, "Scroll to end", "abottom.png");
		createMonitorButton(tbMonitor, MonitorAction.CLEAR, "Clear", "eraser.png");

		eMonitor = new StyledText(parent, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		eMonitor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		eMonitor.setRightMargin(3);
		eMonitor.setBottomMargin(3);
		eMonitor.setTopMargin(3);
		eMonitor.setLeftMargin(3);
		eMonitor.setText("Embed plugin serial terminal\r\n");
	}

	private void createMonitorButton(ToolBar tb, final MonitorAction action, String tip, String ico) {
		ToolItem teClear = new ToolItem(tb, SWT.NONE);
		teClear.setToolTipText(tip);
		teClear.setImage(ResourceManager.getPluginImage(getPID(), "icons/full/eview16/" + ico));
		teClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doMonitorAction(action);
			}
		});
	}

	protected void createIndicators(Composite parent) {
		Composite pSigstate = new Composite(parent, SWT.NONE);
		GridLayout gl_pSigstate = new GridLayout(6, true);
		gl_pSigstate.marginHeight = 0;
		pSigstate.setLayout(gl_pSigstate);

		fRxd = createIndicator(pSigstate, "RXD");
		fTxd = createIndicator(pSigstate, "TXD");

		fCts = createIndicator(pSigstate, "CTS");
		fDcd = createIndicator(pSigstate, "DCD");
		fDsr = createIndicator(pSigstate, "DSR");
		fRng = createIndicator(pSigstate, "RNG");

		fError = createIndicator(parent, "ERROR");
		fError.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		fError.setRightMargin(2);
		fError.setLeftMargin(20);
	}

	protected CLabel createIndicator(Composite parent, String txt) {
		CLabel ind = new CLabel(parent, SWT.NONE);
		ind.setTopMargin(1);
		ind.setRightMargin(0);
		ind.setBottomMargin(1);
		ind.setLeftMargin(0);
		ind.setText(txt);
		ind.setImage(fsiOff);
		return ind;
	}

	// region *** Initialization ***
	protected void initListeners() {
		aConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent ev) {
				ToolItem button = ((ToolItem) ev.widget);
				try {
					if (button.getSelection()) {
						connect();
					} else {
						disconnect();
					}
				} catch (Exception e) {
					UI.errorDialog(StatusManager.SHOW, "Can't connect", e);					
				}
			}
		});
	}

	protected void initPort() {
		String[] ports = SerialPortList.getPortNames();
		Combo cp = cPort.getControl();
		cp.setItems(ports);
		cp.select(0);
		cPort.updateView();
		cSpeed.select(115200);		
	}

	protected void connect() throws SerialPortException {
		disconnect();

		serialPort = new SerialPort(cPort.getControl().getText());
		serialPort.openPort();
		serialPort.setParams(cSpeed.getValue(), dataBits, stopBits, cParity.getValue(), false, false);// ,
		// setRTS,
		// setDTR);

		serialPort.addEventListener(serialListener);
	}

	protected void disconnect() throws SerialPortException {
		if (serialPort != null) {
			if (serialPort.isOpened()) {
				serialPort.removeEventListener();
				serialPort.closePort();
			}
		}
	}

	SerialPortEventListener serialListener = new SerialPortEventListener() {
		@Override
		public void serialEvent(SerialPortEvent e) {
			System.out.println(e);
			switch (e.getEventType()) {
			case SerialPortEvent.CTS:
			case SerialPortEvent.RLSD:
				updateIndicator(fDcd, e.getEventValue() == 1);
				break;
			case SerialPortEvent.DSR:
				updateIndicator(fDsr, e.getEventValue() == 1);
				break;
			case SerialPortEvent.RING:
				updateIndicator(fRng, e.getEventValue() == 1);
				break;
			case SerialPortEvent.ERR:
				break;
			case SerialPortEvent.BREAK:
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
			UI.runAsync(new Runnable() {
				public void run() {
					updateIndicator(fRxd, true);
				}
			});
			final String data = serialPort.readString(len);
			UI.runAsync(new Runnable() {
				public void run() {
					eMonitor.append(data);
					eMonitor.append(" ");
					updateIndicator(fRxd, false);
				}
			});

		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	// endregion

	private SerialPort serialPort;
	private ToolItem aConnect;
	private String portName;
	private int baudRate = SerialPort.BAUDRATE_9600;
	private int dataBits = SerialPort.DATABITS_8;
	private int stopBits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;

	boolean isPortActive() {
		return serialPort != null && serialPort.isOpened();
	}

	void updateIndicator(CLabel indicator, boolean state) {
		indicator.setImage(state ? fsiOn : fsiOff);
		indicator.update();
	}

	void updateIndicators() throws SerialPortException {
		if (!isPortActive()) {
			updateIndicator(fCts, false);
			updateIndicator(fDcd, false);
			updateIndicator(fDsr, false);
			updateIndicator(fRng, false);
			return;
		}

		updateIndicator(fCts, serialPort.isCTS());
		updateIndicator(fDcd, serialPort.isRLSD());
		updateIndicator(fDsr, serialPort.isDSR());
		updateIndicator(fRng, serialPort.isRING());

		// updateSignal(fRxd, e.isRXFLAG());
		// updateSignal(fTxd, !e.isTXEMPTY());
	}

	enum MonitorAction {
		CLEAR, HOME, END, SAVE
	}

	void doMonitorAction(MonitorAction action) {
		switch (action) {
		case CLEAR:
			eMonitor.setText("");
			break;
		case HOME:
			eMonitor.setTopIndex(0);
			break;
		case END:
			eMonitor.setTopIndex(eMonitor.getLineCount() - 1);
			break;
		case SAVE:
			break;
		}

	}
}

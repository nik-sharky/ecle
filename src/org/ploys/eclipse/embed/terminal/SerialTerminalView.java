package org.ploys.eclipse.embed.terminal;

import javax.annotation.PreDestroy;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.ploys.eclipse.embed.ui.Activator;
import org.ploys.eclipse.embed.ui.ComboToolItem;

public class SerialTerminalView extends ViewPart {
	private Combo eRequest;
	private StyledText eMonitor;
	private CLabel lStatus;
	private CLabel fRxd, fTxd, fCts, fDcd, fDsr, fRng, fError;
	private Image fsiOn, fsiOff, fsiError;
	ComboToolItem cPort;

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

		ToolItem toolItem = new ToolItem(tbPort, SWT.SEPARATOR);

		Composite composite_5 = new Composite(tbPort, SWT.NONE);
		toolItem.setControl(composite_5);

		cPort = new ComboToolItem(tbPort);

		ToolItem tmSpeed = new ToolItem(tbPort, SWT.SEPARATOR);
		// tmSpeed.setWidth(64);

		Combo cSpeed = new Combo(tbPort, SWT.NONE);
		cSpeed.setToolTipText("Connection speed");
		cSpeed.setItems(new String[] { "4800", "9600", "115200" });
		tmSpeed.setControl(cSpeed);
		cSpeed.select(2);
		cSpeed.pack();
		tmSpeed.setWidth(cSpeed.getSize().x);

		ToolItem tmDatabits = new ToolItem(tbPort, SWT.SEPARATOR);
		tmDatabits.setToolTipText("");
		Combo cDatabits = new Combo(tbPort, SWT.READ_ONLY);
		cDatabits.setToolTipText("Data bits");
		cDatabits.setItems(new String[] { "8" });
		tmDatabits.setControl(cDatabits);
		cDatabits.select(0);
		cDatabits.pack();
		tmDatabits.setWidth(cDatabits.getSize().x);

		ToolItem tmParity = new ToolItem(tbPort, SWT.SEPARATOR);
		Combo cParity = new Combo(tbPort, SWT.READ_ONLY);
		cParity.setToolTipText("Parity");
		cParity.setItems(new String[] { "N" });
		tmParity.setControl(cParity);
		cParity.select(0);
		cParity.pack();
		tmParity.setWidth(cParity.getSize().x);

		ToolItem tmStopbits = new ToolItem(tbPort, SWT.SEPARATOR);
		Combo cStopbits = new Combo(tbPort, SWT.READ_ONLY);
		cStopbits.setToolTipText("Stop bits");
		cStopbits.setItems(new String[] { "1" });
		tmStopbits.setControl(cStopbits);
		cStopbits.select(0);
		cStopbits.pack();
		tmStopbits.setWidth(cStopbits.getSize().x);

		Composite composite_2 = new Composite(pToolbar, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		composite_2.setLayout(new GridLayout(1, false));

		ToolBar toolBar_2 = new ToolBar(pToolbar, SWT.FLAT | SWT.RIGHT);

		ToolItem fRts = new ToolItem(toolBar_2, SWT.CHECK);
		fRts.setHotImage(ResourceManager.getPluginImage(getPID(), "icons/full/eview16/state/green.png"));
		fRts.setImage(ResourceManager.getPluginImage(getPID(), "icons/full/eview16/state/gray.png"));
		fRts.setText("RTS");

		ToolItem fDtr = new ToolItem(toolBar_2, SWT.CHECK);
		fDtr.setHotImage(ResourceManager.getPluginImage("org.ploys.eclipse.embed", "icons/full/eview16/state/green.png"));
		fDtr.setImage(ResourceManager.getPluginImage("org.ploys.eclipse.embed", "icons/full/eview16/state/gray.png"));
		fDtr.setText("DTR");

		Composite pMonitor = new Composite(cParent, SWT.NONE);
		pMonitor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_pMonitor = new GridLayout(2, false);
		pMonitor.setLayout(gl_pMonitor);

		ToolBar toolBar = new ToolBar(pMonitor, SWT.FLAT | SWT.RIGHT | SWT.VERTICAL);
		toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

		ToolItem teSave = new ToolItem(toolBar, SWT.NONE);
		teSave.setToolTipText("Save content");
		teSave.setImage(ResourceManager.getPluginImage("org.ploys.eclipse.embed", "icons/full/eview16/disk.png"));

		ToolItem teHome = new ToolItem(toolBar, SWT.NONE);
		teHome.setToolTipText("Scroll to home");
		teHome.setImage(ResourceManager.getPluginImage("org.ploys.eclipse.embed", "icons/full/eview16/atop.png"));

		ToolItem teEnd = new ToolItem(toolBar, SWT.NONE);
		teEnd.setToolTipText("Scroll to end");
		teEnd.setImage(ResourceManager.getPluginImage("org.ploys.eclipse.embed", "icons/full/eview16/abottom.png"));

		ToolItem teClear = new ToolItem(toolBar, SWT.NONE);
		teClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				aClear();
			}
		});
		teClear.setToolTipText("Clear");
		teClear.setImage(ResourceManager.getPluginImage("org.ploys.eclipse.embed", "icons/full/eview16/eraser.png"));

		ScrolledComposite scrolledComposite = new ScrolledComposite(pMonitor, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		eMonitor = new StyledText(scrolledComposite, SWT.READ_ONLY);
		eMonitor.setRightMargin(3);
		eMonitor.setBottomMargin(3);
		eMonitor.setTopMargin(3);
		eMonitor.setLeftMargin(3);
		eMonitor.setText("1\r\n2\r\n3\r\n4\r\n5\r\n6\r\n7\r\n8\r\n9\r\n10\r\n11\r\n12\r\n13\r\n14\r\n15");

		scrolledComposite.setContent(eMonitor);
		scrolledComposite.setMinSize(eMonitor.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		ToolBar toolBar_3 = new ToolBar(pMonitor, SWT.FLAT | SWT.RIGHT);

		ToolItem fDuplex = new ToolItem(toolBar_3, SWT.CHECK);
		fDuplex.setToolTipText("Duplex mode");
		fDuplex.setHotImage(ResourceManager.getPluginImage("org.ploys.eclipse.embed", "icons/full/eview16/monitor.png"));
		fDuplex.setImage(ResourceManager.getPluginImage("org.ploys.eclipse.embed", "icons/full/eview16/monitor-off.png"));

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
		lStatus.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lStatus.setImage(ResourceManager.getPluginImage(getPID(), "icons/full/eview16/plug-disconnect.png"));
		lStatus.setLeftMargin(2);
		lStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lStatus.setRightMargin(5);
		lStatus.setTopMargin(2);
		lStatus.setBottomMargin(1);
		lStatus.setText("COM1 8N1 115200");

		createIndicators(pStatus);

		initListeners();
		initPort();
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
			public void widgetSelected(SelectionEvent e) {
			}
		});

	}

	protected void initPort() {
		String[] ports = SerialPortList.getPortNames();
		Combo cp = cPort.getControl();
		cp.setItems(ports);
		cp.select(0);
		cPort.updateView();
		cPort.setToolTipText("Serial port");

		// serialPort = new SerialPort("COM1");
	}

	// endregion

	
	private SerialPort serialPort;
	private ToolItem aConnect;

	void updateSignal(CLabel lbl, boolean state) {
		if (fsiOff == null)
			fsiOff = ResourceManager.getPluginImage(getPID(), "icons/full/eview16/state/gray.png");

		if (fsiOn == null)
			fsiOn = ResourceManager.getPluginImage(getPID(), "icons/full/eview16/state/green.png");

		lbl.setImage(state ? fsiOn : fsiOff);
	}

	void updateSignals(SerialPortEvent e) {

		updateSignal(fRxd, e.isRXFLAG());
		updateSignal(fTxd, !e.isTXEMPTY());

		// updateSignal(fCts, port.isCTS());

		updateSignal(fDcd, e.isRLSD());

		updateSignal(fDsr, e.isDSR());
		updateSignal(fRng, e.isRING());
	}

	private String portName;
	private int baudRate = SerialPort.BAUDRATE_9600;
	private int dataBits = SerialPort.DATABITS_8;
	private int stopBits = SerialPort.STOPBITS_1;
	private int parity = SerialPort.PARITY_NONE;

	void aClear() {
		eMonitor.setText("");
	}
}

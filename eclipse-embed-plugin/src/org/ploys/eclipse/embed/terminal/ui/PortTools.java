package org.ploys.eclipse.embed.terminal.ui;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.statushandlers.StatusManager;
import org.ploys.eclipse.embed.common.Converter;
import org.ploys.eclipse.embed.common.State;
import org.ploys.eclipse.embed.terminal.SerialPin;
import org.ploys.eclipse.embed.ui.ComboToolItem;
import org.ploys.eclipse.embed.ui.Icons;
import org.ploys.eclipse.embed.ui.MapComboToolItem;
import org.ploys.eclipse.embed.ui.SpacerToolItem;
import org.ploys.eclipse.embed.ui.UI;

public class PortTools {
	static Integer[] dataRates = { 1200, 2400, 4800, 9600, 14400, 19200, 38400, 57600, 74880, 115200, 128000, 230400, 256000, 460800, 921600 };
	static Integer[] dataBits = { SerialPort.DATABITS_8, SerialPort.DATABITS_7, SerialPort.DATABITS_6, SerialPort.DATABITS_5 };
	static Integer[] stopBits = { SerialPort.STOPBITS_1, SerialPort.STOPBITS_1_5, SerialPort.STOPBITS_2 };
	static Integer[] parities = { SerialPort.PARITY_NONE, SerialPort.PARITY_ODD, SerialPort.PARITY_EVEN, SerialPort.PARITY_MARK,
			SerialPort.PARITY_SPACE };

	static int fcRTSCTS = SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT;
	static int fcXONXOFF = SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT;
	static Integer[] fcs = { SerialPort.FLOWCONTROL_NONE, fcRTSCTS, fcXONXOFF };

	private ToolItem aConnect;

	private PortTools.Port cPort;
	private PortTools.DataBits cDataBits;
	private PortTools.StopBits cStopBits;
	private PortTools.Parity cParity;
	private PortTools.DataRate cSpeed;
	private ToolItem fRts, fDtr;

	private PortToolsHandler handler;

	public PortTools(PortToolsHandler handler) {
		this.handler = handler;
	}

	public void createUI(Composite parent) {
		ToolBar tbPort = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		GridData gd_tbPort = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_tbPort.minimumHeight = 24;
		tbPort.setLayoutData(gd_tbPort);

		aConnect = new ToolItem(tbPort, SWT.CHECK);
		aConnect.setHotImage(Icons.ico("plug-connect"));
		aConnect.setImage(Icons.ico("plug-disconnect"));
		aConnect.setText("Connect");

		new SpacerToolItem(tbPort, 10);
		cPort = new PortTools.Port(tbPort, SWT.READ_ONLY, "Serial port");
		new SpacerToolItem(tbPort, 5);
		cSpeed = new PortTools.DataRate(tbPort, SWT.NONE, "Baud rate");
		new SpacerToolItem(tbPort, 5);
		cDataBits = new PortTools.DataBits(tbPort, SWT.READ_ONLY, "Data bits");
		new SpacerToolItem(tbPort, 5);
		cParity = new PortTools.Parity(tbPort, SWT.READ_ONLY, "Parity");
		new SpacerToolItem(tbPort, 5);
		cStopBits = new PortTools.StopBits(tbPort, SWT.READ_ONLY, "Stop bits");

		Composite composite_2 = new Composite(parent, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		composite_2.setLayout(new GridLayout(1, false));

		ToolBar tbPins = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);

		fRts = new ToolItem(tbPins, SWT.CHECK);
		fRts.setHotImage(Icons.state(State.ON));
		fRts.setImage(Icons.state(State.OFF));
		fRts.setText("RTS");
		fRts.setToolTipText("Toggle RTS pin");

		new SpacerToolItem(tbPins, 5);

		fDtr = new ToolItem(tbPins, SWT.CHECK);
		fDtr.setHotImage(Icons.state(State.ON));
		fDtr.setImage(Icons.state(State.OFF));
		fDtr.setText("DTR");
		fDtr.setToolTipText("Toggle DTR pin");

		initListeners();
	}

	protected void initListeners() {
		aConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent ev) {
				ToolItem button = ((ToolItem) ev.widget);
				boolean result = handler.onConnect(button.getSelection());
				button.setSelection(result);
			}
		});

		cPort.getControl().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				cPort.refreshList();
			}
		});

		cPort.getControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				cPort.refreshList();
			}
		});

		fRts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent ev) {
				ToolItem button = ((ToolItem) ev.widget);
				handler.onPinChange(SerialPin.RTS, button.getSelection());
			}
		});

		fDtr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent ev) {
				ToolItem button = ((ToolItem) ev.widget);
				handler.onPinChange(SerialPin.DTR, button.getSelection());
			}
		});

		SelectionAdapter paramListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent ev) {
				handler.onParamsChange();
			}
		};

		cSpeed.addSelectionListener(paramListener);
		cParity.addSelectionListener(paramListener);
		cDataBits.addSelectionListener(paramListener);
		cStopBits.addSelectionListener(paramListener);
	}

	public static class Port extends MapComboToolItem<String> {
		public Port(ToolBar parent, int style, String tip) {
			super(parent, style, tip);
			refreshList();
		}

		public void refreshList() {
			String val = getValue();
			Combo cb = getControl();
			initList(SerialPortList.getPortNames());

			if (val != null) {
				if (containsValue(val))
					select(val);
				else
					cb.select(0);
			} else {
				cb.select(0);
			}

			cb.setVisibleItemCount(20);
			updateView();
		}
	}

	public static class DataRate extends MapComboToolItem<Integer> {
		public final static int DEFAULT = 9600;

		public DataRate(ToolBar parent, int style, String tip) {
			super(parent, style, tip);
			initList(dataRates);
			select(DEFAULT);

			// TODO add verifyListener
		}

		public Integer getValue() {
			Integer val = super.getValue();

			if (val == null) {
				val = DEFAULT;
				try {
					val = Integer.parseInt(getControl().getText());
				} catch (NumberFormatException e) {

				}
			}

			return val;
		}
	}

	public static class Parity extends MapComboToolItem<Integer> {
		public final static int DEFAULT = SerialPort.PARITY_NONE;

		public Parity(ToolBar parent, int style, String tip) {
			super(parent, style, tip);
			setConverter(createConverter());

			initList(parities);
			select(DEFAULT);
		}

		private static Converter<Integer, String> createConverter() {
			Converter<Integer, String> conv = new Converter<Integer, String>() {
				@Override
				public String convert(Integer data) {
					switch (data) {
					case SerialPort.PARITY_NONE:
						return "None";
					case SerialPort.PARITY_ODD:
						return "Odd";
					case SerialPort.PARITY_EVEN:
						return "Even";
					case SerialPort.PARITY_MARK:
						return "Mark";
					case SerialPort.PARITY_SPACE:
						return "Space";
					}

					return "?";
				}
			};

			return conv;
		}

	}

	public static class DataBits extends MapComboToolItem<Integer> {
		public final static int DEFAULT = SerialPort.DATABITS_8;

		public DataBits(ToolBar parent, int style, String tip) {
			super(parent, style, tip);

			initList(dataBits);
			select(DEFAULT);
		}
	}

	public static class StopBits extends MapComboToolItem<Integer> {
		public final static int DEFAULT = SerialPort.STOPBITS_1;

		public StopBits(ToolBar parent, int style, String tip) {
			super(parent, style, tip);
			setConverter(createConverter());

			initList(stopBits);
			select(DEFAULT);
		}

		private static Converter<Integer, String> createConverter() {
			Converter<Integer, String> conv = new Converter<Integer, String>() {
				@Override
				public String convert(Integer data) {
					switch (data) {
					case SerialPort.STOPBITS_1:
						return "1";
					case SerialPort.STOPBITS_1_5:
						return "1.5";
					case SerialPort.STOPBITS_2:
						return "2";
					}

					return "?";
				}
			};

			return conv;
		}
	}

	public String getPort() {
		return cPort.getControl().getText();
	}

	public void setPortParams(SerialPort serialPort) throws SerialPortException {
		serialPort.setParams(cSpeed.getValue(), cDataBits.getValue(), cStopBits.getValue(), cParity.getValue(), fRts.getSelection(),
				fDtr.getSelection());
	}

	public String getStatus() {
		String s = cPort.getControl().getText() + " " + cSpeed.getValue() + " " + cDataBits.getValue() + cParity.getTitle().substring(0, 1)
				+ cStopBits.getTitle();
		return s;
	}

	public void disablePortSelect(boolean state) {
		cPort.getControl().setEnabled(!state);
	}

	public void setParams(String[] ports, int rate) {
		Combo cp = cPort.getControl();
		cp.setItems(ports);
		cp.select(0);
		cPort.updateView();
		cSpeed.select(rate);
	}
}

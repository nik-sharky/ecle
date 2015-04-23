package org.ploys.eclipse.embed.terminal.ui;

import jssc.SerialPort;

import org.eclipse.swt.widgets.ToolBar;
import org.ploys.eclipse.embed.common.Converter;
import org.ploys.eclipse.embed.ui.ComboToolItem;
import org.ploys.eclipse.embed.ui.MapComboToolItem;

public class PortTools {
	static Integer[] dataRates = { 110, 300, 600, 1200, 4800, 9600, 14400, 19200, 38400, 57600, 115200, 128000, 256000 };
	static Integer[] dataBits = { SerialPort.DATABITS_8, SerialPort.DATABITS_7, SerialPort.DATABITS_6, SerialPort.DATABITS_5 };
	static Integer[] stopBits = { SerialPort.STOPBITS_1, SerialPort.STOPBITS_1_5, SerialPort.STOPBITS_2 };
	static Integer[] parities = { SerialPort.PARITY_NONE, SerialPort.PARITY_ODD, SerialPort.PARITY_EVEN, SerialPort.PARITY_MARK,
			SerialPort.PARITY_SPACE };

	static int fcRTSCTS = SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT;
	static int fcXONXOFF = SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT;	
	static Integer[] fcs = { SerialPort.FLOWCONTROL_NONE, fcRTSCTS, fcXONXOFF };

	public static class Port extends ComboToolItem {
		public Port(ToolBar parent, int style, String tip) {
			super(parent, style, tip);
		}
	}

	public static class DataRate extends MapComboToolItem<Integer> {
		public final static int DEFAULT = 9600;

		public DataRate(ToolBar parent, int style, String tip) {
			super(parent, style, tip);
			initList(dataRates);
			select(DEFAULT);
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

}

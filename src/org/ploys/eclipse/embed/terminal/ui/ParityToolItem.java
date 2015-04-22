package org.ploys.eclipse.embed.terminal.ui;

import jssc.SerialPort;

import org.eclipse.swt.widgets.ToolBar;
import org.ploys.eclipse.embed.common.Converter;
import org.ploys.eclipse.embed.ui.MapComboToolItem;

public class ParityToolItem extends MapComboToolItem<Integer> {

	public ParityToolItem(ToolBar parent, int style, String tip) {
		super(parent, style, createConverter(), tip);

		initList(SerialPort.PARITY_NONE, SerialPort.PARITY_ODD, SerialPort.PARITY_EVEN, SerialPort.PARITY_MARK, SerialPort.PARITY_SPACE);
		select(SerialPort.PARITY_NONE);
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

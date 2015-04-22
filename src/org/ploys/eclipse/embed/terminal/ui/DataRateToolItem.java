package org.ploys.eclipse.embed.terminal.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.ToolBar;
import org.ploys.eclipse.embed.terminal.SerialKeys;
import org.ploys.eclipse.embed.ui.ComboToolItem;

public class DataRateToolItem extends ComboToolItem {
	static int[] dataRates = { 110, 300, 600, 1200, 4800, 9600, 14400, 19200, 38400, 57600, 115200, 128000, 256000 };
	Map<Integer, Integer> idxMap = new HashMap<>();

	public DataRateToolItem(ToolBar parent, int style, String tip) {
		super(parent, style, tip);
		initList(dataRates);
		select(9600);
	}

	public void initList(int... items) {
		Combo cp = getControl();
		String[] values = new String[items.length];

		idxMap.clear();
		for (int i = 0; i < items.length; i++) {
			int rate = items[i];
			values[i] = String.valueOf(rate);
			idxMap.put(rate, i);
		}

		cp.setItems(values);
		updateView();
	}

	public int getValue() {
		int rate = 9600;

		try {
			rate = Integer.parseInt(getControl().getText());
		} catch (NumberFormatException e) {

		}
		return rate;
	}

	public void select(int rate) {
		Combo cp = getControl();

		Integer idx = idxMap.get(rate);
		if (idx != null) {
			cp.select(idx);
			return;
		}

		// is editable
		if (SWT.READ_ONLY != (cp.getStyle() & SWT.READ_ONLY)) {
			cp.setText(String.valueOf(rate));
		}
	}
}

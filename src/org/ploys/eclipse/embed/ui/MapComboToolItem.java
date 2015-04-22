package org.ploys.eclipse.embed.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.ToolBar;
import org.ploys.eclipse.embed.common.Converter;
import org.ploys.eclipse.embed.terminal.SerialKeys;
import org.ploys.eclipse.embed.ui.ComboToolItem;

public class MapComboToolItem<V> extends ComboToolItem {
	Converter<V, String> converter;
	Map<V, Integer> idxMap = new HashMap<>();
	Map<Integer, V> valMap = new HashMap<>();

	public MapComboToolItem(ToolBar parent, int style, Converter<V, String> conv, String tip) {
		super(parent, style, tip);
		converter = conv;
	}

	public void initList(V... items) {
		Combo cp = getControl();
		String[] values = new String[items.length];

		idxMap.clear();
		valMap.clear();

		for (int i = 0; i < items.length; i++) {
			V value = items[i];
			values[i] = convert(value);
			idxMap.put(value, i);
			valMap.put(i, value);
		}

		cp.setItems(values);
		updateView();
	}

	protected String convert(V data) {
		if (converter != null)
			return converter.convert(data);

		return String.valueOf(data);
	}

	public V getValue() {
		int idx = getControl().getSelectionIndex();
		if (idx == -1)
			return null;

		return valMap.get(idx);
	}

	public void select(V value) {
		Combo cp = getControl();

		Integer idx = idxMap.get(value);
		if (idx != null) {
			cp.select(idx);
			return;
		}

		// is editable
		if (SWT.READ_ONLY != (cp.getStyle() & SWT.READ_ONLY)) {
			cp.setText(String.valueOf(value));
		}
	}
}

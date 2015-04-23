package org.ploys.eclipse.embed.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.ToolBar;
import org.ploys.eclipse.embed.common.Converter;
import org.ploys.eclipse.embed.ui.ComboToolItem;

public class MapComboToolItem<V> extends ComboToolItem {
	private Converter<V, String> converter = new Converter.ToString<V>();
	private Map<V, Integer> idxMap = new HashMap<>();
	private Map<Integer, V> valMap = new HashMap<>();

	public MapComboToolItem(ToolBar parent, int style, String tip) {
		super(parent, style, tip);
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

	public void setConverter(Converter<V, String> conv) {
		converter = conv;
	}

	protected String convert(V data) {
		return converter.convert(data);
	}

	public V getValue() {
		int idx = getControl().getSelectionIndex();
		if (idx == -1)
			return null;

		return valMap.get(idx);
	}

	public String getTitle() {
		V val = getValue();
		return val == null ? null : convert(val);
	}

	public void select(V value) {
		Combo cp = getControl();

		Integer idx = idxMap.get(value);
		if (idx != null) {
			cp.select(idx);
			return;
		}

		// is editable
		if (!isReadOnly()) {
			cp.setText(convert(value));
		}
	}
}

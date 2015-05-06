package org.ploys.eclipse.embed.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ComboToolItem extends ToolItem {
	public ComboToolItem(ToolBar parent, int style, String tip) {
		super(parent, SWT.SEPARATOR);
		Combo control = new Combo(parent, style);
		control.setToolTipText(tip);
		setControl(control);
		updateView();
	}

	public Combo getControl() {
		return (Combo) super.getControl();
	}

	public void updateView() {
		Combo control = getControl();
		control.pack();
		setWidth(control.getSize().x);
	}

	public boolean isReadOnly() {
		return SWT.READ_ONLY == (getControl().getStyle() & SWT.READ_ONLY);
	}

	protected void checkSubclass() {
		// Force subclassing
	}
}

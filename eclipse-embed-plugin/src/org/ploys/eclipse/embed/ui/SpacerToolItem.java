package org.ploys.eclipse.embed.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class SpacerToolItem extends ToolItem {
	public SpacerToolItem(ToolBar parent, int width) {
		super(parent, SWT.SEPARATOR);

		Composite control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginLeft = width;
		control.setLayout(layout);
		setControl(control);
		updateView();
	}

	public void updateView() {
		Control control = getControl();
		control.pack();
		setWidth(control.getSize().x);
	}

	protected void checkSubclass() {
		// Force subclassing
	}
}

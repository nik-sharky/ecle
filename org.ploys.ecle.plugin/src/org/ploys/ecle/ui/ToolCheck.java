package org.ploys.ecle.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolCheck extends ToolItem {
	Image imgEnable, imgHot;

	public ToolCheck(ToolBar parent) {
		super(parent, SWT.CHECK);
	}

	public void setImage(Image image) {
		imgEnable = image;
		super.setImage(image);
	}

	public void setHotImage(Image image) {
		imgHot = image;
		super.setHotImage(image);
	}

	public void setSelection(boolean state) {
		super.setSelection(state);
		if (isEnabled())
			super.setImage(state ? imgHot : imgEnable);
	}

	public void select(boolean state) {
		setSelection(state);
		notifyListeners(SWT.Selection, new Event());
		ToolBar parent = getParent();
		if (parent != null)
			parent.update();
	}
	
	protected void checkSubclass() {
		// Force subclassing
	}

}

package org.ploys.eclipse.embed.terminal;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

public class StatusBar extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StatusBar(Composite parent, int style) {
		super(parent, style);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBounds(189, 126, 55, 15);
		lblNewLabel.setText("New Label");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

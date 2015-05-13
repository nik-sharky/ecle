package org.ploys.ecle.terminal;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.ploys.ecle.Activator;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class TerminalPrefs extends PreferencePage implements IWorkbenchPreferencePage {
	private Text text;
	//private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	public TerminalPrefs() {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite page = new Composite(parent, SWT.NONE);
		page.setLayout(new GridLayout(3, false));
		
		Label lblPort = new Label(page, SWT.NONE);
		lblPort.setText("Port");
		new Label(page, SWT.NONE);
		
		text = new Text(page, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		return page;
	}

	@Override
	public void init(IWorkbench workbench) {
	}
}

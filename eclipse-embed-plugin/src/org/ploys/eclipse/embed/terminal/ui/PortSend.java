package org.ploys.eclipse.embed.terminal.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.ploys.eclipse.embed.ui.Icons;

public class PortSend {
	private PortSendHandler handler;

	private Combo editor;
	ToolItem fDuplex;
	Button aSend, fCR, fLF;

	public PortSend(PortSendHandler handler) {
		this.handler = handler;
	}

	public void createUI(Composite parent) {
		ToolBar toolBar_3 = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		fDuplex = new ToolItem(toolBar_3, SWT.CHECK);
		fDuplex.setToolTipText("Duplex mode");
		fDuplex.setHotImage(Icons.ico("monitor"));
		fDuplex.setImage(Icons.ico("monitor-off"));

		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(4, false);
		gl_composite_1.marginHeight = 0;
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);

		editor = new Combo(composite_1, SWT.BORDER);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		aSend = new Button(composite_1, SWT.NONE);
		aSend.setText("Send");

		fCR = new Button(composite_1, SWT.TOGGLE);
		fCR.setText("+CR");

		fLF = new Button(composite_1, SWT.TOGGLE);
		fLF.setText("+LF");

		initListeners();
	}

	private void initListeners() {
		aSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sendData();
			}
		});

		editor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.KEYPAD_CR || e.keyCode == SWT.CR) {
					sendData();
				}
			}
		});
	}

	private void sendData() {
		String cr = fCR.getSelection() ? "\r" : "";
		String lf = fLF.getSelection() ? "\n" : "";
		String data = editor.getText() + cr + lf;
		handler.sendData(data.getBytes(), fDuplex.getSelection());
	}
}

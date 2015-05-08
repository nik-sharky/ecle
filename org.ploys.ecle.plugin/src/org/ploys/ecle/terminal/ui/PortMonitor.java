package org.ploys.ecle.terminal.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.ploys.ecle.ui.Icons;

//TODO Context menu to styled text
//TODO Switch view modes
public class PortMonitor {
	private StyledText eMonitor;
	ToolItem fClearOnStart;

	enum Action {
		CLEAR, HOME, END, SAVE, CLEARONSTART
	}

	void doMonitorAction(Action action, boolean state) {
		switch (action) {
		case CLEAR:
			clear();
			break;
		case HOME:
			eMonitor.setTopIndex(0);
			break;
		case END:
			if (state) {
				eMonitor.setTopIndex(eMonitor.getLineCount() - 1);
				eMonitor.addModifyListener(scrollListener);
			} else {
				eMonitor.removeModifyListener(scrollListener);
			}
			break;
		case SAVE:
			break;
		}

	}

	public boolean isClearOnStart() {
		return fClearOnStart != null && fClearOnStart.getSelection();
	}
	
	public void clear() {
		eMonitor.setText("");
	}

	ModifyListener scrollListener = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			eMonitor.setTopIndex(eMonitor.getLineCount() - 1);
		}
	};

	public void createUI(Composite parent) {
		ToolBar tbMonitor = new ToolBar(parent, SWT.FLAT | SWT.RIGHT | SWT.VERTICAL);
		tbMonitor.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

		fClearOnStart = createButton(tbMonitor, Action.CLEARONSTART, "Clear on session start (port opened or RST twitched)", "terminal-clear");
		createButton(tbMonitor, Action.CLEAR, "Clear", "eraser");

		// ToolItem save = createButton(tbMonitor, Action.SAVE,
		// "Save content to file...", "disk");
		// save.setEnabled(false);

		createButton(tbMonitor, Action.HOME, "Scroll to home", "atop");
		ToolItem se = createButton(tbMonitor, Action.END, "Scroll to end", "abottom");

		eMonitor = new StyledText(parent, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		eMonitor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		eMonitor.setRightMargin(3);
		eMonitor.setBottomMargin(3);
		eMonitor.setTopMargin(3);
		eMonitor.setLeftMargin(3);
		eMonitor.setText("Embed plugin serial terminal\r\n");

		se.setSelection(true);
		doMonitorAction(Action.END, true);
	}

	private ToolItem createButton(ToolBar tb, final Action action, String tip, String ico) {
		int style = SWT.NONE;

		switch (action) {
		case CLEARONSTART:
		case END:
			style = SWT.CHECK;
			break;
		}

		final ToolItem tbItem = new ToolItem(tb, style);

		tbItem.setToolTipText(tip);
		tbItem.setImage(Icons.ico(ico));
		tbItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doMonitorAction(action, tbItem.getSelection());
			}
		});

		return tbItem;
	}

	public void append(String data) {
		eMonitor.append(data);
		// eMonitor.append(" ");
	}
}

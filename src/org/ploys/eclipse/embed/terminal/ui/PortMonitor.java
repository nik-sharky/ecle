package org.ploys.eclipse.embed.terminal.ui;

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
import org.ploys.eclipse.embed.ui.Icons;

//TODO Context menu to styled text
//TODO Switch view modes
public class PortMonitor {
	private StyledText eMonitor;

	enum Action {
		CLEAR, HOME, END, SAVE
	}

	void doMonitorAction(Action action, ToolItem item) {
		switch (action) {
		case CLEAR:
			eMonitor.setText("");
			break;
		case HOME:
			eMonitor.setTopIndex(0);
			break;
		case END:
			if (item.getSelection()) {
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

	ModifyListener scrollListener = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			eMonitor.setTopIndex(eMonitor.getLineCount() - 1);
		}
	};

	public void createUI(Composite parent) {
		ToolBar tbMonitor = new ToolBar(parent, SWT.FLAT | SWT.RIGHT | SWT.VERTICAL);
		tbMonitor.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

		createButton(tbMonitor, Action.SAVE, "Save content to file...", "disk");
		createButton(tbMonitor, Action.HOME, "Scroll to home", "atop");
		createButton(tbMonitor, Action.END, "Scroll to end", "abottom");
		createButton(tbMonitor, Action.CLEAR, "Clear", "eraser");

		eMonitor = new StyledText(parent, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		eMonitor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		eMonitor.setRightMargin(3);
		eMonitor.setBottomMargin(3);
		eMonitor.setTopMargin(3);
		eMonitor.setLeftMargin(3);
		eMonitor.setText("Embed plugin serial terminal\r\n");
	}

	private void createButton(ToolBar tb, final Action action, String tip, String ico) {
		int style = action == Action.END ? SWT.CHECK : SWT.NONE;

		final ToolItem tbItem = new ToolItem(tb, style);

		tbItem.setToolTipText(tip);
		tbItem.setImage(Icons.ico(ico));
		tbItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doMonitorAction(action, tbItem);
			}
		});
	}

	public void append(String data) {
		eMonitor.append(data);
		eMonitor.append(" ");
	}
}

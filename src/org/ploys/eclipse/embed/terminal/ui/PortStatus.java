package org.ploys.eclipse.embed.terminal.ui;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.ploys.eclipse.embed.common.State;
import org.ploys.eclipse.embed.terminal.ui.PortStatus.Led;
import org.ploys.eclipse.embed.ui.Icons;

public class PortStatus {
	public enum Led {
		RXD, TXD, CTS, DCD, DSR, RNG, BREAK, ERROR;
	}

	private CLabel lStatus;

	private Map<Led, CLabel> leds = new EnumMap<>(Led.class);

	public void setLed(Led led, State state) {
		CLabel lc = getElement(led);
		if (lc == null)
			return;

		lc.setImage(Icons.state(state));
		lc.update();
	}

	public void setLed(Led led, boolean state) {
		State onState = State.ON;
		switch (led) {
		case BREAK:
			onState = State.WAIT;
			break;
		case ERROR:
			onState = State.ERROR;
			break;
		}
		setLed(led, state ? onState : State.OFF);
	}

	public void setBreak(boolean brk) {
		setLed(Led.BREAK, brk);
	}

	public void setError(int error) {
		setLed(Led.ERROR, error != 0);
	}

	public void resetLeds() {
		for (Led led : Led.values())
			setLed(led, State.OFF);
	}

	public void setPortStatus(String str) {
		lStatus.setText(str);
	}
	
	/**
	 * Set line status. Lines status is sent as 0 - OFF and 1 - ON
	 *
	 * <br>
	 * <b>element 0</b> - <b>CTS</b> line state</br> <br>
	 * <b>element 1</b> - <b>DSR</b> line state</br> <br>
	 * <b>element 2</b> - <b>RING</b> line state</br> <br>
	 * <b>element 3</b> - <b>RLSD</b> line state</br>
	 */
	public void setLinesStatus(int[] status) {
		if (status == null || status.length < 4) {
			System.err.println("Incorrect lines status");
			return;
		}

		setLed(Led.CTS, status[0] == 1);
		setLed(Led.DSR, status[1] == 1);
		setLed(Led.RNG, status[2] == 1);
		setLed(Led.DCD, status[3] == 1);
	}

	public void createUI(Composite cParent) {
		Composite pStatus = new Composite(cParent, SWT.NO_FOCUS);
		pStatus.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridLayout gl_pStatus = new GridLayout(4, false);
		gl_pStatus.marginTop = 1;
		gl_pStatus.marginBottom = 2;
		gl_pStatus.marginHeight = 0;
		gl_pStatus.verticalSpacing = 0;
		pStatus.setLayout(gl_pStatus);
		pStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lStatus = new CLabel(pStatus, SWT.NONE);
		lStatus.setImage(Icons.ico("plug-disconnect"));
		lStatus.setLeftMargin(2);
		lStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lStatus.setRightMargin(5);
		lStatus.setTopMargin(2);
		lStatus.setBottomMargin(1);
		lStatus.setText("Port: Closed");

		createLeds(pStatus);
	}

	protected void createLeds(Composite parent) {
		Composite pSigstate = new Composite(parent, SWT.NONE);
		GridLayout gl_pSigstate = new GridLayout(6, true);
		gl_pSigstate.marginHeight = 0;
		pSigstate.setLayout(gl_pSigstate);

		createLed(pSigstate, Led.RXD);
		createLed(pSigstate, Led.TXD);

		createLed(pSigstate, Led.CTS);
		createLed(pSigstate, Led.DCD);
		createLed(pSigstate, Led.DSR);
		createLed(pSigstate, Led.RNG);

		CLabel led = createLed(parent, Led.BREAK);
		led.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		led.setLeftMargin(20);

		led = createLed(parent, Led.ERROR);
		led.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		led.setRightMargin(2);
		led.setLeftMargin(10);
	}

	protected CLabel createLed(Composite parent, Led led) {
		CLabel element = new CLabel(parent, SWT.NONE);
		element.setTopMargin(1);
		element.setRightMargin(0);
		element.setBottomMargin(1);
		element.setLeftMargin(0);
		element.setText(led.name());
		element.setImage(Icons.state(State.OFF));

		leds.put(led, element);
		return element;
	}

	private CLabel getElement(Led led) {
		return leds.get(led);
	}
}

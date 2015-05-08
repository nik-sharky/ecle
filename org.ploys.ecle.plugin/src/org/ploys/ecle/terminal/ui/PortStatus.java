package org.ploys.ecle.terminal.ui;

import java.util.EnumMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.ploys.ecle.common.Serial;
import org.ploys.ecle.common.State;
import org.ploys.ecle.ui.Icons;

public class PortStatus {
	private CLabel lStatus;

	private Map<Serial.Pin, CLabel> leds = new EnumMap<>(Serial.Pin.class);

	public void setLed(Serial.Pin led, State state) {
		CLabel lc = getElement(led);
		if (lc == null)
			return;

		lc.setImage(Icons.state(state));
		lc.update();
	}

	public void setLed(Serial.Pin led, boolean state) {
		State onState = State.ON;
		switch (led) {
		case BREAK:
			onState = State.WAIT;
			break;
		case ERROR:
			onState = State.ERROR;
			break;
		default:
			break;
		}
		setLed(led, state ? onState : State.OFF);
	}

	public void setBreak(boolean brk) {
		setLed(Serial.Pin.BREAK, brk);
	}

	public void setError(int error) {
		setLed(Serial.Pin.ERROR, error != 0);
	}

	public void resetLeds() {
		for (Serial.Pin led : Serial.Pin.statusValues())
			setLed(led, State.OFF);
	}

	public void setPortStatus(String str, boolean connected) {
		lStatus.setText(str);
		lStatus.setImage(connected ? Icons.state(State.ON) : Icons.ico("plug-disconnect"));
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

		setLed(Serial.Pin.CTS, status[0] == 1);
		setLed(Serial.Pin.DSR, status[1] == 1);
		setLed(Serial.Pin.RNG, status[2] == 1);
		setLed(Serial.Pin.DCD, status[3] == 1);
	}

	public void createUI(Composite cParent) {
		Label spcStatus = new Label(cParent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		spcStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

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
		lStatus.setLeftMargin(3);
		lStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lStatus.setRightMargin(5);
		lStatus.setTopMargin(2);
		lStatus.setBottomMargin(1);

		setPortStatus("Port closed", false);

		createLeds(pStatus);
	}

	protected void createLeds(Composite parent) {
		Composite pSigstate = new Composite(parent, SWT.NONE);
		GridLayout gl_pSigstate = new GridLayout(6, true);
		gl_pSigstate.marginHeight = 0;
		pSigstate.setLayout(gl_pSigstate);

		createLed(pSigstate, Serial.Pin.RXD);
		createLed(pSigstate, Serial.Pin.TXD);

		createLed(pSigstate, Serial.Pin.CTS);
		createLed(pSigstate, Serial.Pin.DCD);
		createLed(pSigstate, Serial.Pin.DSR);
		createLed(pSigstate, Serial.Pin.RNG);

		CLabel led = createLed(parent, Serial.Pin.BREAK);
		led.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		led.setLeftMargin(20);

		led = createLed(parent, Serial.Pin.ERROR);
		led.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		led.setRightMargin(2);
		led.setLeftMargin(10);
	}

	protected CLabel createLed(Composite parent, Serial.Pin led) {
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

	private CLabel getElement(Serial.Pin led) {
		return leds.get(led);
	}
}

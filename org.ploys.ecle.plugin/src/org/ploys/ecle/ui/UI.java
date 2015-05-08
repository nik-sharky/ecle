package org.ploys.ecle.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.ploys.ecle.Activator;

public class UI {
	public static void runAsync(Runnable runnable) {
		Display.getDefault().asyncExec(runnable);
	}

	public static void runSync(Runnable runnable) {
		Display.getDefault().syncExec(runnable);
	}

	public static void errorDialog(int mode, String message, Exception e) {
		e.printStackTrace();
		
		IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e);
		StatusManager.getManager().handle(new StatusAdapter(status), mode);
	}
}

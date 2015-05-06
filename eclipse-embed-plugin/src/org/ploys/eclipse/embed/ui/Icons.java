package org.ploys.eclipse.embed.ui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.ploys.eclipse.embed.common.State;

public class Icons {
	static String baseDir = "icons/full";

	private static String getPluginID() {
		return Activator.PLUGIN_ID;
	}

	public static Image ico(String path, String part) {
		return ResourceManager.getPluginImage(getPluginID(), baseDir + "/" + (part == null ? "" : part + "/") + path + ".png");
	}

	public static Image ico(String path) {
		return ico(path, "eview16");
	}

	public static Image state(State state) {
		return ico("state/" + state.icoName());
	}

}

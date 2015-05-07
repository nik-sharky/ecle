package org.ploys.ecle.prefs;

/**
 * Constant definitions for plug-in preferences
 */
public class PrefKeys {

	public static interface Path {
		public static final String SDK = "prefPathSDK";
		public static final String COMPILER = "prefPathCompiler";
		public static final String TOOLS = "prefPathTools";
	}

	public static interface Serial {
		public static final String PORT = "prefSerialPort";
		public static final String SPEED = "prefSerialSpeed";
		public static final String PARITY = "prefSerialParity";
		public static final String DATABITS = "prefSerialDataBits";
		public static final String STOPBITS = "prefSerialStopBits";
		public static final String HWFLOWCTL = "prefSerialHWFlowCtl";
		public static final String SWFLOWCTL = "prefSerialSWFlowCtl";

		public static final String[][] VPARITY = { { "None", "N" }, { "Odd", "O" }, { "Even", "E" }, { "Mark", "M" }, { "Space", "S" } };
		public static final String[][] VDATABITS = { { "8", "8" }, { "7", "7" }, { "6", "6" }, { "5", "5" } };
		public static final String[][] VSTOPBITS = { { "1", "1" }, { "2", "2" } };

	}

	public static interface Sming {
		public static final String PATH = "prefPathSming";
		public static final String PATH_LIBS = "prefPathSmingLibs";

	}
	/*
	 * public static final String P_PATH = "pathPreference"; public static final
	 * String P_BOOLEAN = "booleanPreference"; public static final String
	 * P_CHOICE = "choicePreference"; public static final String P_STRING =
	 * "stringPreference";
	 */

}

package org.ploys.ecle.common;

public enum State {
	OFF("gray"), ON("green"), ERROR("red"), WAIT("yellow");

	private String icoName;

	private State(String icoName) {
		this.icoName = icoName;
	}

	public String icoName() {
		return icoName;
	}
}

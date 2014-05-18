package com.javadaemon.webserver.request;

public class Request {
	
	private METHOD method;
	private String URI;
	private String version;
	
	public Request(METHOD method, String URI, String version) {
		this.method = method;
		this.URI = URI;
		this.version = version;
	}

	public METHOD getMethod() {
		return method;
	}

	public String getURI() {
		return URI;
	}

	public String getVersion() {
		return version;
	}
}

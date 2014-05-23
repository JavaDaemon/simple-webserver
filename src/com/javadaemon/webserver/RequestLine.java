package com.javadaemon.webserver;

import java.io.File;

/**
 * Representation of the Request-Line in an HTTP request.
 * 
 * @author Mads Peter Horndrup
 */
public class RequestLine {
	
	public enum METHOD {
		GET,
		POST,
		UNKNOWN,
		;
	}
	
	private METHOD method;
	private File uri;
	private String version;
	
	public RequestLine(METHOD method, File uri, String version) {
		this.method = method;
		this.uri = uri;
		this.version = version;
	}
	
	public METHOD getMethod() {
		return method;
	}
	
	public File getURI() {
		return uri;
	}
	
	public String getVersion() {
		return version;
	}
}

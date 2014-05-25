package com.javadaemon.webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import com.javadaemon.webserver.RequestLine.METHOD;

public class ServerUtil {
	
	/**
	 * Will parse the HTTP Request-Line from the given requestData. 
	 * The requestData has to be the entire Request-Line as specified in the <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1">HTTP/1.1 specification</a>, as read from the stream.
	 * 
	 * TODO: Maybe this should not check if the file exists??
	 * XXX: Possible security problem: URI can link backwards, into the system running the webserver.
	 * 
	 * @param requestData				Bytes containing the Request-Line.
	 * @return							The Request-Line object.
	 * @throws IllegalArgumentException	Happens if the Request-Line could not be determined from the input provided.
	 * @throws FileNotFoundException	Happens if the server could not find the file asked for.
	 */
	public static RequestLine parseRequest(byte[] requestData) throws IllegalArgumentException, FileNotFoundException {
		if (requestData.length == 0) {
			throw new IllegalArgumentException("Empty request-line.");
		}
		String request;
		try {
			request = new String(requestData, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError("US-ASCII was not recognized as a character encoding.");
		}
		String[] tokens = request.trim().split(" ");
		if (tokens.length != 3) {
			throw new IllegalArgumentException("Unable to recognize tokens on request-line.");
		}
		METHOD method;
		try {
			method = METHOD.valueOf(tokens[0]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unrecognized method");
		}
		File file = new File("site"+tokens[1]);
		String version = tokens[2];
		return new RequestLine(method, file, version);
	}
}

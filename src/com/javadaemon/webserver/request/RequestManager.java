package com.javadaemon.webserver.request;

import java.net.Socket;

import com.javadaemon.webserver.requesthandler.GETHandler;
import com.javadaemon.webserver.requesthandler.RequestHandler;

/**
 * 
 * 
 * @author Mads Peter Horndrup
 */
public class RequestManager {
	
	public RequestHandler getHandler(Socket clientSocket, Request request) {
		switch(request.getMethod()) {
			default:
			case GET:
				return new GETHandler(clientSocket, request);
		}
	}

}

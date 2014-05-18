package com.javadaemon.webserver.requesthandler;

import java.net.Socket;

import com.javadaemon.webserver.request.Request;

public abstract class RequestHandler implements Runnable {
	
	private Socket clientSocket;
	private Request request;
	
	public RequestHandler(Socket clientSocket, Request request) {
		this.clientSocket = clientSocket;
		this.request = request;
	}

	@Override
	public abstract void run();
	
	/**
	 * @return	The connected client's socket.
	 */
	public Socket getSocket() {
		return clientSocket;
	}
	
	/**
	 * @return	The request as sent by the client
	 */
	public Request getRequest() {
		return request;
	}
}

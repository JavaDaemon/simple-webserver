package com.javadaemon.webserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.javadaemon.webserver.RequestLine.METHOD;

/**
 * Runs on a seperate Thread. This processes the actual request.
 * 
 * @author Mads Peter Horndrup
 */
public class WorkerRunnable implements Runnable {
	
	private Socket clientSocket;
	
	public WorkerRunnable(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			
			byte[] requestData = new byte[8192];
			for (int i = 0; i < 8192; i++) {
				int inByte = input.read();
				if (inByte == 10) { // LF (ASCII = 10) signifies end of request-line.
					break;
				}
				if (inByte == -1) { // End of stream.
					/*
					 * TODO: Error handling for wrong request, since there was not LF.
					 */
					break;
				}
				requestData[i] = (byte) (inByte & 0xFF);
			}
			// InputStream is now at headers.
			
			RequestLine request;
			try {
				request = ServerUtil.parseRequest(requestData);
			} catch (IllegalArgumentException e) {
				closeConnection();
				return;
			}
			
			if (!request.getURI().exists() || request.getURI().isDirectory()) {
				/*
				 * Return a File not Found error.
				 */
				closeConnection();
			}
			
			if (request.getMethod() == METHOD.GET) {
				RequestHandler.handleGet(request, output);
			}
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void closeConnection() {
		try {
			clientSocket.getInputStream().close();
			clientSocket.getOutputStream().close();
			clientSocket.close();
		} catch (IOException e) {
			/*
			 * Flushing failed. Oh well. The stream should be all flushed before it's closed anyway.
			 */
		}
	}
}

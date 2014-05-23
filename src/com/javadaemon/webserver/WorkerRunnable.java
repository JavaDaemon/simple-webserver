package com.javadaemon.webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
			
			// TODO: Check if this is throwing an error. It really might.
			RequestLine request = ServerUtil.parseRequest(requestData);
			
			/*
			 * Here, a response could be sent through the OutputStream.
			 */
			
			input.close();
			output.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

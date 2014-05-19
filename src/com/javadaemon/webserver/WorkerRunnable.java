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
			
			byte[] method = new byte[8];
			for (int i = 0; i < method.length; i++) {
				int data = input.read();
				if (data == 32) { // Space signifies that method-field has ended
					break;
				}
				method[i] = (byte)(0xFFFF ^ data);
			}
			String methodString = new String(method, "US-ASCII");
			/*
			 * 
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

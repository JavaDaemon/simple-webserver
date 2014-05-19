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
			
			/*
			 * FIXME: Instead of doing this, read the entire request.
			 */
			byte[] method = new byte[8];
			for (int i = 0; i < method.length; i++) {
				int data = input.read();
				if (data == 32) { // Space signifies that the method-field has ended
					break;
				}
				method[i] = (byte)(0xFF & data);
			}
			String methodString = new String(method, "US-ASCII");
			switch (methodString.trim()) {
			case "GET":
				handleGet();
				break;
			case "POST":
			case "OPTIONS":
			case "HEAD":
			case "PUT":
			case "DELETE":
			case "CONNECT":
			case "TRACE":
			default:
				handleDefault();
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called upon a GET request
	 */
	private void handleGet() {
		System.out.println("GET RECEVIED!");
		closeConnection();
	}
	
	/**
	 * Called when no known method was requested
	 */
	private void handleDefault() {
		closeConnection();
	}
	
	private void closeConnection() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package com.javadaemon.webserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
			} catch (FileNotFoundException e) {
				closeConnection();
				return;
			} catch (IllegalArgumentException e) {
				closeConnection();
				return;
			}
			
			if (request.getMethod() == METHOD.GET) {
				output.write("HTTP/1.1 200 OK\n".getBytes("US-ASCII"));
				output.write(("Content-Length: "+request.getURI().length()+"\n").getBytes("US-ASCII"));
				output.write("\r\n".getBytes("US-ASCII")); // \r\n converts to CRLF
				output.flush();
				/*
				 * XXX: DataInputStream is really slow without a BufferedInputStream in the middle.
				 * Actually, it's really darn slow anyway.
				 */
				DataInputStream dataStream = new DataInputStream(new BufferedInputStream(new FileInputStream(request.getURI()), 2048));
				for (int i = 0; i < request.getURI().length(); i++) {
					output.write(dataStream.readByte());
				}
				output.flush();
				dataStream.close();
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

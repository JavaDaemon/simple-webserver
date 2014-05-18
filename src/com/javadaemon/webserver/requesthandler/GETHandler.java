package com.javadaemon.webserver.requesthandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.javadaemon.webserver.request.Request;

/**
 * An example GET-Handler, that will only serve pictures of bananas.
 * 
 * @author Mads Peter Horndrup
 */
public class GETHandler extends RequestHandler {
	
	public GETHandler(Socket clientSocket, Request request) {
		super(clientSocket, request);
	}

	public void run() {
		try {
			InputStream input = getSocket().getInputStream();
			OutputStream output = getSocket().getOutputStream();
			
			File file = new File("pic/banana.jpg");
			long length = file.length();
			FileInputStream imageStream = new FileInputStream(file);

			String headers = "HTTP/1.1 200 OK \n"
							+ "Content-Type: image/jpeg \n"
							+ "Content-Length: "+length+"\n\n";
			
			output.write(headers.getBytes("US-ASCII"));
			
			for (long i = 0; i < length; i++) {
				int data = imageStream.read();
				output.write(data);
			}
			output.flush();
			
			imageStream.close();
			output.close();
			input.close();
			
			System.out.println("Banana served to client: "+getSocket().getInetAddress().toString());
		} catch (IOException e) {
			/*
			 * We should probably inform the others...
			 */
			e.printStackTrace();
		}
	}
}

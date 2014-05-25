package com.javadaemon.webserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class RequestHandler {
	
	public static void handleGet(RequestLine request, OutputStream output) throws UnsupportedEncodingException, IOException {
		output.write("HTTP/1.1 200 OK\n".getBytes("US-ASCII"));
		output.write(("Content-Length: "+request.getURI().length()+"\n").getBytes("US-ASCII"));
		output.write("\r\n".getBytes("US-ASCII")); // \r\n converts to CRLF
		output.flush();
		
		/*
		 * The following is an attempt at speeding up the I/O by using buffers. It did not work, unfortunately.
		 */
		/*if (request.getURI().length() > Integer.MAX_VALUE) { // Too large file for buffer.
			DataInputStream dataStream = new DataInputStream(new BufferedInputStream(new FileInputStream(request.getURI()), 2048));
			for (int i = 0; i < request.getURI().length(); i++) {
				output.write(dataStream.readByte());
			}
			output.flush();
			dataStream.close();
		} else {
			byte[] outData = new byte[(int) request.getURI().length()];
			InputStream stream = new FileInputStream(request.getURI());
			stream.read(outData, 0, (int)request.getURI().length());
			output.write(outData);
			output.flush();
			stream.close();
		}*/
		
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

}

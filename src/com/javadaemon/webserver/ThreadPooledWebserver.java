package com.javadaemon.webserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.javadaemon.webserver.request.Request;
import com.javadaemon.webserver.request.METHOD;
import com.javadaemon.webserver.request.RequestManager;

public class ThreadPooledWebserver implements Runnable {
	
	protected int serverPort;
    protected ServerSocket serverSocket;
    
    protected ExecutorService threadPool = Executors.newCachedThreadPool();
    protected RequestManager manager = new RequestManager();
    
    protected volatile boolean isStopped = false;

    public ThreadPooledWebserver(int port){
        this.serverPort = port;
    }

    public void run(){
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            byte[] requestArray = new byte[256];
            try {
            	for (int i = 0; i < 256; i++) {
            		requestArray[i] = (byte) clientSocket.getInputStream().read();
            		if (requestArray[i] == 10) { // Linefeed encountered!
            			break;
            		}
            	}
            } catch (IOException e) {
            	// FIXME: Proper error handling
            	e.printStackTrace();
            }
            String requestLine = "";
			try {
				requestLine = new String(requestArray, "US-ASCII");
			} catch (UnsupportedEncodingException e) {
				// FIXME: Proper error handling
				// This is VERY unlikely to happen, as "US-ASCII" is always used
				e.printStackTrace();
			}
            String[] request = requestLine.trim().split(" ");
            Request httpRequest = new Request(METHOD.valueOf(request[0]), request[1], request[2]);
            threadPool.execute(manager.getHandler(clientSocket, httpRequest));
        }
        System.out.println("Server Stopped.") ;
    }


    private boolean isStopped() {
        return this.isStopped;
    }

    public void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port "+serverPort, e);
        }
    }

}
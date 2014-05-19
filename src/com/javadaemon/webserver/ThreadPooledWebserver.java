package com.javadaemon.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A threadpooled webserver, listenening for connections as much as possible.
 * Whenever a connection is made, it is quickly handed off to the threadpool.
 * 
 * @author Mads Peter Horndrup
 */
public class ThreadPooledWebserver implements Runnable {
	
	protected int serverPort;
    protected ServerSocket serverSocket;
    
    protected ExecutorService threadPool = Executors.newCachedThreadPool();
    
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
            threadPool.execute(new WorkerRunnable(clientSocket));
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
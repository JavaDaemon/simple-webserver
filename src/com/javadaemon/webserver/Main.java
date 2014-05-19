package com.javadaemon.webserver;

/**
 * Entry point of webserver. Might want to stick a GUI in there.
 * 
 * @author Mads Peter Horndrup
 */
public class Main {
	
	public static void main(String[] args) {
		ThreadPooledWebserver server = new ThreadPooledWebserver(80);
		new Thread(server).start();
	}
}

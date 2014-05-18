package com.javadaemon.webserver;

public class Main {
	
	public static void main(String[] args) {
		ThreadPooledWebserver server = new ThreadPooledWebserver(80);
		new Thread(server).start();
	}

}

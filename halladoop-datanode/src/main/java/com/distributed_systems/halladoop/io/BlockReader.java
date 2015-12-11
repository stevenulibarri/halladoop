package com.distributed_systems.halladoop.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class BlockReader implements Runnable {
	private Socket socket;

	public BlockReader(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Operation op = (Operation) inputStream.readObject();
			switch(op){
			case READ:
				break;
			case WRITE:
				break;
			case DELETE:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

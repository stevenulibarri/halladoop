package com.distributed_systems.halladoop.io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.distributed_systems.halladoop.dataNode.model.Operation;
import com.distributed_systems.halladoop.dataNode.model.ReadData;

public class BlockReader implements Runnable {
	private Socket socket;

	public BlockReader(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			Operation op = (Operation) inputStream.readObject();
			switch(op){
			case READ:
				ReadData data = (ReadData) inputStream.readObject();
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
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

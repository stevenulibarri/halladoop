package com.distributed_systems.halladoop.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class BlockReader implements Runnable{
	private Socket socket;
	
	public BlockReader(Socket socket){
		this.socket = socket;
	}
	
	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int returnResult = 0;
			while(returnResult != -1){
				returnResult = inputStream.read();
				outputStream.write(returnResult);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

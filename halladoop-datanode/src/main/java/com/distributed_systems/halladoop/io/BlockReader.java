package com.distributed_systems.halladoop.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.distributed_systems.halladoop.dataNode.App;
import com.distributed_systems.halladoop.dataNode.model.Operation;
import com.distributed_systems.halladoop.dataNode.model.ReadData;
import com.distributed_systems.halladoop.dataNode.model.WriteData;

public class BlockReader implements Runnable {
	private Socket socket;

	public BlockReader(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			Operation op = (Operation) inputStream.readObject();
			switch (op) {
			case READ:
				ReadData readData = (ReadData) inputStream.readObject();
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				if (App.getFiles().containsKey(readData.getBlockId())) {
					File file = new File(App.getFiles().get(readData.getBlockId()));
					FileInputStream fileInput = new FileInputStream(file);
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					int nextInput = 0;
					while (nextInput != -1) {
						nextInput = fileInput.read();
						buffer.write(nextInput);
					}
					outputStream.writeObject(buffer.toByteArray());
					fileInput.close();
					outputStream.flush();
					outputStream.close();
					inputStream.close();

				} else {
					throw new FileNotFoundException();
				}
				break;
			case WRITE:
				WriteData writeData = (WriteData) inputStream.readObject();
				try {
					String path = App.getNextPath(writeData.getBlockId());
					File writeFile = new File(path);
					writeFile.mkdir();
					FileOutputStream fileOutput = new FileOutputStream(writeFile);
					fileOutput.write(writeData.getData());
					fileOutput.flush();
					fileOutput.close();

					ObjectOutputStream writeOutputStream = new ObjectOutputStream(socket.getOutputStream());
					writeOutputStream.writeObject(true);
					writeOutputStream.flush();
					writeOutputStream.close();
				} catch (Exception e) {
					ObjectOutputStream writeOutputStream = new ObjectOutputStream(socket.getOutputStream());
					writeOutputStream.writeObject(false);
					writeOutputStream.flush();
					writeOutputStream.close();
				}
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

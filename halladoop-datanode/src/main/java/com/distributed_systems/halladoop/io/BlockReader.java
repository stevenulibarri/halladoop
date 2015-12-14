package com.distributed_systems.halladoop.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

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
			//ObjectMapper mapper = new ObjectMapper();
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			//Operation op = mapper.readValue(getNextData(socket.getInputStream()), Operation.class);
			Operation op = (Operation) inputStream.readObject();
			switch (op) {
			case READ:
				//ReadData readData = mapper.readValue(socket.getInputStream(), ReadData.class);
				ReadData readData = (ReadData) inputStream.readObject();
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				Map<String, String> map = App.getFiles();
				if (App.getFiles().containsKey(readData.getBlockId())) {
					String path = App.getFiles().get(readData.getBlockId());
					File file = new File(path);
					FileInputStream fileInput = new FileInputStream(file);
					byte[] fileData = new byte[(int) file.length()];
					fileInput.read(fileData);
					outputStream.writeObject(fileData);
					fileInput.close();
					outputStream.flush();
					outputStream.close();
					//inputStream.close();

				} else {
					throw new FileNotFoundException();
				}
				break;
			case WRITE:
				WriteData writeData = (WriteData) inputStream.readObject();
				try {
					String path = App.getNextPath(writeData.getBlockId());
					File writeFile = new File(path);
					writeFile.mkdirs();
					FileOutputStream fileOutput = new FileOutputStream(writeFile + File.separator + writeData.getBlockId() + ".bin");
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
					e.printStackTrace();
				}
				break;
			case DELETE:
				ReadData deleteData = (ReadData) inputStream.readObject();
				//ReadData deleteData = mapper.readValue(socket.getInputStream(), ReadData.class);
				if (App.getFiles().containsKey(deleteData.getBlockId())) {
					File file = new File(App.getFiles().get(deleteData.getBlockId()));
					file.delete();
				}
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
	
	private byte[] getNextData(InputStream is){
		int readValue;
		try {
			readValue = is.read();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while(readValue != -1){
				readValue = is.read();
				baos.write(readValue);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

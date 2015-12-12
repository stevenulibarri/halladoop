package com.distributed_systems.halladoop.client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.codehaus.jackson.map.ObjectMapper;

import com.distributed_systems.halladoop.dataNode.model.Operation;
import com.distributed_systems.halladoop.dataNode.model.ReadData;
import com.distributed_systems.halladoop.dataNode.model.WriteData;

/**
 * Hello world!
 *
 */
public class App 
{
	
	
    public static void main( String[] args )
    {
    	try {
			Socket socket = new Socket("localhost", 4567);
			WriteData writeData = new WriteData();
			writeData.setBlockId("3");
			File file = new File("C:\\test\\kernel7.img");
			byte[] data = new byte[(int)file.length()];
			FileInputStream stream = new FileInputStream(file);
			stream.read(data);
			writeData.setData(data);
//			ObjectMapper mapper = new ObjectMapper();
//			BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
//			Operation op = Operation.WRITE;
//			byte[] opcode = mapper.writeValueAsBytes(op);
//			bos.write(opcode);
//			bos.flush();
//			byte[] serialized = mapper.writeValueAsBytes(writeData);
//			bos.write(serialized);
//			bos.flush();
			Operation op = Operation.WRITE;
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(op);
			oos.flush();
			oos.writeObject(writeData);
			oos.flush();
			boolean t = false;
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			try {
				Object return1 = ois.readObject();
				if(return1 instanceof Boolean){
					System.out.println("Received a boolean " + return1);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			socket.close();
			Socket socket2 = new Socket("localhost", 4567);
			ObjectOutputStream oos2 = new ObjectOutputStream(socket2.getOutputStream());

			ReadData readData = new ReadData();
			readData.setBlockId("3");
			Operation op2 = Operation.READ;
			oos2.writeObject(op2);
			oos2.flush();
			oos2.writeObject(readData);
			oos2.flush();
			try {
				ObjectInputStream ois2 = new ObjectInputStream(socket2.getInputStream());

				Object return2 = ois2.readObject();
				if(return2 instanceof byte[]){
					System.out.println("Received an output stream");
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			socket2.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    // /packetSuccessConf(){}
    // /exception(){}
}

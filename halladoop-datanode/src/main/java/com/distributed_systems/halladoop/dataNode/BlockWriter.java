package com.distributed_systems.halladoop.dataNode;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class BlockWriter implements Runnable{

	private String blockId;
	private Socket socket;
	
	public BlockWriter(String blockId, Socket socket){
		this.blockId = blockId;
		this.socket = socket;
	}
	
	@Override
	public void run() {		
		
	}

}

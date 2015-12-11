**Register**
----
  - Registers a dataNode with the nameNode.
  - Returns an Id that the node will be identified by in the cluster.

* **URL**

  /register/

* **Method:**

  `POST`

* **Data Params**

  - node_ip
  - total_disk_space_mb
  - available_disk_space_mb

* **Sample Call:**

  ```javascript
  {
    "node_ip": "1.1.1.1",
    "total_disk_space_mb": 1337,
    "available_disk_space_mb": 1337
  }
  ```

* **Success Response:**

  * **Code:** 201 CREATED <br />
  * **Content:**
  ```javascript
  {
    "node_id": 1
  }
  ```


**Heartbeat**
----
  - Updates the nameNode of the dataNode's status.
  - Returns an ack may include delete or replicate operations to be executed by the dataNode.

* **URL**

  /heartbeat/

* **Method:**

  `POST`

* **Data Params**

  - node_id
  - available_disk_space_mb
  - block_manifest

* **Sample Call:**
  ```javascript
  {
    "node_id": 1337,
    "available_disk_space_mb": 10,
    "block_manifest": ["block1", "block2", "block3"]
  }
  ```

* **Success Response:**

  * **Code:** 200 <br />
  * **Content:**
  ```javascript
  {
    "delete": ["block1","block2","block3"],
    "replicate": [
      {"block_id": "block123", "nodes": ["1.2.3.4", "2.3.4.5"]}
    ]
  }
  ```
  

**Finalize**
----
  - Informs the nameNode of a completed block write by the client or a completed replicate operation from a dataNode.

* **URL**

  /finalize/

* **Method:**

  `POST`

* **Data Params**

  - block_id
  - nodes

* **Sample Call:**
  ```javascript
  {
    "block_id": "block123",
    "nodes": [1,2]
  }
  ```

* **Success Response:**

  * **Code:** 200 OK <br />
  * **Content:**
  ```javascript
  {
    "delete": ["block1","block2","block3"],
    "replicate": [
      {"block_id": "block123", "nodes": ["1.2.3.4", "2.3.4.5"]}
    ]
  }
  ```


**Write**
----
  - Client request to dataNode to prepare to add a file to the virtual filesystem.
  - Returns a list of dataNodes that will recieve the file's blocks.

* **URL**

  /write/

* **Method:**

  `POST`

* **Data Params**

  - file_path
  - num_blocks

* **Sample Call:**

  ```javascript
  {
    "file_path": "/dir/another_dir/text.txt",
    "num_blocks": 12,
  }
  ```

* **Success Response:**

  * **Code:** 201 CREATED <br />
  * **Content:**
   ```javascript
  {
    "nodes": ["1.1.1.1", "1.2.3.4", "4.4.4.4"]
  }
  ```

* **Error Response:**

  * **Code:** 403 FORBIDDEN <br />
    **Content:** `{ error : "File Exists." }`


**Read**
----
  - Client requests the locations of all blocks for a given file.
  - Returns a list block_id's mapped to a list of dataNodes that contain the block.

* **URL**

  /read/{file_path}

* **Method:**

  `GET`

* **Sample Call:**

  /read/dir/another_dir/text.txt

* **Success Response:**

  * **Code:** 200 OK <br />
  * **Content:**
   ```javascript
  {
  "manifest": [
      {"block_id": "123", "nodes": ["1.2.3.4.", "4.3.2.1","8.8.8.8"]}
    ]
  }
  ```

* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** `{ error : "File not found." }`


**Delete**
----
  - Client requests that a file be deleted from the virtual filesytem.
  - Block deletes will be enacted via heartbeat responses.

* **URL**

  /delete/{file_path}

* **Method:**

  `DELETE`


* **Sample Call:**

  /delete/dir/another_dir/text.txt

* **Success Response:**

  * **Code:** 200 OK <br />

  

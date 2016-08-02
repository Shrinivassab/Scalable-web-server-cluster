/**
 *
 * @author Mohamed
 */

   import java.net.*;
   import java.io.*;
   import java.util.*;
   import javax.swing.*;
   import java.awt.*;
   import java.awt.List;

final class ServCon implements Runnable
{
	final static String CRLF = "\r\n";	
	Socket socket; //Client Socket;
	TextArea stsrpt;
	int port;
	TextField sip;
	Button bt1;
	List acl, szl;

	public ServCon(Socket socket , TextArea stsrpt , List acl , TextField sip , Button bt1, List szl)throws Exception
	{
		this.socket = socket;
		this.stsrpt = stsrpt;
		this.acl = acl;
		this.szl = szl;
		this.sip = sip;
		this.bt1 = bt1;
	}

	public void run()
	{
		try
		{
			processRequest();
		}catch(Exception e){stsrpt.append("\n" + e);}
	}

	public void processRequest()throws Exception
	{
		//acl.add((String.valueOf(socket.getInetAddress()).substring(1)));
		stsrpt.setText("---------------------------------------------------");
		stsrpt.append("Host Address : " + socket.getInetAddress().getHostAddress() + "\n");
		InputStream is = socket.getInputStream() ;
		DataOutputStream os = new DataOutputStream(socket.getOutputStream()) ;
                  	// Set up input stream filters.
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
      
      		// Get the request line of the HTTP request message.
		String requestLine = br.readLine();
		System.out.println("Request Line : " + requestLine);
		// Display the request line.
		stsrpt.append("\n" + requestLine + "\n");
      
		// Extract the filename from the request line.
		StringTokenizer tokens = new StringTokenizer(requestLine);
		String reqType = tokens.nextToken();
		System.out.println("Request Type : " + reqType);
		boolean post = false;
		boolean fileExists = true;
		boolean commandPage = false;
		String command = "";
		FileInputStream fis = null;
		String fileName = null;
		boolean head = false;
		if(reqType.equalsIgnoreCase("POST"))
		{
			post = true;
			fileExists = false;
			String recieved;
			int contentLength = 0;
			while ((recieved = br.readLine()) != null && recieved.length() != 0)
			{
				stsrpt.append(recieved + "\n");
				System.out.println("Received : " +recieved);
				if(recieved.toLowerCase().startsWith("content-length:") && recieved.length() > 16)
				{
					contentLength = Integer.parseInt(recieved.substring(16));
					System.out.println("Content Length disp: " + contentLength);
				}
			}
		
			DataInputStream dis = new DataInputStream(is);
			byte[] b = new byte[contentLength];
			if(dis.read(b) != contentLength)
			{
				System.out.println("Not Equal");
			throw new IOException("Complete content of POST not able to be read\n");
			}
			else
			{
				System.out.println("Data Readed : " + new String(b,0,b.length));
			}
			recieved = new String(b);
			System.out.println("Received Line : " + recieved);
			if(recieved.toLowerCase().startsWith("command"))
			{
				String decoded = URLDecoder.decode(recieved);
				stsrpt.append(decoded + "\n");
				if(decoded.length() > 8)
				{
					command = decoded.substring(8);
					System.out.println("Command Stored : " + command);
				}
			}
		}
		else
		{
		String cReq;
		int flg = 0;
		if(reqType.equalsIgnoreCase("HEAD"))
			head = true;
		fileName = tokens.nextToken();
		System.out.println("Filename : " + fileName);
		cReq = String.valueOf(socket.getInetAddress()).substring(1) + " - " + fileName.substring(1);
		int itmcnt = acl.getItemCount();
		if(itmcnt == 0)
			acl.add(cReq);
		else
		{
		for(int itm = 0 ; itm < itmcnt; itm++)
		{
			if(acl.getItem(itm).equals(cReq))
			{
				//acl.add(cReq);
				break;
			}
			else
			{							
				acl.add(cReq);
				stsrpt.append(cReq);
				break;
			}	
		}
		}
		if(fileName.equalsIgnoreCase("/command") || fileName.equalsIgnoreCase("./command"))
		{
			commandPage = true;
			fileExists = false;
		}
		else
		{
			System.out.println("Retrieving Header Lines");
			if(fileName.startsWith("./"))
			{
				fileName = fileName.substring(2);
			}
			fileName = "." + URLDecoder.decode(fileName);
			// Get and display the header lines.
			String headerLine = null;
			while ((headerLine = br.readLine()).length() !=0) 
			{
				System.out.println(headerLine);
				stsrpt.append(headerLine + "\n");
			}
            
			try {
				fis = new FileInputStream(fileName);
			} 
			catch (FileNotFoundException e) 
			{
				fileExists = false;
			}
		}
		}
		// Construct the response message.
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		if (fileExists) 
		{
			statusLine = "HTTP/1.0 200 ok" + CRLF;
			contentTypeLine = "Content-type: " + 
			contentType( fileName ) + CRLF;
			System.out.println("Content Type : " + contentTypeLine);
		} 
		else if(post)
		{
			statusLine = "HTTP/1.0 201 ok" + CRLF;
			contentTypeLine = "text/html" + CRLF;
		}
		else if(commandPage)
		{
			statusLine = "HTTP/1.0 200 ok" + CRLF;
			contentTypeLine = "text/html" + CRLF;
		}
		else 
		{
			File file;
			if ((file = new File(fileName)).isDirectory())
			{
				statusLine = "HTTP/1.0 200 ok" + CRLF;
				contentTypeLine = "text/html" + CRLF;
				if(!head)
					entityBody = listIndex(file) + CRLF;
			}
			else
			{
				statusLine = "HTTP/1.0 404 Not Found" + CRLF;
				contentTypeLine = "text/html" + CRLF;
				if(!head)
				entityBody = "<HTML>" + 
				"<HEAD><TITLE>Not Found</TITLE></HEAD>" +
				"<BODY>Sorry but the file <i>" + fileName +"</i> could not be found on this server</BODY></HTML>" + CRLF;
			}
		}

		// Send the status line.
		os.writeBytes(statusLine);
		// Send the content type line.
		os.writeBytes(contentTypeLine);
			// Send a blank line to indicate the end of the header lines.
		os.writeBytes(CRLF);
      
      
		// Send the entity body.
		if (fileExists && !head)
		{
			sendBytes(fis, os);
			fis.close();
		}
		else if(commandPage)
		{
			String heading = "Type system command";
			os.writeBytes("<html>\n" +
			"<head><title>" + heading + "</title></head>\n" +
			"<body>\n" + 
			"<form METHOD=POST ACTION=\"\"> \n" +
			"<strong>" + heading + "</strong><br>\n" +
			"<input name=\"command\" TYPE=text SIZE=\"60\"><p>\n" +
			"<input value=\"execute\" TYPE=submit><p>\n" +
			"</form></body>\n" + 	
			"</html>\n");
		} 
		else if(post)
		{
			String s = null;
			try {
			String recieved = new String();
			System.out.println("Command : " + command);
	
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			os.writeBytes("<html>\n" +
			"<head><title></title></head>\n" +
			"<body>\n");
			if((s = stdInput.readLine()) != null)
			{
				os.writeBytes("Here is the output from " + command +"<br><br>\n");
				do
				{
				os.writeBytes(s + "<br>\n");
				}while ((s = stdInput.readLine()) != null) ;
			}
			else
			{
				os.writeBytes("There was no output from the command " + command + "<br><br>\n");
			}
			// read any errors from the attempted command
			if((s = stdError.readLine()) != null)
			{
				os.writeBytes("The command reported errors:<br><br>\n");
				do
				{
					os.writeBytes(s + "<br>\n");
				}while ((s = stdError.readLine()) != null) ;
			}
			//stop the program from running
			p.destroy();
			os.writeBytes("</body></html>\n");
			}
			catch (IOException e) {
				os.writeBytes(e + "<br>\n");
			}
		}
		else 
		{
			os.writeBytes(entityBody);
		}
      
		// Close streams and socket.
		os.close();
		br.close();
		socket.close();     
	}

	private static void sendBytes(FileInputStream fis, OutputStream os)throws Exception
	{
		// Construct a 1K buffer to hold bytes on their way to the socket.
		byte[] buffer = new byte[1024];
		int bytes = 0;
      
		// Copy requested file into the socket's output stream.
		while((bytes = fis.read(buffer)) != -1 ) 
		{
			os.write(buffer, 0, bytes);
		}
	}
   
	private static String contentType(String fileName)
	{
		fileName = fileName.toLowerCase();
		if(fileName.endsWith(".htm") || fileName.endsWith(".html")|| fileName.endsWith(".txt")) 
		{
			return "text/html";
		}
		if(fileName.endsWith(".jpg") || fileName.endsWith(".jpe")) 
		{
			return "image/jpeg";
		}
		if(fileName.endsWith(".gif")) 
		{
			return "image/gif";
		}
	return "application/octet-stream";
	}

	public String listIndex(File file)
	{
		String index = "";
		String dir = file.getPath().replace('\\', '/');
		index += "<html><head><title>" +
		"Directory listing of " + dir +
		"</title></head>\n\n<body>\n" ;
		index += "<h2>Directory listing of <i>" + 
		dir + "</i></h2><br>\n" ;
		File list[] = file.listFiles();
		index += "<ul>\n";
		for(int i = 0; i < list.length; i++)
		{	
			boolean isDir = list[i].isDirectory();
			String name = list[i].getName();
			index += "<li>";
			if(isDir)
			 index += "<b>";
		            /*
		         	index += "<a href = \"http://" + 
		               socket.getLocalAddress().getHostName() +
		               ":" + socket.getLocalPort() + "/" + dir + "/" + name + 
		               "\">" + name + "</a>";
		         	*/

	
			index += "<a href = \"" + dir + "/" + name + "\">" + name + "</a>";
			if(isDir)
			index += "</b>";
			index += "</li>\n" ;
		}
		index += "</ul>\n";
		index += "</body></html>\n";
	return index;
	}
}
	
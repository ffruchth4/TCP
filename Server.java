package OperatingSystem;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;

public class Server {
	public static void main (String[] args) throws Exception
	{
				
	try	(
			//create a server socket and client socket
			ServerSocket server= new ServerSocket(6005);
			Socket client = server.accept();
	
			//create a bufferedreader to read from the client
			BufferedReader buffer= new BufferedReader(new InputStreamReader(client.getInputStream()));
	
			//send data to client with printwriter
			PrintWriter print= new PrintWriter(client.getOutputStream(), true);
	
			
		)
		{	
			System.out.println("Server connected.");
			
			//string sent to client
			String packets= "These programs connect the client and server socket using java to pass packages from one to another.";
					
			ArrayList<String> arrayList= new ArrayList<String>(20);
			//break the string into substrings of a length of 5 and put the in arrayList
			for(int w=0,x=0, y=5; x<100; w++,x+=5, y+=5)
			{
				String z= packets.substring(x,y);				
				arrayList.add(w, z);			
			}
			
			Random rand= new Random();
			
			//add the indexes to the arrayList
			for(int g=0; g <arrayList.size(); g++)
			{
				if(g<10)
				{
					arrayList.set(g,("0" + g + arrayList.get(g)));
				}
				else
				{
					arrayList.set(g,(g + arrayList.get(g)));
				}
			}
			
			//copy the arrayList to another arrayList so that can shuffle the arrayList
			ArrayList <String>copyWithIndexes= new ArrayList<String>(20);			
			for(int d=0; d<arrayList.size(); d++)
			{
				copyWithIndexes.add( arrayList.get(d));
			}
			Collections.shuffle(arrayList); //shuffle arrayList	
			
			//send the packages 80% of the time
			for(int f = 0; f < arrayList.size(); f++)
			{
				
				if(rand.nextInt(100)>=20)
				{
					print.println(arrayList.get(f));
				}
			}
			System.out.println("Packages sent.");
			print.println("done.");
			
			
			String clientRequest = buffer.readLine();
			while(!clientRequest.equals("done."))
			{
				//80% of the time it will send back the missing package
				if(rand.nextInt(100)>=20)
					{
						int holder= Integer.parseInt(clientRequest);
						print.println(copyWithIndexes.get(holder));
						
					}
				else 
				{
					print.println("again");
				}
				
				
				clientRequest = buffer.readLine();
				
				
			}
			System.out.println("All dropped packages were sent.");
			print.println("finished"); 
				
				
		}
	
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
	
	}
}

package OperatingSystem;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class client {
	public static void main (String [] args) throws Exception{
		
		
		//using a try block because we want everything to close
		//after were done using it
		try (
		//specify the ip address and the port number
		Socket client= new Socket("127.0.0.1", 6005);
		
		//create object to send data to server
		PrintWriter writeToServer= new PrintWriter(client.getOutputStream(), true);
		
		//create object to read from server
		BufferedReader readFromServer= new BufferedReader(new InputStreamReader(client.getInputStream()));
		)
		{
		System.out.println("Client connected.");
		
		//create an arraylist to store the packages received
		ArrayList<String> packages = new ArrayList<String>(20);

		//create an arraylist to check which packages are missing
		ArrayList<String> check = new ArrayList<String>(20);
		
		//read what the server sent using a bufferedReader
		String replyFromServer= readFromServer.readLine();
		
		/*stay in this loop until the server
		* sends a message that its done sending all
		 the packages for the first time*/
		while(!replyFromServer.equals("done."))
		{			
			//read from server
			System.out.println("Received package: "+ "\"" +replyFromServer + "\"");
			//add this package to the packages arraylist
			packages.add(replyFromServer);
			
			replyFromServer=readFromServer.readLine();
		}//end of while

		//initialize every element in arraylist to null
		for(int i = 0; i < 20; i++) {
			check.add(i, null);
		}
		
		//go through each element of packages arraylist		
		for(int i = 0; i < packages.size(); i++) {
			/*take out the first two characters of the package
			* and use that to determine the index that the package
			* belongs in to see which ones are missing
			*/
			int b = Integer.parseInt(packages.get(i).substring(0,2));
			
			/*take the rest of the substring that does not include
			* the numbers and place it in the check arraylist
			*/
			check.set(b,packages.get(i).substring(2,7));
			
		}
		System.out.println();
		/*stay in this loop until the server
		sends a message that it is finished
		sending all the dropped packages*/
		while(!readFromServer.equals("finished"))
		{
			boolean done = true;
			//loop through the check arraylist
			for(int i = 0; i < check.size(); i++) {
				
				/*if there are elements in the check arraylist
				* that are still null, meaning that the package must
				* have been dropped along the way... */
				if(check.get(i)==(null))
				{
					//display which package is missing
					System.out.println("Missing package: "+ i);
					//tell the server which one is missing
					writeToServer.println(i);
					
					//read the response of the server
					String reply = readFromServer.readLine();
					done = false;

					//if the server sends a message that says "again"
					if(reply.equals("again")) {
						//display that the missing package was blocked again
						System.out.println("Missing package blocked.");
						//start again
						break;
					}
					//once it is received, place the main part of the 
					//package into the index
					//that is specified by the first 2 characters
					check.set(Integer.parseInt(reply.substring(0,2)),reply.substring(2,7));

					//display that the package has been received
					System.out.println("Received missing package #" + i);
				}
			}
			//if the server says that it is done
			if(done) {
				System.out.println("We recieved all the packages.");
				//send the server a message that it received everything
				writeToServer.println("done.");	
				break;
			}
		
		
			
		}
		//display all the packages
		System.out.println();
		System.out.println("Message from server: ");
		for(int i =0;i < check.size(); i++) {
			System.out.print(check.get(i));
			}
		
		System.out.println();
		}//end of try
		
		//if the specified host is unknown
		catch(UnknownHostException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		//if there is an io exception
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}}
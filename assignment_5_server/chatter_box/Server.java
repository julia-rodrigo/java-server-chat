package chatter_box;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// run the server on eclipse

// open command prompts

// find the file on your directory
// C:\Users\NOOT NOOT\eclipse-workspace\ParallelDistributionAssignment\src\chatter_box

// run this
// java Client.java

// enter user name and look at server on eclipse
// a message appears

// do the same on the other command prompt

// now, a new message saying the name of the new person appears
// on the other opened client command prompts

// you can make multiple clients and all will appear on the chat
// theres a screenshot ('chatter_box.png') given to demonstrate this

public class Server {

    private ServerSocket serverSocket;

    public static int port = 1000;

    public Server (ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

    }

    public void server_begin (){
        try{
            while (!serverSocket.isClosed()) {
            	
            	// client connect on this server
                Socket socket = serverSocket.accept();
                System.out.println("a new client entered the chat room");

                // the client changed names are recorded here on when a new server connects
                ClientFile clientFile = new ClientFile (socket);

                Thread thread = new Thread (clientFile);
                thread.start();
            }
        } catch (IOException e){

        }
    }

    public void close_socket(){
        
        try {
            if (serverSocket != null) {
                serverSocket.close();
            } 
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main (String [] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket (port);
        Server server = new Server (serverSocket);
        server.server_begin();
    }
    
}

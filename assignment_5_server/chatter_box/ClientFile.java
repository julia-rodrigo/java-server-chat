package chatter_box;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientFile implements Runnable {

    // send messages to all clients
    public static ArrayList <ClientFile> clientFiles = new ArrayList<>();

    // connection for client and server
    private Socket socket;

    // this will be used to read messages from client
    private BufferedReader bufferedReader;

    // send messages from client
    private BufferedWriter bufferedWriter;

    private String username;

    public ClientFile(Socket socket) {
        try{
            this.socket = socket;
            
            // send
            this.bufferedWriter = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
            
            // read
            this.bufferedReader = new BufferedReader (new InputStreamReader (socket.getInputStream()));
            
            // a text after the enter key
            this.username = bufferedReader.readLine(); 
            
            // bunch of messages
            clientFiles.add(this);

            show_message(username + ": has entered the chat.");

        } catch (IOException e) {
        	close_server(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run (){
        String message_client;

        while (socket.isConnected()){
            try {
            	message_client = bufferedReader.readLine();
                show_message(message_client);
            } catch (IOException e) {
                close_server (socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void show_message(String message) {
        for (ClientFile clientFile : clientFiles) {
            try {
            	// everyones shows but the name of the sender
                if (!clientFile.username.equals(username)){
                    clientFile.bufferedWriter.write(message);
                    clientFile.bufferedWriter.newLine();
                    clientFile.bufferedWriter.flush();
                }
            } catch (IOException e) {
            	close_server(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientFile (){
        clientFiles.remove (this);
        show_message(username + ": has left the chat.");
    }

    public void close_server (Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientFile();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();

            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            } 

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();;
        }
    }
}
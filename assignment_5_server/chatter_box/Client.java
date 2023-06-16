package chatter_box;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public static int port = 1000;

    public Client(Socket socket, String username) {
        try{
            this.socket = socket ;
            this.bufferedWriter = new BufferedWriter (new OutputStreamWriter((socket.getOutputStream())));
            this.bufferedReader = new BufferedReader (new InputStreamReader((socket.getInputStream())));
            this.username = username;
        } catch (IOException e) {
            close_server (socket, bufferedReader, bufferedWriter);

        }
    }

    public void message_sent() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner hello = new Scanner (System.in);
            while (socket.isConnected()){
                String send = hello.nextLine();
                bufferedWriter.write(username + ": " + send);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            close_server (socket, bufferedReader, bufferedWriter);
        }
    }

    public void listen_for_message() {
        new Thread (new Runnable () {
            @Override
            public void run (){
                String grp_message;

                while (socket.isConnected()){
                    try { 
                        grp_message = bufferedReader.readLine();
                        System.out.println(grp_message);
                    } catch (IOException e){
                        close_server (socket, bufferedReader, bufferedWriter);
                    }

                } 
            }
        }).start();
    }
    
    public void close_server (Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
    
    public static void main (String[] args) throws IOException {
        Scanner hello = new Scanner (System.in);

        System.out.println("Enter username: ");
        String username = hello.nextLine();

        Socket socket = new Socket ("localhost", port);
        Client client = new Client(socket, username);
        client.listen_for_message();
        client.message_sent();
    }

    
}

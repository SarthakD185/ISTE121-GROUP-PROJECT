import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

import java.net.*;
import java.io.*;

import java.util.*;

public class UnoServer extends Application implements EventHandler<ActionEvent>{

   private Stage stage;
   private Scene scene;
   private VBox root;
   
   public TextArea taLog = new TextArea();
   private Button btnStart = new Button("Start");
   
   public ServerSocket ss = null;
   private ServerThread serverThread = null;
   public static final int SERVER_PORT = 54321;
   
   Socket socket;
   DataInputStream dis = null;
   DataOutputStream dos = null;
   
   //card variables
   String color;
   int number;
   
   //full deck array list
   ArrayList<Card> fullDeck = new ArrayList<Card>();
   
   public static void main(String[] args) {
      launch(args);
   }
   
   public void start(Stage _stage) {
      // Window setup
      stage = _stage;
      stage.setTitle("UNO Server");
      stage.setOnCloseRequest(
         new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evt) { System.exit(0); }
         });
      stage.setResizable(false);
      root = new VBox(8);
      
      FlowPane fpTop = new FlowPane(8,8);
      fpTop.setAlignment(Pos.CENTER);
      fpTop.getChildren().addAll(btnStart);
      root.getChildren().add(fpTop);
      
      FlowPane fpBot = new FlowPane(8,8);
      fpBot.setAlignment(Pos.CENTER);
      taLog.setPrefRowCount(10);
      taLog.setPrefColumnCount(35);
      fpBot.getChildren().addAll(taLog);
      root.getChildren().add(fpBot);
      
      btnStart.setOnAction(this);
      
      // Show window
      scene = new Scene(root, 500, 200);
      stage.setScene(scene);
      stage.show();
      
   }
    
   public void handle(ActionEvent ae) {
      String label = ((Button)ae.getSource()).getText();
      switch(label) {
         case "Start":
            
            serverThread = new ServerThread();
            serverThread.start();
            btnStart.setText("Stop");
            createDeck();
            break;
         case "Stop":
            disconnect();
            btnStart.setText("Start");
            break;
                     
      }
   }
    
    
   class ServerThread extends Thread{
      private ServerSocket sSocket = null;
      public void run() {
      
         try
         {
            ss = new ServerSocket(12345);
         
            while(true)
            {
               Socket s = ss.accept();
               System.out.println("Request received from " + s.getInetAddress().getHostName());
               new ProcessThread(s).start();
            
            }
         }
         catch(Exception ex)
         {
            ex.printStackTrace();
         }
      
      }
   }
   public void disconnect(){
      try{
         ss.close();
      }catch(Exception e){
         e.printStackTrace();
      }
   }
   
   class ProcessThread extends Thread {
   
      
      
      public ProcessThread(Socket s)
      {
         socket = s;
      }
      
      public void run(){
      
         try{
            String ip = socket.getInetAddress().getHostName();
            
            taLog.appendText("Accepting connection from " + ip + "\n");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
         
            while(true){
               String data = br.readLine();
               System.out.println("Client Sent Data: " + data);
               
               if(data.equals("quit")){
                  pw.println("Server Response: Disconnect request received.");
                  taLog.appendText(data + " \n");
                  pw.flush(); // you have to flush the data.
                  break;
               }else{
                  taLog.appendText(data + " \n");
                  pw.println(data); //This is the data in the file.
                  pw.flush(); // you have to flush the data.
               }
            }
            socket.close();
         }catch(Exception e){
            e.printStackTrace();
         }   
           
            
            
      
      }
      
      
   } 
   
   public void createDeck(){
      //create the zero cards
      Card c1 = new Card("red", 0);
      Card c2 = new Card("yellow", 0);
      Card c3 = new Card("blue", 0);
      Card c4 = new Card("green", 0);
            
      //add the zero cards to the full deck
      fullDeck.add(c1);
      fullDeck.add(c2);
      fullDeck.add(c3);
      fullDeck.add(c4);
            
      //colors
      for (int x = 0; x < 4; x++){
         //numbers
         for (int y = 1; y < 10; y++){
            //2 of each number
            for(int z = 0; z < 2; z++){
            
            if(x == 0){
               color = "red";
            }
            else if(x == 1){
               color = "yellow";
            }
            else if(x == 2){
               color = "blue";
            }
            else if(x == 3){
               color = "green";
            }
            
            number = y;
            //create a Card
            Card c = new Card(color, number);
            
            //add the card to the full deck
            fullDeck.add(c);
            }
         }
      }
      
      System.out.println("Array Size: " + fullDeck.size());
      for(int x2 = 0; x2 < fullDeck.size(); x2++){
         System.out.println(fullDeck.get(x2).toString());
      }
   }
 
 
}

import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.Text;

import java.net.*;
import java.io.*;

import java.util.*;

public class UnoServer extends Application implements EventHandler<ActionEvent>{

   private Stage stage;
   private Scene scene;
   private VBox root;
   
   private Button btnStart = new Button("Start");
   private Label lblShowCard = new Label("");
   private Label lblPileCard = new Label("");
   
   public ServerSocket ss = null;
   
   public static final int SERVER_PORT = 54321;
   
   Socket socket;
   Socket socket2 = null;
   
   private Random randomGenerator;
   
   String color;
   int number;
   Card c = null;
   
   //INPUTS AND OUTPUTS STREAMS 
   Vector<ObjectOutputStream> clients = new Vector<ObjectOutputStream>();
   
   OutputStream osServer = null;
   ObjectOutputStream objOsServer = null;
   InputStream isServer = null;
   ObjectInputStream objIsServer = null;
   
   //full deck array list
   ArrayList<Card> fullDeck = new ArrayList<Card>();
   
   //placement pile
   ArrayList<Card> placementPile = new ArrayList<Card>();
   
   
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
      
      fpBot.getChildren().addAll(lblPileCard);
      root.getChildren().add(fpBot);
      
      btnStart.setOnAction(this);
      
      // Show window
      scene = new Scene(root, 500, 200);
      stage.setScene(scene);
      
      //Setup server
      createDeck();
      randomize();
      System.out.println("size: " + fullDeck.size());
      
      stage.show();
      
   }
    
   public void handle(ActionEvent ae) {
      String label = ((Button)ae.getSource()).getText();
      switch(label) {
         case "Start":
            Thread t1 = 
            new Thread() {
               public void run() {
                  doServerStuff();
               }
            };
            t1.start();
            btnStart.setText("End");
            break;
         case "End":
            disconnect();
            btnStart.setText("Start");
            break;        
      }
   }
   
   private void doServerStuff() {
      try {
         System.out.println("Starting server");
         ServerSocket sSocket = new ServerSocket(12345);
         while(true) {
            Socket socket1 = sSocket.accept();
            Thread t2 = new ClientThread(socket1);
            System.out.println("client connected");
            t2.start();
         }
      }
      catch(Exception e) {
      }
   } 
    
   class ClientThread extends Thread{
      private ServerSocket sSocket = null;
      Socket socket2 = null;
      Scanner scn = null;
      ObjectInputStream ooi = null;
      ObjectOutputStream oos = null;
      
      /** constructor */
      public ClientThread(Socket _cSocket) {
         socket2 = _cSocket;
      }
      
      public void run() {
      try {
            ooi = new ObjectInputStream(socket2.getInputStream());
            oos = new ObjectOutputStream(socket2.getOutputStream());
            clients.add(oos);
            
            while(true)
            {
               String command = ooi.readUTF();
               System.out.println("command:" + command);

               switch(command) {
                  case "CONNECT":
                     ArrayList<Card> hand = pullCards();
                     oos.writeObject(hand);
                     oos.flush();
                     oos.reset();
                     
                     placementPile();
                     break;
                     
                  case "DRAW":
                     //Implemented
                     
                     ArrayList<Card> sCard = sendCard();
                     oos.writeObject(sCard);
                     oos.flush();
                     oos.reset();
                     
                     System.out.println(sCard.size());
                     
                     
                     // String data = ooi.readUTF();
//                      broadcastMessage("INFO","From server " + data);
                           
                     break;
                  case "PLACE":
                  
                  // We think that its not connecting to the socket or not receiving from the client.
                     sSocket = new ServerSocket(54123);
                     socket2 = sSocket.accept();
                     oos = new ObjectOutputStream(socket.getOutputStream());
                     ooi = new ObjectInputStream(socket.getInputStream());
                     Card c = (Card)ooi.readObject();;
                     //Game logic goes here...
                     /*
                        
                        Note: WILD matches any color and WILD+4 invokes drawCard() x 4 and +2 invokes drawCard() x 2
                     
                     
                        if card c.getcolor() == placementpile.get(placementpile.size().getColor())
                        if card c.getNumber() == placementpile.get(placementpile.size().getnumber())
                        
                        then 
                        
                        add card c to placement pile and remove it from player hand
                        
                        if not then
                        
                        pop up alter saying illegal move 
                        
                   
                        
                     */
                     placementPile.add(c);
                     System.out.println(placementPile.size());
                     
                     break;
                  default:
                     System.out.println("Invalid command");
               
               }
            }
            // pw.close();
            // scn.close();
            //socket2.close();
         }
         catch(Exception e) {
            e.printStackTrace();
         }
      
      }
   }
   
   private void broadcastMessage(String command, Object data) {
      System.out.println("sending broadcast message : " + clients.size());  
      for(ObjectOutputStream clientOutStream : clients) {
         System.out.println("sending broadcast message ==");
         try {
            clientOutStream.writeUTF(command);
            clientOutStream.flush();
            System.out.println("Sending data " + data);
            clientOutStream.writeObject(data);
            clientOutStream.reset();
            clientOutStream.flush();
         } catch(Exception ex)
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
   
   public void createDeck(){
      //create the zero cards
      Card c1 = new Card("RED", 0);
      Card c2 = new Card("YELLOW", 0);
      Card c3 = new Card("BLUE", 0);
      Card c4 = new Card("GREEN", 0);
      
      //create the WILD cards
      Card c5 = new Card("WILD", 0);
      Card c6 = new Card("WILD", 0);
      Card c7 = new Card("WILD", 0);
      Card c8 = new Card("WILD", 0);
      
      //create the WILD +4 cards
      Card c9 = new Card("WILD+4", 0);
      Card c10 = new Card("WILD+4", 0);
      Card c11 = new Card("WILD+4", 0);
      Card c12 = new Card("WILD+4", 0);
            
      //add the zero cards to the full deck
      fullDeck.add(c1);
      fullDeck.add(c2);
      fullDeck.add(c3);
      fullDeck.add(c4);
      fullDeck.add(c5);
      fullDeck.add(c6);
      fullDeck.add(c7);
      fullDeck.add(c8);
      fullDeck.add(c9);
      fullDeck.add(c10);
      fullDeck.add(c11);
      fullDeck.add(c12);
            
      //colors
      for (int x = 0; x < 4; x++){
         //numbers
         // regular numbers are 0 to 9
         // 10 is +2
         for (int y = 1; y < 11; y++){
            //2 of each number
            for(int z = 0; z < 2; z++){
            
               if(x == 0){
                  color = "RED";
               }
               else if(x == 1){
                  color = "YELLOW";
               }
               else if(x == 2){
                  color = "BLUE";
               }
               else if(x == 3){
                  color = "GREEN";
               }
               
               number = y;
            //create a Card
               c = new Card(color, number);
            
            //add the card to the full deck
               fullDeck.add(c);
              
            }
         }
      }
     
   }
   
   //takes the full deck array list and randomizes the order of it
   public void randomize(){
      Collections.shuffle(fullDeck);
      // System.out.println("Array Size: " + fullDeck.size());
      for(int x2 = 0; x2 < fullDeck.size(); x2++){
         System.out.println(fullDeck.get(x2).toString());
      }
      
   }
   
   
   public void placementPile(){
   
      placementPile.add(fullDeck.get(0));
      System.out.println(placementPile.get(0));
      fullDeck.remove(0);
      System.out.println(fullDeck.get(0));
      
      stylizeCard();
      
   }
   
   
   //TAKES 7 CARDS AND SENDS THEM TO THE CLIENT.
   public ArrayList<Card> pullCards(){
      ArrayList<Card> hand = new ArrayList<Card>();
      for(int i = 0; i <= 6; i++){
         hand.add(fullDeck.get(0));
         fullDeck.remove(0);
      }
      
      return hand;
   }
   
   public ArrayList<Card> sendCard(){
      ArrayList<Card> sCard = new ArrayList<Card>();
         sCard.add(fullDeck.get(0));
         fullDeck.remove(0);
         
         
      return sCard;
   }
   
    
    public void stylizeCard(){
    
      lblPileCard.setText("" + fullDeck.get(0).getNumber());
      if(fullDeck.get(0).getColor() == "RED"){
         lblPileCard.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
         lblPileCard.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
      }else if(fullDeck.get(0).getColor() == "GREEN"){
         lblPileCard.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
         lblPileCard.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
      }else if(fullDeck.get(0).getColor() == "BLUE"){
         lblPileCard.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
         lblPileCard.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
      }else if(fullDeck.get(0).getColor() == "YELLOW"){
         lblPileCard.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
         lblPileCard.setStyle("-fx-font: 24 arial; -fx-text-fill: black;");
      }else if(fullDeck.get(0).getColor() == "WILD" || fullDeck.get(0).getColor() == "WILD+4"){
         lblPileCard.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
         lblPileCard.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
      }
      
      lblPileCard.setPrefHeight(100);
      lblPileCard.setPrefWidth(50);
      lblPileCard.setAlignment(Pos.CENTER);
    
    
    }
   


 
 
}
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


public class UnoClient extends Application implements EventHandler<ActionEvent>{

   //Window attributes
   private Stage stage;
   private Scene scene;
   private VBox root;
   
   private Label lblConnect = new Label("Enter IP address of server to connect: ");
   private TextArea taTemp = new TextArea();
   private Button btnConnect = new Button("Connect");
   private TextField tfServerIP = new TextField();
   
   private Button btnCreateHand = new Button("Create Hand");
   private Button btnDraw = new Button("Draw");
   private Button btnPlace = new Button("Place");
   private Label lblCard1 = new Label("");
      
   PrintWriter pwt = null;
   ObjectOutputStream odeck = null;
   OutputStream deckBack = null;
   ObjectOutputStream oSendDeckBack = null;
   
   
   //ArrayList<Card> handReceived = new ArrayList<Card>(); 
   ArrayList<Card> deckReceived = new ArrayList<Card>();
   
   ArrayList<Card> PlayerHand = new ArrayList<Card>();
   
   public static final int SERVER_PORT = 12345;
   private Socket socket = null;
   
   public static void main(String[] args) {
      launch(args);
   }
   
   public void start(Stage _stage){
      stage = _stage;
      stage.setTitle("UNO Client");
      root = new VBox();
      
      FlowPane fpTop = new FlowPane(8,8);
      fpTop.setAlignment(Pos.CENTER);
      fpTop.getChildren().addAll(lblConnect, tfServerIP, btnConnect);
      root.getChildren().add(fpTop);
      
      FlowPane fpMid = new FlowPane(8,8);
      fpMid.setAlignment(Pos.CENTER);
      fpMid.getChildren().addAll(btnCreateHand, btnDraw, btnPlace);
      root.getChildren().add(fpMid);
      
      FlowPane fpBot = new FlowPane(8,8);
      fpBot.setAlignment(Pos.CENTER);
      fpBot.getChildren().addAll(lblCard1);
      root.getChildren().add(fpBot);
      
      //disabled buttons until after client is connected to server
      btnCreateHand.setDisable(true);
      //btnDraw.setDisable(true);
      btnPlace.setDisable(true);
      
      
      btnConnect.setOnAction(this);
      btnCreateHand.setOnAction(this);
      btnDraw.setOnAction(this);
      
      
      
      scene = new Scene(root, 500, 250);
      stage.setScene(scene);
      stage.show();
      
   
      
   
   }
   
   public void handle(ActionEvent ae) {
      String label = ((Button)ae.getSource()).getText();
      switch(label) { //Switch case for buttons
         case "Connect":
            doStart();
            //receiveDeck();
            break;
         case "Disconnect":
            doStop();
            break;
         case "Draw":
            drawCard();
            showHand();
            break;
         case "Create Hand":

            break;

      }
   }
   
   public void doStart(){
   
      try{
         socket = new Socket(tfServerIP.getText(), SERVER_PORT);
         pwt = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
          
       
      }catch(Exception e){
         e.printStackTrace();
      }
      taTemp.appendText("Connected!\n");
       
      btnConnect.setText("Disconnect");
      
      
   }
   
   public void doStop(){
      try {
         socket.close();
      }
      catch(IOException ioe) {
         taTemp.appendText("IO Exception: " + ioe + "\n");
         //Alerts 
         Alert a = new Alert(AlertType.NONE);
         a.setAlertType(AlertType.ERROR);
         a.setContentText("Unexpected Error Occurred.");
         a.show();
         return;
      }
      taTemp.appendText("Disconnected!\n");
      btnConnect.setText("Connect");
   }
   
   public void createHand(){
   
      drawCard();
      drawCard();
      drawCard();
      drawCard();
      drawCard();
      drawCard();
      drawCard();
      
   
   }
   
   public void drawCard(){
      System.out.println("Starting DRAW");
   
      receiveDeck();
      System.out.println("Received Deck");
      System.out.println(deckReceived.get(0));
      System.out.println("Player 1 hand: " + PlayerHand.size());
      PlayerHand.add(deckReceived.get(0));
      
   
      System.out.println("Removing card from deck");
      deckReceived.remove(0);
      System.out.println("Player 1 hand: " + PlayerHand.size());
      System.out.println("DECK SIZE: " + deckReceived.size());
      
      
   
      //sendDeckBack();
   }
   
   
   
   
   
   public void receiveDeck(){
      try{
         OutputStream outputStream = socket.getOutputStream();
         odeck = new ObjectOutputStream(outputStream);
      
         InputStream inputStream = socket.getInputStream();
         ObjectInputStream inDeck = new ObjectInputStream(inputStream);
      
         deckReceived = (ArrayList<Card>)inDeck.readObject();
      
                //flush to print data
         odeck.flush();
         for (int i = 0; i < deckReceived.size(); i++){
            System.out.println(deckReceived.get(i).toString() +  " , ");
         }
         System.out.println(deckReceived.size());
         System.out.println("-------Received Deck-------");
      }catch(Exception e){
         e.printStackTrace();
      }
   
   
   }
    
   public void sendDeckBack(){
      try{
      
          // get the output stream from the socket.
         deckBack = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
         oSendDeckBack = new ObjectOutputStream(deckBack);
      
      
      
         System.out.println("Sending messages to the ServerSocket");
         oSendDeckBack.writeObject(deckReceived);
         oSendDeckBack.flush();
      
         System.out.println("Closing socket and terminating program.");
      
      
      }catch(Exception e){
         e.printStackTrace();
      }
   }
   
   public void showHand(){
   
   lblCard1.setText("" + PlayerHand.get(0).getNumber());
   
   if(PlayerHand.get(0).getColor() == "RED"){
         lblCard1.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
         lblCard1.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
      }else if(PlayerHand.get(0).getColor() == "GREEN"){
         lblCard1.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
         lblCard1.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
      }else if(PlayerHand.get(0).getColor() == "BLUE"){
         lblCard1.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
         lblCard1.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
      }else if(PlayerHand.get(0).getColor() == "YELLOW"){
         lblCard1.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
         lblCard1.setStyle("-fx-font: 24 arial; -fx-text-fill: black;");
      }
      
      lblCard1.setPrefHeight(100);
      lblCard1.setPrefWidth(50);
      lblCard1.setAlignment(Pos.CENTER);
   
   }
   
   // public void receiveHand(){
//       try{
//          OutputStream outputStream = socket.getOutputStream();
//                oout = new ObjectOutputStream(outputStream);
//                
//          InputStream inputStream = socket.getInputStream();
//                ObjectInputStream ooin = new ObjectInputStream(inputStream);
//                
//           handReceived = (ArrayList<Card>)ooin.readObject();
//                
//                //flush to print data
//                oout.flush();
//                
//           for (int i = 0; i < handReceived.size(); i++){
//                   System.out.println(handReceived.get(i).toString() +  " , ");
//                } 
//                
//                System.out.println("Received");
//       }catch(Exception e){
//          e.printStackTrace();
//       }
//    
//    }
   

}
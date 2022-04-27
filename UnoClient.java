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
   private Label lblCard2 = new Label("");
   private Label lblCard3 = new Label("");
   private Label lblCard4 = new Label("");
   private Label lblCard5 = new Label("");
   private Label lblCard6 = new Label("");
   private Label lblCard7 = new Label("");
   
   TextInputDialog dialogPlace = null;
   String dialogInput = null;
   
   int cardPos;
   
   
   
   //NEEDED FOR SERVER   
   PrintWriter pwt = null;
   
   //Input and output streams
   OutputStream osClient = null;
   ObjectOutputStream objOsClient = null;
   
   InputStream isClient = null;
   ObjectInputStream objIsClient = null;
   
   
   
   
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
      fpBot.getChildren().addAll(lblCard1, lblCard2, lblCard3, lblCard4, lblCard5, lblCard6, lblCard7);
      root.getChildren().add(fpBot);
      
     
      
      
      btnConnect.setOnAction(this);
      btnCreateHand.setOnAction(this);
      btnDraw.setOnAction(this);
      btnPlace.setOnAction(this);
      
      
      
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
            
            break;
         case "Create Hand":
            createHand();
            
            break;
         case "Place":
            String text = ((Button)ae.getSource()).getText();
            if(text.equals("Place")){
               dialogPlace = new TextInputDialog();
               
               dialogPlace.setTitle("Place Card");
               dialogPlace.setHeaderText("Choose a card to place (0 is left most card and 6 is right most card)");
               dialogPlace.setContentText("Enter the number:");
               dialogPlace.showAndWait();
               
               
            }
            dialogInput = dialogPlace.getEditor().getText();
            System.out.println(dialogInput);
            
            
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
   
      // drawCard();
//       drawCard();
//       drawCard();
//       drawCard();
//       drawCard();
//       drawCard();
//       drawCard();
      
      receiveDeck();

      PlayerHand.add(deckReceived.get(0));
      PlayerHand.add(deckReceived.get(1));
      PlayerHand.add(deckReceived.get(2));
      PlayerHand.add(deckReceived.get(3));
      PlayerHand.add(deckReceived.get(4));
      PlayerHand.add(deckReceived.get(5));
      PlayerHand.add(deckReceived.get(6));
      
      
      deckReceived.remove(0);
      deckReceived.remove(1);
      deckReceived.remove(2);
      deckReceived.remove(3);
      deckReceived.remove(4);
      deckReceived.remove(5);
      deckReceived.remove(6);
      
      showHand();
      
      System.out.println("Player 1 hand: " + PlayerHand.size());
      
      for (int i = 0; i < PlayerHand.size(); i++){
            System.out.println(PlayerHand.get(i).toString() +  " , ");
         }
      
      
      System.out.println("DECK SIZE: " + deckReceived.size());
      
   
   }
   
   public void drawCard(){
      System.out.println("Starting DRAW");
   
      receiveDeck();
      
      
      if(PlayerHand.size() == 6){
         btnDraw.setDisable(true);
      }
      
      PlayerHand.add(deckReceived.get(0));
      deckReceived.get(0);
      
      
      System.out.println("Received Deck");
      
      System.out.println("Player 1 hand: " + PlayerHand.size());
      
      
      
      
      
      
   
      System.out.println("Removing card from deck");
      deckReceived.remove(0);
      
      System.out.println("Player 1 hand: " + PlayerHand.size());
      System.out.println("DECK SIZE: " + deckReceived.size());
      
      
   
      //sendDeckBack();
   }
   
   
   
   
   
   public void receiveDeck(){
      try{
      
         osClient = socket.getOutputStream();
         objOsClient = new ObjectOutputStream(osClient);
         
         isClient = socket.getInputStream();
         objIsClient = new ObjectInputStream(isClient);
         
         deckReceived = (ArrayList<Card>)objIsClient.readObject();
         
         objOsClient.flush();    
         
         for (int i = 0; i < deckReceived.size(); i++){
            System.out.println(deckReceived.get(i).toString() +  " , ");
         }
         System.out.println(deckReceived.size());
         System.out.println("-------Received Deck-------");
         
         socket.close();
      }catch(Exception e){
         e.printStackTrace();
      }
   
   
   }
   
    
    public void sendDeckBack(){
       try{
         
         objOsClient.writeObject(deckReceived);
         objOsClient.flush();
         
       
         System.out.println("Closing socket and terminating program.");
         
       
       }catch(Exception e){
          e.printStackTrace();
       }
    }
   
   public void showHand(){
  
      checkColor();
        
      
      }
      
      
      
      public void checkColor(){
      
         lblCard1.setText("" + PlayerHand.get(0).getNumber());
         lblCard2.setText("" + PlayerHand.get(1).getNumber());
         lblCard3.setText("" + PlayerHand.get(2).getNumber());
         lblCard4.setText("" + PlayerHand.get(3).getNumber());
         lblCard5.setText("" + PlayerHand.get(4).getNumber());
         lblCard6.setText("" + PlayerHand.get(5).getNumber());
         lblCard7.setText("" + PlayerHand.get(6).getNumber());
         
      
         if(PlayerHand.get(0).getColor().equals("RED")){
            lblCard1.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            lblCard1.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(0).getColor().equals("GREEN")){
            lblCard1.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            lblCard1.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(0).getColor().equals("BLUE")){
            lblCard1.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
            lblCard1.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(0).getColor().equals("YELLOW")){
            lblCard1.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
            lblCard1.setStyle("-fx-font: 24 arial; -fx-text-fill: black;");
         }else if((PlayerHand.get(0).getColor().equals("WILD"))){
            lblCard1.setText("WILD");
            lblCard1.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard1.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }else if((PlayerHand.get(0).getColor().equals("WILD+4"))){
            lblCard1.setText("WILD+4");
            lblCard1.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard1.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }
         
         if(PlayerHand.get(1).getColor().equals("RED")){
            lblCard2.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            lblCard2.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(1).getColor().equals("GREEN")){
            lblCard2.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            lblCard2.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(1).getColor().equals("BLUE")){
            lblCard2.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
            lblCard2.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(1).getColor().equals("YELLOW")){
            lblCard2.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
            lblCard2.setStyle("-fx-font: 24 arial; -fx-text-fill: black;");
         }else if((PlayerHand.get(1).getColor().equals("WILD"))){
            lblCard2.setText("WILD");
            lblCard2.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard2.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }else if((PlayerHand.get(1).getColor().equals("WILD+4"))){
            lblCard2.setText("WILD+4");
            lblCard2.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard2.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }
         
         
         if(PlayerHand.get(2).getColor().equals("RED")){
            lblCard3.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            lblCard3.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(2).getColor().equals("GREEN")){
            lblCard3.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            lblCard3.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(2).getColor().equals("BLUE")){
            lblCard3.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
            lblCard3.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(2).getColor().equals("YELLOW")){
            lblCard3.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
            lblCard3.setStyle("-fx-font: 24 arial; -fx-text-fill: black;");
         }else if((PlayerHand.get(2).getColor().equals("WILD"))){
            lblCard3.setText("WILD");
            lblCard3.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard3.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }else if((PlayerHand.get(2).getColor().equals("WILD+4"))){
            lblCard3.setText("WILD+4");
            lblCard3.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard3.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }


         if(PlayerHand.get(3).getColor().equals("RED")){
            lblCard4.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            lblCard4.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(3).getColor().equals("GREEN")){
            lblCard4.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            lblCard4.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(3).getColor().equals("BLUE")){
            lblCard4.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
            lblCard4.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(3).getColor().equals("YELLOW")){
            lblCard4.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
            lblCard4.setStyle("-fx-font: 24 arial; -fx-text-fill: black;");
         }else if((PlayerHand.get(3).getColor().equals("WILD"))){
            lblCard4.setText("WILD");
            lblCard4.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard4.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }else if((PlayerHand.get(3).getColor().equals("WILD+4"))){
            lblCard4.setText("WILD+4");
            lblCard4.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard4.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }


         if(PlayerHand.get(4).getColor().equals("RED")){
            lblCard5.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            lblCard5.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(4).getColor().equals("GREEN")){
            lblCard5.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            lblCard5.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(4).getColor().equals("BLUE")){
            lblCard5.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
            lblCard5.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(4).getColor().equals("YELLOW")){
            lblCard5.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
            lblCard5.setStyle("-fx-font: 24 arial; -fx-text-fill: black;");
         }else if((PlayerHand.get(4).getColor().equals("WILD"))){
            lblCard5.setText("WILD");
            lblCard5.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard5.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }else if((PlayerHand.get(4).getColor().equals("WILD+4"))){
            lblCard5.setText("WILD+4");
            lblCard5.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard5.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }


         if(PlayerHand.get(5).getColor().equals("RED")){
            lblCard6.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            lblCard6.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(5).getColor().equals("GREEN")){
            lblCard6.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            lblCard6.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(5).getColor().equals("BLUE")){
            lblCard6.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
            lblCard6.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(5).getColor().equals("YELLOW")){
            lblCard6.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
            lblCard6.setStyle("-fx-font: 24 arial; -fx-text-fill: black;");
         }else if((PlayerHand.get(5).getColor().equals("WILD"))){
            lblCard6.setText("WILD");
            lblCard6.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard6.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }else if((PlayerHand.get(5).getColor().equals("WILD+4"))){
            lblCard6.setText("WILD+4");
            lblCard6.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard6.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }


         if(PlayerHand.get(6).getColor().equals("RED")){
            lblCard7.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            lblCard7.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(6).getColor().equals("GREEN")){
            lblCard7.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            lblCard7.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(6).getColor().equals("BLUE")){
            lblCard7.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
            lblCard7.setStyle("-fx-font: 24 arial; -fx-text-fill: white;");
         }else if(PlayerHand.get(6).getColor().equals("YELLOW")){
            lblCard7.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
            lblCard7.setStyle("-fx-font: 24 arial; -fx-text-fill: black;");
         }else if((PlayerHand.get(6).getColor().equals("WILD"))){
            lblCard7.setText("WILD");
            lblCard7.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard7.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }else if((PlayerHand.get(6).getColor().equals("WILD+4"))){
            lblCard7.setText("WILD+4");
            lblCard7.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
            lblCard7.setStyle("-fx-font: 10 arial; -fx-text-fill: white;");
         }
         lblCard1.setPrefHeight(100);
         lblCard1.setPrefWidth(50);
         lblCard1.setAlignment(Pos.CENTER);
         
         lblCard2.setPrefHeight(100);
         lblCard2.setPrefWidth(50);
         lblCard2.setAlignment(Pos.CENTER);
         
         lblCard3.setPrefHeight(100);
         lblCard3.setPrefWidth(50);
         lblCard3.setAlignment(Pos.CENTER);
         
         lblCard4.setPrefHeight(100);
         lblCard4.setPrefWidth(50);
         lblCard4.setAlignment(Pos.CENTER);
         
         
         lblCard5.setPrefHeight(100);
         lblCard5.setPrefWidth(50);
         lblCard5.setAlignment(Pos.CENTER);
         
         
         lblCard6.setPrefHeight(100);
         lblCard6.setPrefWidth(50);
         lblCard6.setAlignment(Pos.CENTER);
         
         lblCard7.setPrefHeight(100);
         lblCard7.setPrefWidth(50);
         lblCard7.setAlignment(Pos.CENTER);
         
         
       


      
      }
   
   

}

   
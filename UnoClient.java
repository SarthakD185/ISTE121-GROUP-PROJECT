import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.collections.*;
import javafx.geometry.*;
import java.util.*;
import java.net.*;
import java.io.*;


public class UnoClient extends Application implements EventHandler<ActionEvent>{

   //Window attributes
   private Stage stage;
   private Scene scene;
   private VBox root;
   
   private Label lblName = new Label("UNO");
   private TextArea taTemp = new TextArea();
   private Button btnConnect = new Button("Connect");
   private TextField tfServerIP = new TextField();
   
   private Button btnCreateHand = new Button("Create Hand");
   private Button btnDraw = new Button("Draw");
   
   private MenuBar menuBar = new MenuBar();
   private Menu mnuPlayer = new Menu("Player");
   private MenuItem miAdd = new MenuItem("Add Player");
   
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
      
      mnuPlayer.getItems().addAll(miAdd);
      menuBar.getMenus().addAll(mnuPlayer);
      root = new VBox(menuBar);
      
      FlowPane fpTop = new FlowPane(8,8);
      fpTop.setAlignment(Pos.CENTER_LEFT);
      fpTop.getChildren().addAll(lblName, tfServerIP, btnConnect, btnCreateHand, btnDraw);
      root.getChildren().add(fpTop);
      
      FlowPane fpBot = new FlowPane(8,8);
      fpBot.setAlignment(Pos.CENTER);
      fpBot.getChildren().addAll(taTemp);
      root.getChildren().add(fpBot);
      
      miAdd.setDisable(true);
      
      
      btnConnect.setOnAction(this);
      btnDraw.setOnAction(this);
      btnCreateHand.setOnAction(this);
      

      
      
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
      
      sendDeckBack();
      
   
   
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
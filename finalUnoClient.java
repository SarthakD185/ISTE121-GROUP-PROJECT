import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import java.lang.Object.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javafx.application.Platform;

/**
 * GUIStarter - class to help with JavaFX classes
 * @author  D. Patric
 * @version 2205
 */

public class finalUnoClient extends Application implements EventHandler<ActionEvent> {
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
   private RadioButton rb1 = new RadioButton("Card 1");
   private RadioButton rb2 = new RadioButton("Card 2");
   private RadioButton rb3 = new RadioButton("Card 3");
   private RadioButton rb4 = new RadioButton("Card 4");
   private RadioButton rb5 = new RadioButton("Card 5");
   private RadioButton rb6 = new RadioButton("Card 6");
   private RadioButton rb7 = new RadioButton("Card 7");
   
   TextInputDialog dialogPlace = null;
   String dialogInput = null;   
   
   //NEEDED FOR SERVER   
   PrintWriter pwt = null;
   
   //Input and output streams
   private ObjectInputStream ooi = null;
   private ObjectOutputStream oos = null;
   
   OutputStream osClient = null;
   ObjectOutputStream objOsClient = null;
   
   
   InputStream isClient = null;
   ObjectInputStream objIsClient = null;
   
   
   
   
   //ArrayList<Card> handReceived = new ArrayList<Card>(); 
   ArrayList<Card> cardsReceived = new ArrayList<Card>();
   
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
            doConnect();
            //receiveDeck();
            break;
         case "Disconnect":
            doStop();
            break;
         case "Draw":
            //drawCard();
            
            break;
         case "Create Hand":
            //createHand();
            
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


//Sending   
   public void doConnect(){
   
      try{
         socket = new Socket(tfServerIP.getText(), 32146);
         oos = new ObjectOutputStream(socket.getOutputStream());
         ooi = new ObjectInputStream(socket.getInputStream()); 
          
         //Start the thread
         new ReceiveMsgThread().start();
         
         
         
      }catch(Exception e){
         System.out.println("Problem joining: "+ e.getMessage());
         
      }
           
   }
   
   public void doStop(){
      try {
         socket.close();
      }
      catch(IOException ioe) {
         taTemp.appendText("IO Exception: " + ioe + "\n");
         //Alerts 
         // Alert a = new Alert(AlertType.NONE);
//          a.setAlertType(AlertType.ERROR);
//          a.setContentText("Unexpected Error Occurred.");
//          a.show();
         return;
      }
      taTemp.appendText("Disconnected!\n");
      btnConnect.setText("Connect");
   }

   
   
      
   class ReceiveMsgThread extends Thread {
      public void run() {
         System.out.println("Client Thread Running");
         
         String message = "";
         try {
            //Loop to keep listening
            while(true) {
            
            }
          }catch(Exception e){
          
          }
                  
      }
   }  
}
   
    
   

   
   


   
   
   
      

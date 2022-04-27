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

public class finalUnoServer extends Application implements EventHandler<ActionEvent>{
   private Stage stage;
   private Scene scene;
   private VBox root;
   
   public TextArea taLog = new TextArea();
   private Button btnStart = new Button("Start");
   private Button btnShuffle = new Button("Shuffle");
   private Button btnSend = new Button("Send Hands");
   private Button btnCreateDeck = new Button("Create Deck");
   private Button btnReceiveNewDeck = new Button("Receive New Deck");
   private Label lblShowCard = new Label("");
   
   //public ServerSocket ss = null;
   //private ServerThread serverThread = null;
   public static final int SERVER_PORT = 32145;
   
   Socket socket;
   
   private Random randomGenerator;
   
   
   //card variables
   String color;
   int number;
   Card c = null;
   
   //INPUTS AND OUTPUTS STREAMS 
   
   Vector<ObjectOutputStream> clients = new Vector<ObjectOutputStream>();
   
   
   //full deck array list
   ArrayList<Card> fullDeck = new ArrayList<Card>();
   
   //new deck for full deck
   ArrayList<Card> Hand = new ArrayList<Card>();
   //placement pile
   ArrayList<Card> placementPile = new ArrayList<Card>();
   //Player hand
   ArrayList<Card> hand1 = new ArrayList<Card>();
   ArrayList<Card> hand2 = new ArrayList<Card>();
   
   ArrayList<Card> newDeck = new ArrayList<Card>();
   
   public static void main(String[] args) {
      launch(args);
   }
   
   /**
    * Launch, draw and set up GUI
    * Do server stuff
    */
    
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
      fpTop.getChildren().addAll(btnStart, btnShuffle, btnSend, btnCreateDeck, btnReceiveNewDeck);
      root.getChildren().add(fpTop);
      
      FlowPane fpBot = new FlowPane(8,8);
      fpBot.setAlignment(Pos.CENTER);
      taLog.setPrefRowCount(10);
      taLog.setPrefColumnCount(35);
      fpBot.getChildren().addAll(lblShowCard);
      root.getChildren().add(fpBot);
      
      btnStart.setOnAction(this);
      btnShuffle.setOnAction(this);
      btnSend.setOnAction(this);
      btnCreateDeck.setOnAction(this);
      btnReceiveNewDeck.setOnAction(this);
      
      
      
      
      // Show window
      scene = new Scene(root, 500, 200);
      stage.setScene(scene);
      
      stage.show();
      
   }
      

   public void handle(ActionEvent evt) {}
   
   
   private void doServerStuff() {
      try {
         ServerSocket sSocket = new ServerSocket(45549);
         while(true) {
            Socket socket1 = sSocket.accept();
            Thread t2 = new ClientThread(socket1);
            t2.start();
         }
      }
      catch(Exception e) {
      }
   }

   private void broadcastMessage() {
      // System.out.println("sending broadcast message : " + clients.size());  
//       for(ObjectOutputStream clientOutStream : clients) {
//          System.out.println("sending broadcast message ==");
//          try {
//             clientOutStream.writeUTF(command);
//             clientOutStream.flush();
//             Variables v = (Variables)data;
//             System.out.println("Sending player data " + v.toString());
//             clientOutStream.writeObject(data);
//             clientOutStream.reset();
//             clientOutStream.flush();
//          } catch(Exception ex)
//          {
//             ex.printStackTrace();
//          }
//       }
   }
            
   class ClientThread extends Thread {
      Socket socket2 = null;
      Scanner scn = null;
      ObjectInputStream ooi = null;
      ObjectOutputStream oos = null; 
   
      
      /** constructor */
      public ClientThread(Socket _cSocket) {
         socket2 = _cSocket;
      }
      
      /** Thread main */
      public void run() {
         try {
            ooi = new ObjectInputStream(socket2.getInputStream());
            oos = new ObjectOutputStream(socket2.getOutputStream());
            clients.add(oos);
            taLog.appendText("Request received from " + socket2.getInetAddress().getHostName()+"\n");
         
            while(true) {
               // String command = scn.nextLine();
               String label  = ooi.readUTF();
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
                  socket2.close();
                  btnStart.setText("Start");
                  break;
               
               case "Receive New Deck":
                  //receiveNewDeck();
                  break;
                  
                  
                           
            }  // switch 
            }  //while
            
            // pw.close();
            // scn.close();
            //socket2.close();
         }  // try
         catch(Exception e) {
         }
      }  // run
   }
 
}

      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      

       
  
   
   
   

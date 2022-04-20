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


public class UnoClient extends Application{

   //Window attributes
   private Stage stage;
   private Scene scene;
   private VBox root;
   
   private Label lblName = new Label("UNO");
   private TextArea taTemp = new TextArea();
   private Button btnStart = new Button("Start");
   private TextField tfServerIP = new TextField();
   
   private MenuBar menuBar = new MenuBar();
   private Menu mnuPlayer = new Menu("Player");
   private MenuItem miAdd = new MenuItem("Add Player");
   
   PrintWriter pwt = null;
   ObjectOutputStream oout = null;
   
   ArrayList<Card> handReceived = new ArrayList<Card>(); 
   
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
      fpTop.setAlignment(Pos.CENTER);
      fpTop.getChildren().addAll(lblName, tfServerIP, btnStart);
      root.getChildren().add(fpTop);
      
      FlowPane fpBot = new FlowPane(8,8);
      fpBot.setAlignment(Pos.CENTER);
      fpBot.getChildren().addAll(taTemp);
      root.getChildren().add(fpBot);
      
      miAdd.setDisable(true);
      
      //to make button do something 
      miAdd.setOnAction(new EventHandler<ActionEvent>(){
         public void handle(ActionEvent evt) {
         
         }
      
      });
      
      btnStart.setOnAction(new EventHandler<ActionEvent>(){
         public void handle(ActionEvent ae) {
         
             String label = ((Button)ae.getSource()).getText();
             switch(label) { //Switch case for buttons
             case "Start":
                doStart();
                receiveHand();
                break;
             case "Stop":
                doStop();
                break;
                     
            

            
         }
         }
      
      });
      
      scene = new Scene(root, 500, 250);
      stage.setScene(scene);
      stage.show();
      
   
   }
   
   public void doStart(){
   
       try{
          socket = new Socket(tfServerIP.getText(), SERVER_PORT);
          pwt = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
          
       
       }catch(Exception e){
          e.printStackTrace();
       }
       taTemp.appendText("Connected!\n");
       
       btnStart.setText("Stop");
      
      
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
      btnStart.setText("Start");
   }
   
   public void receiveHand(){
      try{
         OutputStream outputStream = socket.getOutputStream();
               oout = new ObjectOutputStream(outputStream);
               
         InputStream inputStream = socket.getInputStream();
               ObjectInputStream ooin = new ObjectInputStream(inputStream);
               
          handReceived = (ArrayList<Card>)ooin.readObject();
               
               //flush to print data
               oout.flush();
               
          for (int i = 0; i < handReceived.size(); i++){
                  System.out.println(handReceived.get(i).toString() +  " , ");
               } 
               
               System.out.println("Received");
      }catch(Exception e){
         e.printStackTrace();
      }
   
   }
   

}
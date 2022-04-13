import java.io.Serializable;

public class UnoInterface implements Serializable{

   public String Name;
   int PlayerNumber;

   //Constructor
   public UnoInterface(String N, int PN){

      Name = N;
      PlayerNumber = PN;

   }

   //Accessors
   public String getName(){
      return Name;
   }
   
   public int getPlayerNumber(){
      return PlayerNumber;
   }

   //Mutators
   public void setName(String x){
      Name = x;
   }
   
   public void setPlayerNumber(int x){
      PlayerNumber = x;
   }

   public String toString(){

      String output = "Player info: \nName: " + Name + "\nPlayer Number: " + PlayerNumber;
      return output;
   }

}
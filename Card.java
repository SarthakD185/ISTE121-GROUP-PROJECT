public class Card {
   
   String Color;
   int Number;
   
   //Constructor
   public Card(String C, int N){

      Color = C;
      Number = N;

   }
   
   //Accessors
   public String getColor(){
      return Color;
   }
   
   public int getNumber(){
      return Number;
   }
   
   //Mutators
   public void setColor(String x){
      Color = x;
   }
   
   public void setNumber(int x){
      Number = x;
   }
   
   public String toString(){

      String output = "Card info: Color: " + Color + " Number: " + Number;
      return output;
   }
}

import java.util.Scanner;

public class Dictionary {
    public Item[] items;




    public static void main(String args[]){
        Dictionary dict=new Dictionary();
        Scanner scanner=new Scanner(System.in);
        String command;
        while(true){
            command =scanner.next();
            switch(command){
                case "read":
                    break;
                case "find":
                    break;
                case "size":
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("command not found.");
                    break;
            }
        }
    }
    public void expandItem(){
        Item[] temp=new Item[items.length+1];
        System.arraycopy(items,0,temp,0,items.length);
        items=temp;
    }
}

class Item{
    public String word;
    public Meanings[] means;
    public void expandMean(){
        Meanings[] temp=new Meanings[means.length+1];
        System.arraycopy(means,0,temp,0,means.length);
        means=temp;
    }
}
class Meanings{
    public String type,description;
}
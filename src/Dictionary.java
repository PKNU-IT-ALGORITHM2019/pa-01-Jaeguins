import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Dictionary {
    public Item[] items;




    public static void main(String args[]){
        Dictionary dict=new Dictionary();
        Scanner scanner=new Scanner(System.in);
        String command,param;
        while(true){
            command =scanner.next();
            switch(command){
                case "read":
                    param=scanner.next();
                    dict.readFile(param);
                    break;
                case "find":
                    param=scanner.next();
                    Item result=dict.findItem(param);
                    result.print();
                    break;
                case "size":
                    System.out.println("Total number of words : "+dict.items.length);
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("command not found.");
                    break;
            }
        }
    }
    public Item findItem(String param){
        return findItem(param,0,1);
    }
    public Item findItem(String param,int index,int level){//TODO Check ascend
        int diff=param.compareTo(items[index].word);
        if(diff==0)return items[index];
        else return diff>0?
                findItem(param,(int)(index+items.length/(Math.pow(2,level))),level+1):
                findItem(param,(int)(index-items.length/(Math.pow(2,level))),level+1);
    }
    public void readFile(String src){
        File file=new File(src);
        try{
            FileReader reader=new FileReader(file);
            //TODO
            System.out.println(items.length+" word(s) found.");
            return;
        }
        catch(FileNotFoundException e){
            System.out.println("File not fount.\n");
            return;
        }
    }
    void expandItem(){
        Item[] temp=new Item[items.length+1];
        System.arraycopy(items,0,temp,0,items.length);
        items=temp;
    }
}

class Item{
    public String word;
    public Meanings[] means;
    public void print(){
        for(int i=0;i<means.length;i++){
            System.out.print(word);
            means[i].print();
        }
    }
    public void expandMean(){
        Meanings[] temp=new Meanings[means.length+1];
        System.arraycopy(means,0,temp,0,means.length);
        means=temp;
    }
}
class Meanings{
    public String type,description;
    public void print(){
        System.out.print(" : ("+type+") : ");
        System.out.println(description);
    }
}
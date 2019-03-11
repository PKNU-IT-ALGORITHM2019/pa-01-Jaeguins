import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {
    public ArrayList<Item> items=new ArrayList<Item>();
    int maxLevel=0;



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
                    if(result!=null)
                    result.print();
                    else
                        System.out.println(param+" not found");
                    break;
                case "size":
                    System.out.println("Total number of words : "+dict.items.size());
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
    public Item findItem(String param,double index,int level){//TODO Check ascend
        if(level>=maxLevel*2||index>=items.size()||index<0)return null;
        int diff=param.toLowerCase().compareTo(items.get((int)index).word.toLowerCase());
        if(diff==0)return items.get((int)index);
        else return diff>0?
                findItem(param,(index+items.size()/(Math.pow(2,level))),level+1):
                findItem(param,(index-items.size()/(Math.pow(2,level))),level+1);
    }
    public void readFile(String src){
        int count=0;
        File file=new File(src);
        try{
            FileInputStream inputStream=new FileInputStream(file);
            Scanner scanner=new Scanner(inputStream);
            while (true) {
                if (!scanner.hasNext()) break;
                String t = scanner.nextLine();
                if (t.length() == 0) continue;
                String word = t.substring(0, t.indexOf('(')-1);
                String type = t.substring(t.indexOf('(')+1, t.indexOf(')'));
                String desc = t.substring(t.indexOf(')')+1);
                addItem(word, type, desc);
                count++;
            }
            inputStream.close();
            System.out.println(items.size()+"/"+count+" word(s) found.");
            return;
        }
        catch(IOException e){
            System.out.println("File not fount.\n");
            return;
        }
    }
    void addItem(String word, String type,String desc){
        Item temp=findItem(word);
        if(temp!=null)temp.means.add(new Meanings(type,desc));
        else{
            temp=new Item(word);
            temp.means.add(new Meanings(type,desc));
            items.add(temp);
            maxLevel=(int)(Math.log(items.size())/Math.log(2));
        }
    }
}

class Item{
    public Item(String word){
        this.word=word;
    }
    public String word;
    public ArrayList<Meanings> means=new ArrayList<Meanings>();
    public void print(){
        for(int i=0;i<means.size();i++){
            System.out.print(word);
            means.get(i).print();
        }
    }
}
class Meanings{
    public Meanings(String type,String desc){
        this.type=type;
        description=desc;
    }
    public String type,description;
    public void print(){
        System.out.print(" : ("+type+") : ");
        System.out.println(description);
    }
}
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {
    private ArrayList<Item> items=new ArrayList<Item>();
    private int maxLevel=0;
    private int count;


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
                    param=scanner.nextLine().substring(1);
                    Item result=dict.findItem(param);
                    if(result!=null&&result.word.equalsIgnoreCase(param))
                        result.print();
                    else{
                        System.out.println("Not found");
                        System.out.println(dict.items.get(result.index-1).getLast());
                        System.out.println("- - -");
                        System.out.println(dict.items.get(result.index).getFirst());
                    }

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
    private int find(String param){
        return findItem(param).index;
    }
    private Item findItem(String param){
        return findItem(param,0,1);
    }
    private Item findItem(String param, double index, int level){
        if(index>=items.size()||index<0)return null;
        if(level>=maxLevel*2)return items.get((int) index);
        int diff=param.toLowerCase().compareTo(items.get((int)index).word.toLowerCase());
        if(diff==0)return items.get((int)index);
        else {
            double t=items.size() / Math.pow(2, level);
            if (t<1) return items.get((int) index);
            else return diff > 0 ?
                    findItem(param, index + t, level + 1) :
                    findItem(param, index - t, level + 1);
        }
    }
    private void readFile(String src){
        count=0;
        File file=new File(src);
        try{
            FileInputStream inputStream=new FileInputStream(file);
            Scanner scanner=new Scanner(inputStream);
            while (scanner.hasNext()) {
                String t = scanner.nextLine();
                if (t.length() == 0) continue;
                String word = t.substring(0, t.indexOf('(') - 1);
                String type = t.substring(t.indexOf('(') + 1, t.indexOf(')'));
                String desc = t.substring(t.indexOf(')') + 1);
                addItem(word, type, desc);
                count++;
            }
            inputStream.close();
            System.out.println(count+" word(s) found, "+(count-items.size())+" were merged.");
        }
        catch(IOException e){
            System.out.println("File not found.");
        }
    }
    private void addItem(String word, String type, String desc){
        Item temp=findItem(word);
        if(temp==null||!temp.word.equals(word)){
            temp=new Item(word,items.size());
            temp.means.add(new Meanings(type,desc));
            items.add(temp);
            maxLevel=(int)(Math.log(items.size())/Math.log(2));
        }
        else temp.means.add(new Meanings(type,desc));
    }
}

class Item{
    Item(String word,int index){
        this.word=word;
        this.index=index;
    }
    int index;
    String word;
    ArrayList<Meanings> means=new ArrayList<>();
    void print(){
        for (Meanings mean : means) {
            System.out.print(word);
            mean.print();
        }
    }
    String getLast(){
        return word+"("+means.get(means.size()-1)+")";
    }
    String getFirst(){
        return word+"("+means.get(0)+")";
    }
}
class Meanings{
    Meanings(String type, String desc){
        this.type=type;
        description=desc;
    }
    String type;
    private String description;
    void print(){
        System.out.print(" ("+type+") ");
        System.out.println(description);
    }
    @Override
    public String toString(){
        return type;
    }
}
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {
    private ArrayList<Item> items= new ArrayList<>();
    private int maxLevel=0;


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
                    if(result==null)System.out.println("error occurred.");
                    else if(result.keyWord.equalsIgnoreCase(param))
                        result.print();
                    else{
                        System.out.println("Not found");
                        if(result.index!=0)
                            System.out.println(dict.items.get(result.index - 1).getLast());
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
    private static int diff(String a, String b){
        String compA="",compB="";
        for(char t:a.toLowerCase().toCharArray()){
            if(t>=97&&t<=122) compA+=t;
        }
        for(char t:b.toLowerCase().toCharArray()){
            if(t>=97&&t<=122) compB+=t;
        }
        return compA.compareTo(compB);
    }
    private int find(String param){
        return findItem(param).index;
    }
    private Item findItem(String param){
        return findItem(param,0,1);
    }
    private Item findItem(String param, double index, int level){
        if(index>=items.size()||index<0)
            return null;
        if(level>=maxLevel*2)
            return items.get((int) index);
        int diff=diff(param,items.get((int)index).keyWord);//.toLowerCase().compareTo(items.get((int)index).keyWord.toLowerCase());
        if(diff==0)
            return items.get((int)index);
        else {
            double t=items.size() / Math.pow(2, level);
            if (t<0.5)
                return items.get((int) index);
            else
                if(diff > 0)
                    return findItem(param, index + t, level + 1);
                else
                    return findItem(param, index - t, level + 1);
        }
    }
    private void readFile(String src){
        int count = 0;
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
            System.out.println(count +" keyWord(s) found, "+(count -items.size())+" were merged.");
        }
        catch(IOException e){
            System.out.println("File not found.");
        }
    }
    private void addItem(String word, String type, String desc){
        Item temp=items.size()-1<0?null:items.get(items.size()-1);
        if(temp==null||diff(word,temp.keyWord)!=0){
            temp=new Item(word,items.size());
            temp.means.add(new Meanings(word,type,desc));
            items.add(temp);
            maxLevel=(int)(Math.log(items.size())/Math.log(2));
        }
        else temp.means.add(new Meanings(word,type,desc));
    }
}

class Item{
    @Override
    public String toString(){
        return keyWord;
    }
    Item(String word,int index){
        this.keyWord =word;
        this.index=index;
    }
    int index;
    String keyWord;
    ArrayList<Meanings> means=new ArrayList<>();
    void print(){
        for (Meanings mean : means) {
            mean.print();
        }
    }
    String getLast(){
        return keyWord +"("+means.get(means.size()-1)+")";
    }
    String getFirst(){
        return keyWord +"("+means.get(0)+")";
    }
}
class Meanings{
    Meanings(String word,String type, String desc){
        this.realWord=word;
        this.type=type;
        description=desc;
    }
    private String type,realWord;
    private String description;
    void print(){
        System.out.print(realWord+" ("+type+") ");
        System.out.println(description);
    }
    @Override
    public String toString(){
        return type;
    }
}
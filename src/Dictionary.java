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
    public static int diff(String a,String b){
        String tA=a.toLowerCase(),tB=b.toLowerCase();
        int aI=0,bI=0;
        char aT=tA.charAt(aI),bT=tB.charAt(bI);
        while(true){
            if(aT<97||aT>122) {
                aI++;
                if(a.length()==aI)return -1;
                aT=tA.charAt(aI);
                continue;
            }
            if(bT<97||bT>122) {
                bI++;
                if(b.length()==bI)return 1;
                bT = tB.charAt(bI);
                continue;
            }
            if(aT-bT==0){

                aI++;
                bI++;
                if(a.length()==aI){
                    if(b.length()==bI)
                        return 0;
                    return -1;
                }
                if(b.length()==bI) return 1;
                aT=tA.charAt(aI);
                bT=tB.charAt(bI);
                continue;
            }else
            return aT-bT;
        }
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
        int diff=diff(param,items.get((int)index).word);//.toLowerCase().compareTo(items.get((int)index).word.toLowerCase());
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
        Item temp=items.size()-1<0?null:items.get(items.size()-1);
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
    @Override
    public String toString(){
        return word;
    }
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
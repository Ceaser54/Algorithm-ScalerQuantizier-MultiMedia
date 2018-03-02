/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scalarquantizer;
import java.io.*;
import static java.lang.Math.abs;
import java.util.*;

class node
{
    int avg=0;
    int lvl=0;
    Vector<Integer>arr= new Vector<Integer>();
    node left=null;
    node right=null;
    node parent=null;
}

class rowInTable
{
    int lower=0,upper=0;
    int Q,Qinv;
    
}
public class ScalarQuantizer {

    static int lvls,avg=0,n=0;
    //static boolean flag=false;
    static Vector<Integer>Q=new Vector<Integer>();
    static Vector<Integer>vec= new Vector<Integer>();
    static Vector<rowInTable>table=new Vector<rowInTable>();
    static HashMap<Integer,Integer> map=new HashMap<Integer,Integer>();
    
    public static void build(node root)
    {
        root.left=new node();
        root.right=new node();
        root.left.parent=root;
        root.right.parent=root;
        
        if(root.parent!=null)
            root.lvl=root.parent.lvl;
        for(int i=0 ; i<root.arr.size();i++)
        {
            root.avg+=root.arr.elementAt(i);
        }
        root.avg/=root.arr.size();
        for(int i=0  ;i<root.arr.size();i++)
        {
            if( abs(root.arr.elementAt(i)-(root.avg-1)) <= abs(root.arr.elementAt(i)-(root.avg+1)))
            {
                root.left.arr.add(root.arr.elementAt(i));
            }
            else
                root.right.arr.add(root.arr.elementAt(i));
        }
        
        root.lvl++;
        if(root.lvl==lvls)
        {
            return;
        }
        build(root.left);
        build(root.right);
    }
    public static void write() throws IOException
    {
        FileWriter file=new FileWriter("compress.txt");
        for (int i=0 ; i<vec.size();i++) {
           for(int j=0  ;j<table.size();j++)
           {
               if(vec.elementAt(i)>=table.elementAt(j).lower && vec.elementAt(i)<=table.elementAt(j).upper)
               {
                   file.write(table.elementAt(j).Q+" ");
               }
           }
        }
        file.close();
        FileWriter fil=new FileWriter("compresstable.txt");
        for(int i=0 ; i< table.size();i++)
        {
            fil.write(table.elementAt(i).Q+" "+table.elementAt(i).Qinv+" ");
        }
        fil.close();
    }
    public static void read() throws IOException
    {
        vec=new Vector<Integer>();
        FileReader file=new FileReader("compress.txt");
        Scanner cin=new Scanner(file);
        while(cin.hasNext())
        {
            vec.add(cin.nextInt());
        }
        file.close();
        FileReader fil=new FileReader("compresstable.txt");
        Scanner ci=new Scanner(fil);
        while(ci.hasNext())
        {
            int key=ci.nextInt();
            int val=ci.nextInt();
            map.put(key, val);
        }
        fil.close();
    }
    public static void readInput() throws FileNotFoundException, IOException
    {
        FileReader file=new FileReader("input.txt");
        Scanner cin=new Scanner(file);
        while(cin.hasNext())
        {
            n++;
            int in=cin.nextInt();
            avg+=in;
            vec.add(in);
           
        }
        file.close();
    }
    public static void traverse(node root)
    {
        if(root.left==null && root.right==null)
        {
            root.avg=0;
            for(int i=0 ; i<root.arr.size();i++)
            {
                root.avg+=root.arr.elementAt(i);
            }
            if(root.arr.size()!=0)
                root.avg/=root.arr.size();
            System.out.println(root.avg);
            Q.add(root.avg);
        }
        if(root.left!=null) 
            traverse(root.left);
        if(root.right!=null)
            traverse(root.right);
    }
    public static void compress(String bits) throws IOException
    {
        readInput();
        //int in,avg=0;
        lvls=Integer.parseInt(bits);
        //Scanner cin=new Scanner(System.in);
        //lvls=cin.nextInt();
        //n=cin.nextInt();
        node temp=new node();
        /*for(int i=0 ; i<n ; i++){
            in=cin.nextInt();
            avg+=in;
            vec.add(in);
        }*/
        avg = avg / n;
        temp.arr=vec;
        
        build(temp);
        traverse(temp);
        int cum=0,totalQ=0;
        for(int i=0 ;i<Q.size();i++)
        {
            rowInTable t=new rowInTable();
            if(Q.size()-1==i)
            {
                t.lower=cum;
                t.Q=totalQ;
                totalQ++;
                t.upper=127;
                t.Qinv=Q.elementAt(i);
                cum=t.upper;
            }
            else{
                t.lower=cum;
                t.Q=totalQ;
                totalQ++;
                t.upper=(Q.elementAt(i)+Q.elementAt(i+1))/2;
                t.Qinv=Q.elementAt(i);
                cum=t.upper;
            }
            table.add(t);
            
        }
        for(int i=0 ; i<table.size();i++)
        {
            System.out.println(table.elementAt(i).lower+" "+table.elementAt(i).upper+" "+table.elementAt(i).Q+" "+table.elementAt(i).Qinv);
        }
        try{
            write();
        }
        catch(IOException e)
        {
        }
    }
    public static Vector<Integer> decompress() throws IOException
    {
        read();
        Vector<Integer> temp=new Vector<Integer>();
        for(int i=0 ; i<vec.size();i++)
        {
            temp.add(map.get(vec.elementAt(i)));
        }
        for(int i=0 ; i<temp.size();i++)
        {
            System.out.println(temp.elementAt(i));
        }
        return temp;
    }
    public static void main(String[] args) throws IOException {
     compress("4");
     decompress();
    }
    
}

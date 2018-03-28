/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.script.ScriptException;
import lineexecution.LineExecution;
import parser.BiHashMap;
import parser.Parser;
import parser.selection;
import parser.subprogram;
import readfile.pointers;
import readfile.tokenizer.Token;
import readfile.tokenizer.Tokenizer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.script.ScriptException;
/**
 *
 * @author User
 */
public class ReadFile {
    public static ArrayDeque<Integer> q = new ArrayDeque<Integer>();
    private static ArrayList<Token> tkStream = new ArrayList<Token>();
    public static List<pointers> program = new ArrayList<pointers>();
   
    public static BiHashMap bigBoard = new BiHashMap();
    public static int IFctr=0;
    public static Stack<subprogram> IFstack = new Stack();
    /**
     * @param args the command line arguments
     */
    private static final String FILENAME = "../source.txt";
    public static void main(String[] args) throws ScriptException {
        // TODO code application logic here
        
        BufferedReader br = null;
		FileReader fr = null;
		ArrayList<Token> tokGroup = new ArrayList<Token>();
		try {

			br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
                        
                        
			IFstack.add(new subprogram(true,IFctr));
                        
			String sCurrentLine;
			int ctr=0;
			while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine);
                if(sCurrentLine.equals("")){
                    continue;
                }
                StringTokenizer st = new StringTokenizer(sCurrentLine, "\"+-/*<>= (),:", true);
                String[] tokens = new String[st.countTokens()];

                for(int i=0; i<tokens.length; i++){
                    tokens[i] = "";
                }

                boolean group = false;
                int k=0;
                while(st.hasMoreTokens()){
                    String token = st.nextToken();

                    if(token.equals(" ") && !group){
                        continue;
                    }

                    if(group){
                        tokens[k] += token;
                    }else{
                        tokens[k] = token;
                    }

                    if(token.equals("\"")){
                        group = !group;
                    }
                    
                    if(!group){
                        k++;
                    }
                }


                for(int i = 0; i<tokens.length && tokens[i].equals(""); i++){
                    System.out.println("\nToken->"+i+" "+tokens[i]); //added \n

                    Tokenizer tknObj = new Tokenizer(tokens[i]);

                        System.out.println(" Line"+ctr+":"); //\n

                        Token retVal = tknObj.nextToken();

                        tkStream.add(new Token(retVal.getToken(),retVal.getTokenType()));
                        tokGroup.add(new Token(retVal.getToken(),retVal.getTokenType()));
                        System.out.println(retVal.getToken()+"=>"+retVal.getTokenType());//+"\n---------------------"

                    }
                Parser p = new Parser(tkStream);

                program.add(new pointers((ArrayList<Token>) tkStream.clone(),ctr));//this is the new program array kinda like cursor based cuz we have the tkStream containing the tokens form each line and the index kinda like our address

                if (!program.get(ctr).getCode().isEmpty()){
                    if(program.get(ctr).getCode().get(0).getToken().equals("if") || program.get(ctr).getCode().get(0).getToken().equals("else") || program.get(ctr).getCode().get(0).getToken().equals("orif") || program.get(ctr).getCode().get(0).getToken().equals("end")){//this  counts all ifs,else's,or's and end's then places them in a queue for tracking
                        q.addLast(program.get(ctr).getIndex());
                        //System.out.println("GOT IN");
                        System.out.println(q.peekLast());
                    }
                }

                tkStream.clear();
                ctr++;
                               
			}
            //LINE EXECUTION
            LineExecution lineExec = new LineExecution(tkStream);
			//For Loop to go through all token streams
            System.out.println("Value of number a(level 0): "+bigBoard.get(0,"a"));
            System.out.println("Value of word b(level 0): "+bigBoard.get(0,"b"));
            System.out.println("Value of truth c(level 0): "+bigBoard.get(0,"c"));
            System.out.println("Value of n1(level 0): "+bigBoard.get(0,"n1"));
            System.out.println("Value of n2(level 0): "+bigBoard.get(0,"n2"));
            System.out.println("Value of n3(level 0): "+bigBoard.get(0,"n3"));
            System.out.println("Value of n4(level 0): "+bigBoard.get(0,"n4"));
            System.out.println("Value of w1(level 0): "+bigBoard.get(0,"w1"));
            System.out.println("Value of w2(level 0): "+bigBoard.get(0,"w2"));
            System.out.println("Value of w3(level 0): "+bigBoard.get(0,"w3"));
            System.out.println("Value of w4(level 0): "+bigBoard.get(0,"w4"));
            System.out.println("Value of t1(level 0): "+bigBoard.get(0,"t1"));
            System.out.println("Value of t2(level 0): "+bigBoard.get(0,"t2"));
            System.out.println("Value of t3(level 0): "+bigBoard.get(0,"t3"));
            System.out.println("Value of t4(level 0): "+bigBoard.get(0,"t4"));
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
    }
    
}

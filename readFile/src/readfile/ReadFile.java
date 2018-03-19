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
    private static final String FILENAME = "C:\\Users\\Ethan Ray Mosqueda\\Desktop\\EdCode\\PL-Project\\source.txt";
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
                StringTokenizer st = new StringTokenizer(sCurrentLine, "+-/*<>= (),:", true);
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

                    if(token.charAt(token.length()-1) == '"'){
                        group = false;
                    }else if(token.charAt(0) == '"'){
                        group = true;
                    }

                    if(group){
                        tokens[k] += token;
                    }else{
                        tokens[k] = token;
                    }

                    if(!group){
                        k++;
                    }
                }


                for(int i = 0; i<tokens.length && tokens[i]!=""; i++){
                    System.out.println("Token->"+i+" "+tokens[i]);

                    Tokenizer tknObj = new Tokenizer(tokens[i]);

                        System.out.println("\n Line"+ctr+":");

                        Token retVal = tknObj.nextToken();

                        tkStream.add(new Token(retVal.getToken(),retVal.getTokenType()));
                        tokGroup.add(new Token(retVal.getToken(),retVal.getTokenType()));
                        System.out.println(retVal.getToken()+"=>"+retVal.getTokenType()+"\n---------------------");

                    }
                Parser p = new Parser(tkStream);

                program.add(new pointers((ArrayList<Token>) tkStream.clone(),ctr));//this is the new program array kinda like cursor based cuz we have the tkStream containing the tokens form each line and the index kinda like our address

                if(program.get(ctr).getCode().get(0).getToken().equals("if") || program.get(ctr).getCode().get(0).getToken().equals("else") || program.get(ctr).getCode().get(0).getToken().equals("orif") || program.get(ctr).getCode().get(0).getToken().equals("end")){//this  counts all ifs,else's,or's and end's then places them in a queue for tracking
                    q.addLast(program.get(ctr).getIndex());
                    //System.out.println("GOT IN");
                    System.out.println(q.peekLast());
                }

                tkStream.clear();
                ctr++;
                               
			}
            //LINE EXECUTION
            LineExecution lineExec = new LineExecution(tkStream);

			//For Loop to go through all token streams
            System.out.println("Value of x(level 0): "+bigBoard.get(0,"x"));
            System.out.println("Value of y(level 0): "+bigBoard.get(0,"y"));
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

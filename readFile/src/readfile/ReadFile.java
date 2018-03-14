/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Stack;
import javax.script.ScriptException;
import parser.BiHashMap;
import parser.Parser;
import parser.selection;
import readfile.tokenizer.Token;
import readfile.tokenizer.Tokenizer;
/**
 *
 * @author User
 */
public class ReadFile {
    private static ArrayList<Token> tkStream = new ArrayList<Token>();
   
    public static BiHashMap bigBoard = new BiHashMap();
    public static int IFctr=0;
    public static Stack<selection> IFstack = new Stack();
    /**
     * @param args the command line arguments
     */
    public ReadFile(){
         
    }
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
                        
                        
                        IFstack.push(new selection(true,IFctr));
                        
			String sCurrentLine;
                        int ctr=0;
			while ((sCurrentLine = br.readLine()) != null) {
                            //System.out.println(sCurrentLine);
                            StringTokenizer st = new StringTokenizer(sCurrentLine, "+-*/ (),=", true);
                            String[] tokens = new String[st.countTokens()];
                            
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
                                
                                if(token.charAt(0) == '"'){
                                    group = true;
                                }else if(token.charAt(token.length()-1) == '"'){
                                    group = false;
                                }
                                
                                if(!group){
                                    k++;
                                }
                            }
                            
                            
                                for(int i = 0; tokens[i] != null; i++){
                                    System.out.println("Token->"+i+" "+tokens[i]);

                                    Tokenizer tknObj = new Tokenizer(tokens[i]);

                                        System.out.println("\n Line"+ctr+":");
                                        
                                        Token retVal = tknObj.nextToken();

                                        tkStream.add(new Token(retVal.getToken(),retVal.getTokenType()));
                                        tokGroup.add(new Token(retVal.getToken(),retVal.getTokenType()));
                                        System.out.println(retVal.getToken()+"=>"+retVal.getTokenType()+"\n---------------------");
                                      
                                    }
                                Parser p = new Parser(tkStream);
                                tkStream.clear();
                                ctr++;
                               
			}
                        System.out.println("MAIN=>"+bigBoard.get(0,"x")+"\n");
                        System.out.println("FIRST IF=>"+bigBoard.get(1,"y"));
                        System.out.println("SECOND IF=>"+bigBoard.get(2,"a"));
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

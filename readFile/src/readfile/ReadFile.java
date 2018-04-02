/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.script.ScriptException;
import lineexecution.LineExecution;
import parser.BiHashMap;
import parser.subprogram;
import readfile.tokenizer.Token;
import readfile.tokenizer.Tokenizer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;
import parser.groups;
import parser.grpInstance;

//unused imports
import java.io.FileInputStream;
import java.io.InputStreamReader;
import parser.Parser;
import parser.selection;
import readfile.pointers;
import readfile.tokenizer.TokenType;
import javax.script.ScriptException;
/**
 *
 * @author User
 */
public class ReadFile {

    public static ArrayList<tuple> levelsAndLines = new ArrayList<tuple>();
    public static ArrayList<tuple> loopTracker = new ArrayList<tuple>();
    public static ArrayList<tuple> functionTrav = new ArrayList<tuple>();
    public static ArrayList<tuple> callTrav = new ArrayList<tuple>();
    public static ArrayDeque<Integer> q = new ArrayDeque<Integer>();
    private static ArrayList<Token> tkStream = new ArrayList<Token>();
    public static List<pointers> program = new ArrayList<pointers>();
    public static List<groups> groupDefinitions = new ArrayList<groups>();
    public static List<grpInstance> groupInstances = new ArrayList<grpInstance>();
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
                        int level=0;
			while ((sCurrentLine = br.readLine()) != null) {

               
                if(sCurrentLine.equals("")){
                    continue;
                }
                StringTokenizer st = new StringTokenizer(sCurrentLine, "\t\"+-/*<>= (),:%", true);
                String[] tokens = new String[st.countTokens()];

                for(int i=0; i<tokens.length; i++){
                    tokens[i] = "";
                }

                boolean group = false;
                int k=0;
                String lastToken = "";
                while(st.hasMoreTokens()){
                    String token = st.nextToken();
                    
                    if(token.equals("/") && lastToken.equals("/")){
                        throw new IllegalStateException("Wrong Syntax");
                    }
                    
                    if(token.equals("=")&& lastToken.equals("not")){
                        tokens[k-1] += token;
                        continue;
                    }
                    
                    if((token.equals("\t") ||token.equals(" ")) && !group){
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
                    
                    lastToken = token;
                }
                

                for(int i = 0; i<tokens.length && !tokens[i].equals(""); i++){
                   

                    Tokenizer tknObj = new Tokenizer(tokens[i]);

                    

                    Token retVal = tknObj.nextToken();

                    tkStream.add(new Token(retVal.getToken(),retVal.getTokenType()));
                    tokGroup.add(new Token(retVal.getToken(),retVal.getTokenType()));
                  

                }
                Parser p = new Parser(tkStream);
                           
                                
                program.add(new pointers((ArrayList<Token>) tkStream.clone(),ctr, p.type));//this is the new program array kinda like cursor based cuz we have the tkStream containing the tokens form each line and the index kinda like our address

                if(program.get(ctr).getCode().get(0).getToken().equals("if") || program.get(ctr).getCode().get(0).getToken().equals("else") || program.get(ctr).getCode().get(0).getToken().equals("orif")){//this  counts all ifs,else's,or's and end's then places them in a queue for tracking

                    if(program.get(ctr).getCode().get(0).getToken().equals("if")){
                        ++level;
                        
                       levelsAndLines.add(new tuple(program.get(ctr).getIndex(),level));

                    }else if(program.get(ctr).getCode().get(0).getToken().equals("else") || program.get(ctr).getCode().get(0).getToken().equals("orif")){
                        levelsAndLines.add(new tuple(program.get(ctr).getIndex(),level));
                    }

                }else if(program.get(ctr).getCode().get(0).getToken().equals("end")) {
                    System.out.println("Level: "+level);
                    boolean found = false;
                    for (int x = ctr - 1; x >= 0 && found == false;x--) {
                        if ((program.get(x).getType().equals("IF STATEMENT!")
                            || program.get(x).getType().equals("ORIF STATEMENT!")
                            || program.get(x).getType().equals("ELSE STATEMENT!"))
                            && (levelsAndLines.contains(new tuple(program.get(x).getIndex(),level)))
                          ) {
                            
                            levelsAndLines.add(new tuple(program.get(ctr).getIndex(),level--));
                            found = true;
                        } else if ((program.get(x).getType().equals("PRE TEST LOOP!"))
                                && (loopTracker.contains(new tuple(x,level)))
                        ) {

                            loopTracker.add(new tuple(program.get(ctr).getIndex(),level--));
                            found = true;
                        } else if ((program.get(x).getType().equals("JOB DECLARATION!")
                                || program.get(x).getType().equals("JOB DECLARATION WITHOUT PARAMS AND RETURN TYPE!")
                                || program.get(x).getType().equals("JOB DECLARATION WITHOUT PARAMS!")
                                || program.get(x).getType().equals("JOB DECLARATION WITHOUT RETURN TYPE!"))
                                && (functionTrav.contains(new tuple(x,level)))
                        ) {
                            
                            functionTrav.add(new tuple(program.get(ctr).getIndex(),level--));
                            found = true;
                        }

                    }

                }else if (program.get(ctr).getType().equals("GROUP DECLARATION!")) {

                    level++;

                }else if (program.get(ctr).getCode().get(0).getToken().equals("do")) {

                    loopTracker.add(new tuple(program.get(ctr).getIndex(), ++level));

                }else if (program.get(ctr).getType().equals("WHILE!")) {

                    loopTracker.add(new tuple(program.get(ctr).getIndex(), level--));

                }else if(program.get(ctr).getCode().get(0).getToken().equals("job")){
                      if(level != 0){
                          throw new IllegalStateException("Cannot define job here");
                      }
                      ++level;
                      functionTrav.add(new tuple(program.get(ctr).getIndex(),level));
                }else if(program.get(ctr).getType().equals("FUNCTION CALL!")){
                      for(int i=0; i<functionTrav.size(); i++){
                          if(program.get(functionTrav.get(i).getLine()).getType().equals("END!")){
                              continue;
                          }
                          if(program.get(functionTrav.get(i).getLine()).getCode()
                            .get(1).getToken().equals(program.get(ctr).getCode().get(0).getToken())){
                            functionTrav.get(i).setRet(new tuple(ctr,level));
                            callTrav.add(new tuple(ctr,level,new tuple(functionTrav.get(i).getLine(),1)));
                          }
                      }
                }else if (program.get(ctr).getCode().get(0).getToken().equals("repeat")
                            || program.get(ctr).getCode().get(0).getToken().equals("do")
                            || program.get(ctr).getCode().get(0).getToken().equals("foreach")) {

                    loopTracker.add(new tuple(program.get(ctr).getIndex(), ++level));
                }

                tkStream.clear();
                ctr++;
                               
            }
            System.out.println("Program and pointers");
            System.out.println("===============================");
            int ifs = 0;
            int funcs = 0;
            int loops = 0;
            int calls = 0;
            for(int i=0; i<program.size();i++){
                String line = "Line "+i+": ";
                for(int j=0; j<program.get(i).getCode().size(); j++){
                   line+=(program.get(i).getCode().get(j).getToken())+" ";
                }
                if(levelsAndLines.size() > ifs && levelsAndLines.get(ifs).getLine() == i){
                    line+= "<--- if Level "+levelsAndLines.get(ifs).getLevel();
                    ifs++;
                }else if(functionTrav.size() > funcs && functionTrav.get(funcs).getLine() == i){
                    line+= "<--- funcs Level "+functionTrav.get(funcs).getLevel();
                    if(functionTrav.get(funcs).getRet()!=null){
                        line+=" return at "+functionTrav.get(funcs).getRet().getLine();
                    }
                    funcs++;
                }else if(loopTracker.size() > loops && loopTracker.get(loops).getLine() == i){
                    line+= "<--- loop Level "+loopTracker.get(loops).getLevel();
                    loops++;
                }else if(callTrav.size() > calls && callTrav.get(calls).getLine() == i){
                    line+= "<--- call Level "+callTrav.get(calls).getLevel();
                    if(callTrav.get(calls).getRet() != null){
                        line+=" call at "+callTrav.get(calls).getRet().getLine();
                    }
                    calls++;
                }
                System.out.println(line);
            }
            
            //LINE EXECUTION
            LineExecution lineExec = new LineExecution(program);

//            ArrayList<Token> x = (ArrayList<Token>) bigBoard.get(IFstack.peek().getLevel(),"b");
//            System.out.print("Value of numbers b(level "+IFstack.peek().getLevel()+"): ("+IFctr);
//            for(int i=0; i  < x.size();i++){
//                System.out.print(" "+x.get(i).getToken());
//                if(i+1 < x.size()){
//                    System.out.print(",");
//                }
//            }
//            System.out.print(" )\n");
//
//            x = (ArrayList<Token>) bigBoard.get(0,"vw2");
//            System.out.print("Value of numbers vw2(level 0): (");
//            for(int i=0; i  < x.size();i++){
//                System.out.print(" "+x.get(i).getToken());
//                if(i+1 < x.size()){
//                    System.out.print(",");
//                }
//            }
//            System.out.print(" )\n");
//
//            x = (ArrayList<Token>) bigBoard.get(0,"vn3");
//            System.out.print("Value of numbers vn3(level 0): (");
//            for(int i=0; i  < x.size();i++){
//                System.out.print(" "+x.get(i).getToken());
//                if(i+1 < x.size()){
//                    System.out.print(",");
//                }
//            }
//            System.out.print(" )\n");
//            System.out.println("Value of number x(level IFstack.peek().getLevel()): "+bigBoard.get(IFstack.peek().getLevel(),"x"));
//            System.out.println("Value of numbers x(level IFstack.peek().getLevel()): "+bigBoard.get(IFstack.peek().getLevel(),"z"));
 //         System.out.println("Value of number a(level 0): "+bigBoard.get(IFstack.peek().getLevel(),"x"));

//            System.out.println("Value of word b(level 0): "+bigBoard.get(0,"b"));
//            System.out.println("Value of truth c(level 0): "+bigBoard.get(0,"c"));
//            System.out.println("Value of n1(level 0): "+bigBoard.get(0,"n1"));
//            System.out.println("Value of n2(level 0): "+bigBoard.get(0,"n2"));
//            System.out.println("Value of n3(level 0): "+bigBoard.get(0,"n3"));
//            System.out.println("Value of n4(level 0): "+bigBoard.get(0,"n4"));
//            System.out.println("Value of w1(level 0): "+bigBoard.get(0,"w1"));
//            System.out.println("Value of w2(level 0): "+bigBoard.get(0,"w2"));
//            System.out.println("Value of w3(level 0): "+bigBoard.get(0,"w3"));
//            System.out.println("Value of w4(level 0): "+bigBoard.get(0,"w4"));
//            System.out.println("Value of t1(level 0): "+bigBoard.get(0,"t1"));
//            System.out.println("Value of t2(level 0): "+bigBoard.get(0,"t2"));
//            System.out.println("Value of t3(level 0): "+bigBoard.get(0,"t3"));
//            System.out.println("Value of t4(level 0): "+bigBoard.get(0,"t4"));
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import readfile.tokenizer.Token;
import readfile.tokenizer.TokenData;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import readfile.tokenizer.TokenType;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import readfile.pointers;
import static readfile.ReadFile.IFctr;
import static readfile.ReadFile.IFstack;
import static readfile.ReadFile.bigBoard;
import static readfile.ReadFile.program;
import static readfile.ReadFile.q;

/**
 *
 * @author User
 */
public class Parser {
    
    List<pointers> program;
    ArrayList<Token> tkStream;
     public static Hashtable<Object, Object> varList= new Hashtable<Object, Object>();
     
     
    public Parser(ArrayList<Token> tkStream) throws ScriptException{
      this.tkStream = tkStream;
     
      Start();
    }
    
    public String stringify(){
        String s = "";
        for(int i=0; i<tkStream.size(); i++){
            switch(tkStream.get(i).getTokenType()){
                case DATA_TYPE:
                    s+="<type>";
                    break;
                case BOOLEAN_LITERAL:
                    s+="<bool>";
                    break;
                case IDENTIFIER:
                    s+="<identifier>";
                    break;
                case STRING_LITERAL:
                    s+="<string>";
                    break;
                case NUMBER_LITERAL:
                    s+="<number>";
                    break;
                case OPERATION:
                    s+="<operation>";
                    break;
                default:
                    s+=tkStream.get(i).getToken();
                    //System.out.println("--->"+tkStream.get(i).getToken()+" is "+tkStream.get(i).getTokenType());
            }
            s+=(i==tkStream.size()-1)?"":" ";
        }
        return s;
    }
    
    private String parseExpression(int start, int end){
        String expression = "";
                for(int i=start; i<end; i++){
                    expression+=tkStream.get(i).getToken();
        }
        return expression;
    }
    
    private boolean isExpression(int start){
        
    }
    
     public void Start() throws ScriptException{
        ArrayList<Token> code = new ArrayList<>();
        String line = stringify(); 
        System.out.println(line);
        
        String[] lexeme = line.split(" ");
        //System.out.println(lexeme.length+": "+(lexeme[0]+" "+lexeme[1]+"<expr>"+lexeme[lexeme.length-2]+" "+lexeme[lexeme.length-1]));
        
        if(lexeme.length >= 2 && (lexeme[0]+" "+lexeme[1]).matches("<type>\\s<identifier>")){ //lexeme
            if((lexeme.length > 3 && !lexeme[2].matches("is")) || lexeme.length == 3){
                throw new IllegalStateException("Wrong Syntax");
            }
            code.add(tkStream.get(0));
            code.add(tkStream.get(1));
            if(lexeme.length > 3 && lexeme[2].matches("is") && isExpression(3)){
                String expression = parseExpression(3, lexeme.length);
                System.out.println(expression);
                System.out.println("Expression!");
                //add necessary execution for expression; use the expression string
            }
            System.out.println("DECLARATION!");
        }else if(lexeme.length >= 5 && (lexeme[0]+" "+lexeme[1]+"<expr>"+lexeme[lexeme.length-2]+" "+lexeme[lexeme.length-1]).matches("if\\s\\(<expr>\\)\\sthen")){ //if statement
            //necessary if execution
            System.out.println("IF STATEMENT!");
            String expression = parseExpression(2,lexeme.length-2);
            System.out.println(expression);
            //add necessary execution for expression; use the expression string
        }else if(lexeme.length >= 5 && (lexeme[0]+" "+lexeme[1]+"<expr>"+lexeme[lexeme.length-2]+" "+lexeme[lexeme.length-1]).matches("orif\\s\\(<expr>\\)\\sthen")){ //orif statement
            //necessary if exectution
            System.out.println("ORIF STATEMENT!");
            String expression = parseExpression(2,lexeme.length-2);
            //add necessary execution for expression; use the expression string
        }else if(line.matches("else then")){ //else statement
            System.out.println("ELSE STATEMENT!");
        }else if(lexeme.length > 2 && (lexeme[0]+" "+lexeme[1]).matches("<identifier>\\sis")){ //assignment
            String expression = parseExpression(2, lexeme.length);
            System.out.println(expression);
            System.out.println("ASSIGNMENT!");
        }else if(lexeme.length == 1 && (lexeme[0].matches("end"))){
            System.out.println("END!");
        }else{
            throw new IllegalStateException("Wrong Syntax");
        }
    }
    
  public boolean isDeclared(String token){
      boolean val=false;
     if(varList.containsKey(token)){
       val = true;
     }else{
       val = false;
     }
     
     return val;
  }
  
  public boolean isAccessible(String token){
     boolean val = false;
     
     
     
     for(int ctr=0;ctr<=IFstack.peek().getLevel();ctr++){
         
         if( bigBoard.containsKeys(ctr, token)){
            val=true;
            break;
         }
     }
     
     return val;
  } 
    
}

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

import static readfile.ReadFile.IFctr;
import static readfile.ReadFile.IFstack;
import static readfile.ReadFile.bigBoard;

/**
 *
 * @author User
 */
public class Parser {
    
 
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
                case INTEGER_LITERAL: case FLOAT_LITERAL:
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
    
    private String parseExpression(String[] line, int start, int end){
        String expression = "";
                for(int i=start; i<end; i++){
                    expression+=line[i];
        }
        return expression;
    }
    
     public void Start() throws ScriptException{
        String line = stringify(); 
        System.out.println(line);
        
        String[] declaration = line.split(" ");
        //System.out.println(declaration.length+": "+(declaration[0]+" "+declaration[1]+"<expr>"+declaration[declaration.length-2]+" "+declaration[declaration.length-1]));
        
        if(declaration.length >= 2 && (declaration[0]+" "+declaration[1]).matches("<type>\\s<identifier>")){ //declaration
            //declare the variable
            System.out.println("DECLARATION!");
            if(declaration.length > 3 && declaration[2].matches("is")){
                String expression = parseExpression(declaration, 3, declaration.length);
                System.out.println("Expression!");
                //add necessary execution for expression; use the expression string
            }
        }else if(declaration.length >= 5 && (declaration[0]+" "+declaration[1]+"<expr>"+declaration[declaration.length-2]+" "+declaration[declaration.length-1]).matches("if\\s\\(<expr>\\)\\sthen")){ //if statement
            //necessary if exectution
            System.out.println("IF STATEMENT!");
            String expression = parseExpression(declaration, 2,declaration.length-2);
            System.out.println(expression);
            //add necessary execution for expression; use the expression string
        }else if(declaration.length >= 5 && (declaration[0]+" "+declaration[1]+"<expr>"+declaration[declaration.length-2]+" "+declaration[declaration.length-1]).matches("orif\\s\\(<expr>\\)\\sthen")){ //orif statement
            //necessary if exectution
            System.out.println("ORIF STATEMENT!");
            String expression = parseExpression(declaration, 2,declaration.length-2);
            //add necessary execution for expression; use the expression string
        }else if(line.matches("else then")){ //else statement
            System.out.println("ELSE STATEMENT!");
        }else if(declaration.length > 2 && (declaration[0]+" "+declaration[1]).matches("<identifier>\\sis")){ //assignment
            String expression = parseExpression(declaration, 2, declaration.length);
            System.out.println("ASSIGNMENT!");
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

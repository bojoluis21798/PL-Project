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
    
     public void Start() throws ScriptException{
        String line = stringify(); 
        System.out.println(line);
        
        if(line.matches("<type>\\s<identifier>\\s(is\\s(<number>)|(<identifier>)\\s(<operation>\\s(<number>)|(<identifier>))*)?")){
            System.out.println("DECLARATION!");
        }else if(line.matches("(if)\\s\\(\\s((<bool>)|(((<number>)|(<identifier>))\\s(<operation>\\s((<number>)|(<identifier>)))*))\\s\\)\\s(then)")){
            System.out.println("IF STATEMENT!");
        }else if(line.matches("(orif)\\s\\(\\s((<bool>)|(((<number>)|(<identifier>))\\s(<operation>\\s((<number>)|(<identifier>)))*))\\s\\)\\s(then)")){
            System.out.println("OR IF STATEMENT!");
        }else if(line.matches("else then")){
            System.out.println("ELSE STATEMENT!");
        }else if(line.matches("<identifier>\\s(is)\\s((<identifier>)|(<number>)|(<string>)|(<bool>))")){
            System.out.println("ASSIGNMENT!");
        }else{
            System.out.println("Wrong Syntax");
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import readfile.tokenizer.Token;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import readfile.pointers;
import static readfile.ReadFile.IFstack;
import static readfile.ReadFile.bigBoard;

//unused imports
import java.util.Iterator;
import java.util.LinkedList;
import readfile.tokenizer.TokenData;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import readfile.tokenizer.TokenType;
import static readfile.ReadFile.IFctr;
import static readfile.ReadFile.program;
import static readfile.ReadFile.q;

/**
 *
 * @author User
 */
public class Parser {
    ArrayList<Token> code = new ArrayList<>();
    List<pointers> program;
    ArrayList<Token> tkStream;
     public static Hashtable<Object, Object> varList= new Hashtable<Object, Object>();
    String[] lexeme; 
     
    public Parser(ArrayList<Token> tkStream) throws ScriptException{
        this.tkStream = tkStream;
        String line = stringify(); 
        System.out.println(line);
        
        this.lexeme = line.split(" ");
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
    
    private boolean isExpression(int start, int end){
        String expr = "";
        
        if(start > end){
            return true;
        }
        
        for(int i=start; i<=end; i++){
            expr+=(lexeme[i].equals("<operation>"))?tkStream.get(i).getToken():lexeme[i];
       }
        
        System.out.println("Expression: "+expr);
        
        expr=expr.replaceAll("<string>", "0");
        expr=expr.replaceAll("<number>","0");
        expr=expr.replaceAll("<boolean>","0");
        //expr=expr.replaceAll("(<identifier>)\\s(using)\\s\\())\\)", "");
        expr=expr.replaceAll("<identifier>", "0");
        
        System.out.println("Expression: "+expr);
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        
        Object result = null;
        try{
            result = engine.eval(expr);
        }catch(ScriptException e){
            return false;
        }
        String type = result.getClass().getTypeName();
        System.out.println("Type: "+type);
        
        return type.equals("java.lang.String") || 
               type.equals("java.lang.Integer") || 
               type.equals("java.lang.Boolean") ||
               type.equals("java.lang.Double");
    }
    
     public void Start() throws ScriptException{
        
        if(lexeme.length == 2 && (lexeme[0]+" "+lexeme[1]).matches("<type>\\s<identifier>")){
            System.out.println("DECLARATION!");
        }else if(lexeme.length > 3 && (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]).matches("<type>\\s<identifier>\\sis") && isExpression(3,lexeme.length-1)){
            System.out.println("INITIALIZATION!");
        }else if(
            lexeme.length >= 5 && 
            (lexeme[0]+" "+lexeme[1]+"<expr>"+lexeme[lexeme.length-2]+" "+lexeme[lexeme.length-1]).matches("if\\s\\(<expr>\\)\\sthen") &&
            isExpression(2,lexeme.length-3)
        ){ 
            System.out.println("IF STATEMENT!");
        }else if(lexeme.length >= 5 && 
            (lexeme[0]+" "+lexeme[1]+"<expr>"+lexeme[lexeme.length-2]+" "+lexeme[lexeme.length-1]).matches("orif\\s\\(<expr>\\)\\sthen") &&
            isExpression(2,lexeme.length-3)
        ){ //orif statement
            System.out.println("ORIF STATEMENT!");
        }else if(lexeme.length == 2 && (lexeme[0]+" "+lexeme[1]).matches("else then")){ //else statement
            System.out.println("ELSE STATEMENT!");
        }else if(
            lexeme.length > 2 && (lexeme[0]+" "+lexeme[1]).matches("<identifier>\\sis") &&
            isExpression(2,lexeme.length-1)    
        ){ //assignment
            System.out.println("ASSIGNMENT!");
        }else if(lexeme.length == 1 && (lexeme[0].matches("end"))){
            System.out.println("END!");
        }else if(
            lexeme.length >= 4 && 
            (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]+"<expr>"+lexeme[lexeme.length-1]).matches("<identifier>\\susing\\s\\(<expr>\\)") &&
            isExpression(3,lexeme.length-2)
        ){ 
            System.out.println("FUNCTION CALL!");
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

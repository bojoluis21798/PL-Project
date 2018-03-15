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
    
     public void Start() throws ScriptException{
           Object result = null;
           
          if(tkStream.get(0).getTokenType() == TokenType.KEYWORD && tkStream.get(0).getToken().equals("if")){
                IFctr++;
                List <Token> boolE = tkStream.subList(1, tkStream.size());
                for(Token tok:boolE){
                   if(tok.getToken().equals("then")){
                       List<Token> boolE2 = boolE.subList(0, boolE.size()-1);
                       String st = "";
                       for(Token token:boolE2){
                          st+=" "+token.getToken();
                        }
                        ScriptEngineManager manager = new ScriptEngineManager();
                        ScriptEngine engine = manager.getEngineByName("JavaScript");
                        result = engine.eval(st);
                        //System.out.println(result);
                        if(result.equals(true)){
                            //System.out.println(IFctr);
                           IFstack.push(new selection( true,IFctr));
                        }else{
                           IFstack.push(new selection(false,IFctr));
                        }
                        
                    }
                  }
                
            }else if(tkStream.get(0).getTokenType() == TokenType.KEYWORD && tkStream.get(0).getToken().equals("orif")  ){
               
                if(IFstack.peek().getBool() == false){
                    IFstack.pop();
                    

                     List <Token> boolE = tkStream.subList(1, tkStream.size());
                     for(Token tok:boolE){
                        if(tok.getToken().equals("then")){
                            List<Token> boolE2 = boolE.subList(0, boolE.size()-1);
                            String st = "";
                            for(Token token:boolE2){
                               st+=" "+token.getToken();
                             }
                             ScriptEngineManager manager = new ScriptEngineManager();
                             ScriptEngine engine = manager.getEngineByName("JavaScript");
                             result = engine.eval(st);
                             //System.out.println(result);
                             if(result.equals(true)){
                                 //System.out.println(IFctr);
                                IFstack.push(new selection( true,IFctr));
                             }else{
                                IFstack.push(new selection(false,IFctr));
                             }
                         }
                       }
                }else{
                    IFstack.pop();
                    

                     List <Token> boolE = tkStream.subList(1, tkStream.size());
                     for(Token tok:boolE){
                        if(tok.getToken().equals("then")){
                            List<Token> boolE2 = boolE.subList(0, boolE.size()-1);
                            String st = "";
                            for(Token token:boolE2){
                               st+=" "+token.getToken();
                             }
                             ScriptEngineManager manager = new ScriptEngineManager();
                             ScriptEngine engine = manager.getEngineByName("JavaScript");
                             result = engine.eval(st);
                             //System.out.println(result);
                             if(result.equals(true)){
                                 //System.out.println(IFctr);
                                IFstack.push(new selection( true,IFctr));
                             }else{
                                IFstack.push(new selection(false,IFctr));
                             }
                         }
                       }
                }
               
            }else if(tkStream.get(0).getToken().equals("else")){//do else statement
                if(IFstack.peek().getBool() == false){
                    System.out.println("GOT IN");
                    IFstack.pop();
                    IFctr++;
                        IFstack.push(new selection(true,IFctr));
                         if(tkStream.get(0).getTokenType() == TokenType.DATA_TYPE){
                             declare();
                         }else{
                             System.out.println("Not a Declaration/Initialization");
                         }

                        if(tkStream.size() > 4){//expression
                             int ctr=0;

                             for(Token tok:tkStream){
                             if(tok.getToken().equals("is")){
                                break;
                             }
                                 ctr++;
                             }
                             List<Token> expressions=tkStream.subList(ctr, tkStream.size());

                             //expr(expressions);
                         }
                }
                if(IFstack.peek().getBool() == true){
                   IFstack.pop();
                   IFstack.push(new selection(false,IFctr));
                }
               
            }else if(tkStream.get(0).getToken().equals("end") ){  //check if end 
             
                  IFstack.pop();
               
          }else if(IFstack.peek().getLevel() > 0 && IFstack.peek().getBool() == true){//do this statement if if-condition is true
              System.out.println("GOT IN");
              if(tkStream.get(0).getTokenType() == TokenType.DATA_TYPE){
                    declare();
              }else{
                    System.out.println("Not a Declaration/Initialization");
               }
               
               if(tkStream.size() > 4){//expression
                    int ctr=0;

                    for(Token tok:tkStream){
                    if(tok.getToken().equals("is")){
                       break;
                    }
                        ctr++;
                    }
                    List<Token> expressions=tkStream.subList(ctr, tkStream.size());

                    //expr(expressions);
                }
               
          }
           
           if(IFstack.peek().getLevel()==0 && IFstack.peek().getBool() == true){//checks if in main
             System.out.println("got in");
              if(tkStream.get(0).getTokenType() == TokenType.DATA_TYPE){
                    declare();
              }else{
                    System.out.println("Not a Declaration/Initialization");
              }
               
               if(tkStream.size() > 4){//expression
                    int ctr=0;

                    for(Token tok:tkStream){
                        if(tok.getToken().equals("is")){
                           break;
                        }
                            ctr++;
                    }
                    List<Token> expressions=tkStream.subList(ctr+1, tkStream.size());

                    //expr(expressions);
                }
           }
    }
    public void declare(){
        switch(tkStream.get(0).getToken()){
            case "number":
                number();
                break;
            case "word":
                word();
                break;
            case "flag":
                flag();
                break;
//            case "numbers":
//                numbers();
//                break;
//            case "words":
//                words();
//                break;
//            case "flags":
//                flags();
//                break;
            default:
                System.out.println("Invalid Syntax: No such data type");
        }
    }
    
    public String stringify(){
        String s = "";
        
        for(int i=1; i<tkStream.size(); i++){
            s +=tkStream.get(i);
        }
        return s;
    }
    
    public void number(){
        String s = stringify();
        
        if(s.matches("")){
            
        }else{
            System.out.println("Invalid Syntax");
        }
        /*if(tkStream.get(1).getTokenType() == TokenType.IDENTIFIER){
            if(tkStream.get(2).getTokenType() == TokenType.KEYWORD && tkStream.get(2).getToken().equals("is")){
               
                if(tkStream.get(3).getTokenType() == TokenType.INTEGER_LITERAL || tkStream.get(3).getTokenType() == TokenType.FLOAT_LITERAL){
                    if(isDeclared(tkStream.get(1).getToken())){
                        System.out.println("UNACCEPTABLE number declaration");
                    }else{
                       bigBoard.put(IFstack.peek().getLevel(),tkStream.get(1).getToken(),Integer.parseInt(tkStream.get(3).getToken()));
                       System.out.println(bigBoard.get(IFstack.peek().getLevel(),"y"));
                       System.out.println("This is a number declaration");
                    }
                    
                }else{
                    System.out.println("Invalid Syntax: not a number value");
                }
            }else{
                System.out.println("Invalid Syntax: not an initialization");
            }
        }else{
            System.out.println("Invalid Syntax: no variable name");
        }*/
        
    }

    public void word(){
        if(tkStream.get(1).getTokenType() == TokenType.IDENTIFIER){
            if(tkStream.get(2).getTokenType() == TokenType.KEYWORD && tkStream.get(2).getToken().equals("is")){
                if(tkStream.get(3).getTokenType() == TokenType.IDENTIFIER){
                    System.out.println("This is a word declaration");
                    
                }else{
                    System.out.println("Invalid Syntax: not a word value");
                }
            }else{
                System.out.println("Invalid Syntax: not an initialization");
            }
        }else{
            System.out.println("Invalid Syntax: no variable name");
        }
    }

    public void flag(){
        if(tkStream.get(1).getTokenType() == TokenType.IDENTIFIER){
            
            if(tkStream.get(2).getTokenType() == TokenType.KEYWORD && tkStream.get(2).getToken().equals("is")){
                if(tkStream.get(3).getToken().equals("true") || tkStream.get(3).getToken().equals("false")){
                    System.out.println("This is a flag declaration");
                }else{
                    System.out.println("Invalid Syntax: not a flag value");
                }
            }else{
                System.out.println("Invalid Syntax: not an initialization");
            }
        }else{
            System.out.println("Invalid Syntax: no variable name");
        }
    }
    
    /*public void expr(List<Token> exp){
       
        
       TokenData expr = new TokenData(Pattern.compile("((\\s*\\d+\\s*[+|\\*|\\-|/]\\s*\\d+\\s*[+|\\*|\\-|/]\\s*\\d+\\s*[+|\\*|\\-|/]\\s*\\d+\\s*)|(\\s*\\d+\\s*[+|\\*|\\-|/]\\s*\\d+))*"),TokenType.EXPRESSION);
       TokenData expr2 = new TokenData(Pattern.compile("[a-zA-Z][a-zA-Z0-9]*"),TokenType.IDENTIFIER);
       
       String st = new String();
          st="";
       for(Token tok:exp){
          st+=tok.getToken();
       }
       System.out.println(st);
       Matcher matcher = expr2.getPattern().matcher(st);
       Matcher matcher2 = expr.getPattern().matcher(st);
       
       if(matcher.find()){  
            TokenData expr3 = new TokenData(Pattern.compile("(\\s*\\w+\\s*[+|\\*|\\-|/]\\s*\\w+\\s*[+|\\*|\\-|/]\\s*\\w+\\s*[+|\\*|\\-|/]\\s*\\w+\\s*)|(\\s*\\w+\\s*[+|\\*|\\-|/]\\s*\\w+)*"),TokenType.EXPRESSION);
            Matcher matcher3 = expr3.getPattern().matcher(st);
          if(matcher3.find()){
              boolean bool = false;
              for(Token tok:exp){
                  //System.out.println(tok.getToken());
                 if(tok.getTokenType() == TokenType.IDENTIFIER && isDeclared(tok.getToken())){
                   bool = true;
                 }
              }
              if(bool == true){
                  System.out.println("THIS IS AN ACCEPTABLE MATHEMATICAL EXPRESSION WITH VARIABLES");
              }else{
                  System.out.println("UNACCEPTABLE VARIABLE DECLARATION");
              }
          }else{
             System.out.println("THIS IS NOT AN ACCEPTABLE MATHEMATICAL EXPRESSION WITH VARIABLES");
          }
       }else if(matcher2.find()){
          System.out.println("THIS IS AN ACCEPTABLE MATHEMATICAL EXPRESSION ");
       }else{
           System.out.println("Invalid Mathematical Expression");
       }
       
       
       
    }*/
  
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

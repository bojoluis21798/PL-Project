/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import readfile.tokenizer.Token;
import readfile.tokenizer.TokenData;
import readfile.tokenizer.TokenType;

/**
 *
 * @author User
 */
public class Parser {
    
 
    ArrayList<Token> tkStream;
   
    
    public Parser(ArrayList<Token> tkStream){
      this.tkStream = tkStream;
      Start();
    }
    
     public void Start(){
        if(tkStream.get(0).getTokenType() == TokenType.DATA_TYPE){
            declare();
        }else{
            System.out.println("Not a Declaration/Initialization");
        }
        
        System.out.println(tkStream.size());
        if(tkStream.size() > 4){
           
          
            List<Token> expressions=tkStream.subList(3, tkStream.size());
            for(Token tok:expressions) {
               System.out.println(tok.getToken());
            }
            expr(expressions);
            
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
    
    public void number(){
        if(tkStream.get(1).getTokenType() == TokenType.IDENTIFIER){
            if(tkStream.get(2).getTokenType() == TokenType.KEYWORD && tkStream.get(2).getToken().equals("is")){
                if(tkStream.get(3).getTokenType() == TokenType.INTEGER_LITERAL || tkStream.get(3).getTokenType() == TokenType.FLOAT_LITERAL){
                    System.out.println("This is a number declaration");
                }else{
                    System.out.println("Invalid Syntax: not a number value");
                }
            }else{
                System.out.println("Invalid Syntax: not an initialization");
            }
        }else{
            System.out.println("Invalid Syntax: no variable name");
        }
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
    
    public void expr(List<Token> exp){
       TokenData expr = new TokenData(Pattern.compile("(\\s*\\d+\\s*[+|\\*|-|/]\\s*\\d+\\s*[+|\\*|-|/]\\s*\\d+\\s*[+|\\*|-|/]\\s*\\d+\\s*)*"),TokenType.EXPRESSION);
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
            TokenData expr3 = new TokenData(Pattern.compile("\\s*\\w+\\s*[\\+|-|\\*|\\/]\\s*\\w+\\s*"),TokenType.EXPRESSION);
            Matcher matcher3 = expr3.getPattern().matcher(st);
          if(matcher3.find()){
             System.out.println("THIS IS AN ACCEPTABLE MATHEMATICAL EXPRESSION WITH VARIABLES");
          }else{
             System.out.println("THIS IS NOT AN ACCEPTABLE MATHEMATICAL EXPRESSION WITH VARIABLES");
          }
       }else if(matcher2.find()){
          System.out.println("THIS IS AN ACCEPTABLE MATHEMATICAL EXPRESSION ");
       }else{
           System.out.println("Invalid Mathematical Expression");
       }
    }
  public void varExpr(List<Token> exp){
      TokenData expr = new TokenData(Pattern.compile("\\s*\\w+\\s*[\\+|-|\\*|\\/]\\s*\\w+\\s*"),TokenType.EXPRESSION);
      String st = new String();
          st="";
       for(Token tok:exp){
          st+=tok.getToken();
       }
       Matcher matcher = expr.getPattern().matcher(st);
       if(matcher.find()){
          System.out.println("THIS IS AN ACCEPTABLE MATHEMATICAL EXPRESSION WITH VARIABLES");
       }else{
           System.out.println("Invalid Mathematical Expression");
       }
  }
    /*void expr(List<Token> subArr){
        boolean retval = A1(subArr);//first rule if it is only an integer then ok
        if(retval){
           System.out.println("ACCEPTABLE");
        }else{
           System.out.println("DIDN'T WORK");
        }
    }
    
    boolean A1(List<Token> character){
        for(Token tok:character){
            if(tok.getTokenType().equals(TokenType.FLOAT_LITERAL)){
                return true;
            }else{
                return false;
            }
        }
       throw new IllegalStateException("Could not parse!");
    }*/
}

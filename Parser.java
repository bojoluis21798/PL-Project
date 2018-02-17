/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.List;
import readfile.tokenizer.Token;
import readfile.tokenizer.TokenType;

/**
 *
 * @author User
 */
public class Parser {
    
    ArrayList<Token> tkStream;
    public enum Grammar{
       NON_TERMINAL,TERMINAL,RULES,
    }
    
    public Parser(ArrayList<Token> tkStream){
      this.tkStream = tkStream;
      Start();
    }
    
    public void Start(){
       for(Token tok:tkStream){
           if(tok.getTokenType().equals(TokenType.FLOAT_LITERAL)){
             System.out.println("HELLO");
           }
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

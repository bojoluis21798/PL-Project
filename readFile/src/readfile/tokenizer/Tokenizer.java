/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile.tokenizer;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author User
 */
public class Tokenizer {
    private ArrayList<TokenData> tokDatas;
    
    private String str;
    private Token lastToken=null;
    private boolean pushBack;
    
    public Tokenizer(String str){
       this.tokDatas = new ArrayList<TokenData>();
       this.str = str;
        
       tokDatas.add(new TokenData("\"[a-zA-Z0-9\\s]*\"",TokenType.STRING_LITERAL));
       tokDatas.add(new TokenData("[a-zA-Z][a-zA-Z0-9]*",TokenType.IDENTIFIER));
       tokDatas.add(new TokenData("[\\+|\\*|-|/]|[<>=]",TokenType.OPERATION));
       tokDatas.add(new TokenData("^-?\\d+$",TokenType.INTEGER_LITERAL));
       tokDatas.add(new TokenData("^([-]?\\d*\\.?\\d*)$",TokenType.FLOAT_LITERAL));
       tokDatas.add(new TokenData("(true)",TokenType.BOOLEAN_LITERAL));
       tokDatas.add(new TokenData("(false)",TokenType.BOOLEAN_LITERAL));
       
       for(String t:new String[]{"=","\\)","\\(",",",":"}){
         tokDatas.add(new TokenData(t,TokenType.TOKEN));
       }
    }
   
    public Token nextToken(){
        str = str.trim();
        if(pushBack) {
          pushBack = false;
          return lastToken;
        }
        
        if(str.isEmpty()){
           return (lastToken = new Token("",TokenType.EMPTY));
        }
        //tokenize(str)
        //while{}
        
        
          for(TokenData data: tokDatas) {
            
            if(str.matches(data.getPattern())){
                 if(data.getType() == TokenType.STRING_LITERAL) {
                     return (lastToken = new Token(str.substring(1,str.length()-1),TokenType.STRING_LITERAL));
                  } else {
                     if( str.equals("if") || str.equals("then") || str.equals("end") || str.equals("while")||str.equals("is")||str.equals("AND")){
                         if(str.equals("AND")){
                          return (lastToken = new Token("&&",TokenType.KEYWORD));
                         }
                          return (lastToken = new Token(str,TokenType.KEYWORD));
                      }else if(str.equals("number") || str.equals("word") || str.equals("flag")){
                          return (lastToken = new Token(str,TokenType.DATA_TYPE));
                      }else if(data.getType()==TokenType.OPERATION){
                        return (lastToken = new Token(str,TokenType.OPERATION));
                      }else if(data.getType()==TokenType.IDENTIFIER){
                          
                         return (lastToken = new Token(str,TokenType.IDENTIFIER));
                      }else{
                          return (lastToken = new Token(str,data.getType()));
                      }
                      
                  }
                
            }
         }
        throw new IllegalStateException("Could not parse!");
     
    }
    
    public boolean hasNextToken(){
        return !str.isEmpty();
    }
    
    public void pushBack(){
          if(lastToken!=null){
              this.pushBack = true;
          }
    }
}

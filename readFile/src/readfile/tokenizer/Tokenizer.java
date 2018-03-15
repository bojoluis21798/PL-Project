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
       
       tokDatas.add(new TokenData("(false)|(true)",TokenType.BOOLEAN_LITERAL));
       tokDatas.add(new TokenData("\"[^\"]*\"",TokenType.STRING_LITERAL));
       tokDatas.add(new TokenData("[a-zA-Z][a-zA-Z0-9]*",TokenType.IDENTIFIER));
       tokDatas.add(new TokenData("[\\+|\\*|-|/]|[<>=]",TokenType.OPERATION));
       tokDatas.add(new TokenData("^-?\\d+$",TokenType.INTEGER_LITERAL));
       tokDatas.add(new TokenData("^([-]?\\d*\\.?\\d*)$",TokenType.FLOAT_LITERAL));
       
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
            String token = str;
            if(str.matches(data.getPattern())){
                 if(data.getType() == TokenType.STRING_LITERAL) {
                     return (lastToken = new Token(token.substring(1,token.length()-1),TokenType.STRING_LITERAL));
                  } else {
                     if(token.equals("else") || token.equals("orif") || token.equals("if") || token.equals("then") || token.equals("end") || token.equals("while")||token.equals("is")||token.equals("and")){
                         if(token.equals("and")){
                          return (lastToken = new Token("&&",TokenType.KEYWORD));
                         }else if(token.equals("or")){
                             return (lastToken = new Token("||",TokenType.KEYWORD));
                         }else if(token.equals("not")){
                             return (lastToken = new Token("!",TokenType.KEYWORD));
                         }else if(token.equals("notequal")){
                             return (lastToken = new Token("!=",TokenType.KEYWORD));
                         }
                          return (lastToken = new Token(token,TokenType.KEYWORD));
                      }else if(token.equals("number") || token.equals("word") || token.equals("truth")){
                          return (lastToken = new Token(token,TokenType.DATA_TYPE));
                      }else if(data.getType()==TokenType.OPERATION){
                        return (lastToken = new Token(token,TokenType.OPERATION));
                      }else if(data.getType()==TokenType.IDENTIFIER){
                          
                         return (lastToken = new Token(token,TokenType.IDENTIFIER));
                      }else{
                          return (lastToken = new Token(token,data.getType()));
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

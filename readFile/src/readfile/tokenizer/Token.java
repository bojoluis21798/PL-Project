/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile.tokenizer;

/**
 *
 * @author User
 */
public class Token {
    private String token;
    private TokenType type;
    
    public Token(String token,TokenType type){
        this.token = token;
        this.type = type;
    }
    
    public String getToken(){
      return token;
    }
    
    public TokenType getTokenType(){
        return type;
    }

    public void setTokenType(TokenType t){
        this.type = t;
    }
}


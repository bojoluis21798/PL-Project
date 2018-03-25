/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile.tokenizer;

import java.util.regex.Pattern;

/**
 *
 * @author User
 */
public class TokenData {
    private String patt;
    private TokenType type;
    
    public TokenData(String patt,TokenType type){
       this.patt = patt;
       this.type=type;
    }
    public String getPattern(){
        return patt;
    }
    
    public TokenType getType(){
       return type;
    }
}

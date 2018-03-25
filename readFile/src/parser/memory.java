/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import readfile.tokenizer.TokenType;

/**
 *
 * @author User
 */
public class memory {
    Object var;
    TokenType tok;
    
    public memory(Object var,TokenType tok){
        this.var = var;
        this.tok = tok;
    }
}

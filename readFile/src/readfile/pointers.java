/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile;

import java.util.ArrayList;
import java.util.List;
import readfile.tokenizer.Token;

/**
 *
 * @author User
 */
public class pointers {
    //private String code;
    private List<Token> tokenStream = new ArrayList<Token>();
    private int index;
    
    public pointers(ArrayList<Token> code,int index){
          this.tokenStream = code;
          this.index = index;
    }
    
    public List<Token> getCode(){
          return tokenStream;
    }
    
    public int getIndex(){
           return index; 
    }
}

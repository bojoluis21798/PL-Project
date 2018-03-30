/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;
//unused imports
import readfile.tokenizer.TokenType;

/**
 *
 * @author User
 */
public class member {
    private static Object value;
    private static String dataType;
    private static String memberName;
    
    public member(Object value,String dataType,String memberName){
        this.value = value;
        this.dataType = dataType;
        this.memberName = memberName;
    }
    
    public Object getValue(){
       return this.value;
    }
    
    public String getDT(){
       return this.dataType;
    }
    
    public String getMemberName(){
       return this.memberName;
    }
    
    public void setValue(Object value){
       this.value = value;
    }
}

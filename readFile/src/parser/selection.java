/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author User
 */
public class selection {
    
    private boolean bool;
    private int level;
    
    public selection(boolean bool,int level){
        this.bool = bool;
        this.level = level;
    }
    
    public int getLevel(){
       return this.level;
    }
    
    public boolean getBool(){
       return this.bool;
    }
    
}

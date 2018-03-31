/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile;

/**
 *
 * @author User
 */
public class tuple {
    private int line;
    private int level;
    
    public tuple(int line,int level){
        this.line = line;
        this.level = level;
    }
    
    public int getLine(){
        return this.line;
    }
    
    public int getLevel(){
        return this.level;
    }
}

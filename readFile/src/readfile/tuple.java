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
    
    @Override
    public boolean equals(Object v){
        boolean ret = false;
        
        if(v instanceof tuple){
            tuple t = (tuple) v;
            ret = t.getLine() == this.getLine() && t.getLevel() == this.getLevel();
        }
        return ret;
    }
    
    public int getLine(){
        return this.line;
    }
    
    public int getLevel(){
        return this.level;
    }
}

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
    private tuple ret;
    
    public tuple(int line,int level){
        this.line = line;
        this.level = level;
        this.ret = null;
    }
    
    public tuple(int line, int level, tuple ret){
        this(line,level);
        this.ret = ret;
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
    
    public tuple getRet(){
        return this.ret;
    }
    
    public void setRet(tuple ret){
        this.ret = ret;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.List;
import javax.script.ScriptException;
import static readfile.ReadFile.*;
import readfile.tokenizer.Token;
import readfile.tokenizer.TokenType;
import lineexecution.LineExecution;
import readfile.pointers;
/**
 *
 * @author Bojo Alcisto
 */
public class job {
    private static int jobExists(String identifier){
        for(int i=0; i<callTrav.size();i++){
            if(program.get(callTrav.get(i).getLine()).getCode().get(0).getToken().equals(identifier) && 
                callTrav.get(i).getRet() != null){
                return i;
            }
        }
        return -1;
    }
    
    public static void call(String identifier) throws ScriptException{
        if(jobExists(identifier) == -1){
            throw new IllegalStateException("Job does not exist");
        }
        int open = callTrav.get(jobExists(identifier)).getRet().getLine();
        int close = functionTrav.get(functionTrav.indexOf(callTrav.get(jobExists(identifier)).getRet())+1).getLine();
        //println("Open: "+open);
        //println("Close: "+close);
        ArrayList<Token> dec = (ArrayList<Token>)program.get(callTrav.get(jobExists(identifier)).getRet().getLine()).getCode();
        String decType = program.get(callTrav.get(jobExists(identifier)).getRet().getLine()).getType();
        ArrayList<Token> call = (ArrayList<Token>)program.get(callTrav.get(jobExists(identifier)).getLine()).getCode();
        
        //println("Call");
        String line = "";
        for(int i=0; i<call.size();i++){
            line += call.get(i).getToken();
        }
        //println(line);
        
        //println("DEC");
        line = "";
        for(int i=0; i<dec.size();i++){
            line += dec.get(i).getToken();
        }
        //println(line);
        
        ArrayList<Token> params = new ArrayList<Token>();
        
        int start = 0;
        int end = 0;
        ArrayList<pointers> func = new ArrayList<pointers>();
        
        if(decType.equals("JOB DECLARATION!")){
            start = 7;
            end = dec.size()-2;
        }else if(decType.equals("JOB DECLARATION WITHOUT RETURN TYPE!")){
            start = 4;
            end = dec.size()-2;
        }
        //println("start:"+start);
        //println("end:"+end);
        line = "";
        int j = 3;
        for(int i=start; i<=end; i++){
            params.add(dec.get(i));
            
            if(dec.get(i).getToken().equals(",") || i == end){
                params.add(new Token("is",TokenType.KEYWORD));
                for(;j<call.size()-1 && !call.get(j).getToken().equals(","); j++){
                    params.add(call.get(j));
                }j++;
                for(int k=0; k<params.size();k++){
                    line+=params.get(k).getToken();
                }//println(line);
                func.add(new pointers(params,i-start));
            }
        }
        
        for(int i=open+1; i<=close-1; i++){
            pointers t = program.get(i); 
            func.add(t);
        }
        line = "";
        //println(func.size());
        //println("Lines:");
        for(int i=0;i<func.size();i++){
            line = "";
            for(int k=0;k<func.get(i).getCode().size(); k++){
                line+=func.get(i).getCode().get(k).getToken();
            }
            //println(line);
        }
        
        for(int i=0;i<func.size();i++){
            Iffer.execute((ArrayList<Token>)func.get(i).getCode());
        }
    }
}

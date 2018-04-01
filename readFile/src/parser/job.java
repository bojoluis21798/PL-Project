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
            if(program.get(callTrav.get(i).getLine()).getCode().get(0).equals(identifier) && 
                callTrav.get(i).getRet() != null){
                return i;
            }else return -1;
        }
        return -1;
    }
    
    public static void call(String identifier) throws ScriptException{
        if(jobExists(identifier) == -1){
            throw new IllegalStateException("Job does not exist");
        }
        int open = callTrav.get(jobExists(identifier)).getRet().getLine();
        int close = functionTrav.get(functionTrav.indexOf(callTrav.get(jobExists(identifier)).getRet())+1).getLine();
        
        ArrayList<Token> dec = (ArrayList<Token>)program.get(callTrav.get(jobExists(identifier)).getRet().getLine()).getCode();
        String decType = program.get(callTrav.get(jobExists(identifier)).getRet().getLine()).getType();
        ArrayList<Token> call = (ArrayList<Token>)program.get(callTrav.get(jobExists(identifier)).getLine()).getCode();
        IFctr++;
        IFstack.push(new selection(true,IFctr));
        
        ArrayList<Token> params = new ArrayList<Token>();
        
        int start = 0;
        int end = 0;
        
        if(decType.equals("JOB DECLARATION!")){
            start = 7;
            end = dec.size()-2;
        }else if(decType.equals("JOB DECLARATION WITHOUT RETURN TYPE!")){
            start = 5;
            end = dec.size()-2;
        }
        
        int j = 3;
        for(int i=start; i<=end; i++){
            params.add(dec.get(i));
            if(dec.get(i).getToken().equals(",")){
                params.add(new Token("is",TokenType.KEYWORD));
                for(;j<call.size()-1 && !call.get(j).getToken().equals(","); j++){
                    params.add(call.get(j));
                }j++;
                Iffer.execute(params);
                params.clear();
            }
        }
        
        ArrayList<pointers> func = new ArrayList<pointers>();
        for(int i=open+1; i<close-1; i++){
             func.add(program.get(i));
        }
        new LineExecution(func);
        
    }
}

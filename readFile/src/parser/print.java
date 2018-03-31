/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import static readfile.ReadFile.bigBoard;
import readfile.tokenizer.Token;
import readfile.tokenizer.TokenType;


/**
 *
 * @author User
 */
public class print {
    public static void printIt(ArrayList<Token> code) throws ScriptException{
        
        String st = "";
        Object result = null;
         List<Token> thingToPrint = code.subList(2, code.size());
         
         
         for(Token token:thingToPrint){
             //st+=token.getToken();
             
            String variable = token.getToken();
            if(token.getTokenType().equals(TokenType.IDENTIFIER)){
               
               if (InitAssign.isInitialized(variable) && InitAssign.isAccessible(variable)){
                            
                            int levelOfVariable = InitAssign.accessLevelOf(variable);
                            variable= bigBoard.get(levelOfVariable,variable).toString();
                            
                            
                }else{
                   if(!InitAssign.isInitialized(variable)){
                       throw new IllegalStateException("Error: Variable not Initialized");
                   }
                   if(!InitAssign.isAccessible(variable)){
                       throw new IllegalStateException("Error: Variable not Accessible in this Level");
                   }
                }
                
            }
            st+=variable;
         }
         

        String replace = st.replace("(", "");
        String replace2 = replace.replace(")", "");
       
        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        result = engine.eval(replace2);
        System.out.println(result);
    }
}

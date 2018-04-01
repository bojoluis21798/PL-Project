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
import static parser.groups.accessGroup;

import static readfile.ReadFile.IFstack;
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

        int indexOfVector = -1;
         for(int x = 0; x < thingToPrint.size(); x++){
                //System.out.println(thingToPrint.get(2).getTokenType());
             if(thingToPrint.get(0).getTokenType().equals(TokenType.TOKEN) && thingToPrint.get(1).getTokenType().equals(TokenType.IDENTIFIER) && thingToPrint.get(0).getTokenType().equals(TokenType.TOKEN)){
                 
                 String variable = thingToPrint.get(1).getToken();
                 if (InitAssign.isInitialized(variable) && InitAssign.isAccessible(variable)){
                     int levelOfVariable = InitAssign.accessLevelOf(variable);
                     String value;
                     if(bigBoard.getTokenType(levelOfVariable,variable) == TokenType.STRING_LITERAL){
                         //System.out.println(bigBoard.get(levelOfVariable,variable));
                         value = "\""+bigBoard.get(levelOfVariable,variable).toString()+"\"";
                         
                     }else{
                         value = bigBoard.get(levelOfVariable,variable).toString();
                     }
                     st+=" "+value;
                 }else{
                     throw new IllegalStateException("Error: Variable "+variable+" not in HashMap");
                 }
                 x = thingToPrint.size();
             }else if(thingToPrint.get(x).getTokenType().equals(TokenType.STRING_LITERAL)){
                 st+=" "+"\""+thingToPrint.get(x).getToken()+"\"";
             }else if(thingToPrint.get(x).getTokenType().equals(TokenType.ORDINAL)){
                 indexOfVector = Integer.parseInt(thingToPrint.get(x).getToken().substring(0,1))-1;
                 String vectorVariable = thingToPrint.get(x+2).getToken();
                 System.out.println("vectorVariable: "+vectorVariable);
                 if(     InitAssign.isInitialized(vectorVariable) &&
                         InitAssign.isAccessible(vectorVariable) &&
                         bigBoard.get(IFstack.peek().getLevel(),vectorVariable) instanceof ArrayList){
                     ArrayList<Token> vector = (ArrayList<Token>)bigBoard.get(IFstack.peek().getLevel(),vectorVariable);
                     if(indexOfVector < vector.size()){
                         st+=" "+vector.get(indexOfVector).getToken();
                         x += 2;
                     }else{
                         throw new IllegalStateException("Error: vector has no "+thingToPrint.get(x).getToken()+" ordinal");
                     }
                 }else{
                     if(!InitAssign.isInitialized(vectorVariable))
                         throw new IllegalStateException("Error: variable "+vectorVariable+" not initialized");
                     if(!InitAssign.isAccessible(vectorVariable))
                         throw new IllegalStateException("Error: variable cannot be accessed in level");
                     if(!(bigBoard.get(IFstack.peek().getLevel(),vectorVariable) instanceof ArrayList)){
                         throw new IllegalStateException("Error: variable is not a vector : "+bigBoard.get(IFstack.peek().getLevel(),vectorVariable));
                     }
                 }
             }else if(thingToPrint.get(1).getTokenType().equals(TokenType.IDENTIFIER) && thingToPrint.get(2).getToken().equals("of") && thingToPrint.get(3).getTokenType().equals(TokenType.IDENTIFIER)){
                 st+=" "+accessGroup(thingToPrint.get(3).getToken(),thingToPrint.get(1).getToken());
                 
                 x = thingToPrint.size();
             }else{ //add an else if for group before this else
                 
                 st+=" "+thingToPrint.get(x).getToken();
             }
         }
         

        String replace = st.replace("(", "");
        String replace2 = replace.replace(")", "");
       
        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        result = engine.eval(replace2);
        System.out.println(result+"HELLOOOOOO"+IFstack.peek().getLevel());
        
    }
}

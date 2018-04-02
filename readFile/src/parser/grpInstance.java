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
import static readfile.ReadFile.IFstack;
import static readfile.ReadFile.bigBoard;
import static readfile.ReadFile.groupInstances;
import readfile.tokenizer.Token;
import readfile.tokenizer.TokenType;

/**
 *
 * @author User
 */
public class grpInstance {
    private static String data_type;
    private static List<member> members;
    private static String identifier;
    
    public grpInstance(String data_type,List<member> members,String identifier){
        this.data_type = data_type;
        this.members = members;
        this.identifier = identifier;
    }
    
    public String getDataType(){
        return this.data_type;
    }
    
    public List<member> getMembers(){
          return this.members;
    }
    
    public String getIdentifier(){
       return this.identifier;
    }
    
     public static boolean isInstanceDefined(String instance){
         boolean retval = false;
         
         for(int ctr=0;ctr<groupInstances.size();ctr++){
             if(groupInstances.get(ctr).getIdentifier().equals(instance)){
                retval = true;
                break;
             }
         }
         return retval;
     }
     
     public static void assignMember(ArrayList<Token> code) throws ScriptException{
         
          String groupInstanceIdentifier = code.get(2).getToken();
          String member = code.get(0).getToken();
          List<Token> expression = code.subList(4,code.size());
          String st = "";
          Object result = null;
           for(Token token:expression){
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
         
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            result = engine.eval(st);
            ////println(result);
            
            List<member> temp = (ArrayList<member>) bigBoard.get(IFstack.peek().getLevel(),groupInstanceIdentifier);
            
//            for(int ctr=0; ctr<temp.size();ctr++){
//              //println("HELLO"+temp.get(ctr).getMemberName());
//            }
            
            for(int ctr=0;ctr<temp.size();ctr++){
               if(temp.get(ctr).getMemberName().equals(member)){
                   temp.get(ctr).setValue(result);
                   break;
               }
            }
            
            
            
            bigBoard.put(IFstack.peek().getLevel(),groupInstanceIdentifier, new memory(temp,TokenType.RECORD));
            
     }
}

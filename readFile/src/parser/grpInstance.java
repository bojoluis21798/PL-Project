/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.List;
import static readfile.ReadFile.groupInstances;
import readfile.tokenizer.Token;

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
     
     public static void assignMember(ArrayList<Token> code){
          String groupInstanceIdentifier = code.get(2).getToken();
          String member = code.get(0).getToken();
          Object value = code.get(4);
          int ctr;
          for(ctr=0;ctr<groupInstances.size();ctr++){
              if(groupInstances.get(ctr).getIdentifier().equals(groupInstanceIdentifier )){
                 break;
              }
          }
          
          int ctr2;
          for(ctr2 = 0;ctr2 < groupInstances.get(ctr).getMembers().size();ctr2++){
              if(groupInstances.get(ctr).getMembers().get(ctr2).getMemberName().equals(member)){
                  System.out.println("FUCK YOU WTIH FEELINGS");
                 break;
              }
          }
          System.out.println(ctr);
          
          groupInstances.get(ctr).getMembers().get(ctr2).setValue(value);
          
     }
}

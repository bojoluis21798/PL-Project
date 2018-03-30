/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static readfile.ReadFile.groupDefinitions;

/**
 *
 * @author User
 */
public class groups {
    private static List<member> groupMemory = new ArrayList<>();
    private static String groupName;
    
    public groups(ArrayList groupMemory,String groupName){
       this.groupMemory = (List<member>) groupMemory.clone();
       this.groupName = groupName;
    }
    
    public String getGrpName(){
        return this.groupName;
    }
    
    public List<member> getGrpMemory(){
        return new ArrayList<>(groupMemory);
    }
    
    public static boolean isDefined(String dType){
         
          boolean retval=false;
          for(int ctr=0;ctr<groupDefinitions.size();ctr++){
             if(groupDefinitions.get(ctr).getGrpName().equals(dType)){
                
                retval = true;
                break;
             }
          }
          return retval;
    }
    
    public static List<member> allocateMemory(String dataType){
          List<member> ret = null;
          for(int ctr=0;ctr<groupDefinitions.size();ctr++){
              if(groupDefinitions.get(ctr).getGrpName().equals(dataType)){
                 ret = groupDefinitions.get(ctr).getGrpMemory();
                 break;
              }
          }
         
          return new ArrayList<>(ret);
    }
    
   
}

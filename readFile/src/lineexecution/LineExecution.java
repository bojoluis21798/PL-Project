package lineexecution;

import parser.Iffer;
import javax.script.ScriptException;
import java.util.Hashtable;
import java.util.ArrayList;
import readfile.tokenizer.Token;
import static readfile.ReadFile.program;
import java.util.List;
import parser.groups;
import parser.member;
import static readfile.ReadFile.groupDefinitions;
import static readfile.ReadFile.levelsAndLines;
import static readfile.ReadFile.loopTracker;
import readfile.tokenizer.TokenType;
import readfile.tuple;
import java.util.Collections;

//unused imports
import readfile.tokenizer.TokenData;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import static readfile.ReadFile.IFctr;
import static readfile.ReadFile.IFstack;
import static readfile.ReadFile.bigBoard;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import parser.InitAssign;
import parser.selection;



public class LineExecution {

    ArrayList<Token> tkStream;
    public static Hashtable<Object, Object> varList= new Hashtable<Object, Object>();


    public LineExecution(ArrayList<Token> tkStream) throws ScriptException {

        this.tkStream = tkStream;
        Start();

    }

    public void Start() throws ScriptException{
          int thisLevel=0;
          for(int lineCount=0;lineCount<program.size();){//loop through the pre-processed lines of code

            if(program.get(lineCount).getCode().get(0).getToken().equals("if") ){//when you hit an if,else
                    int i=0;
                    
                     thisLevel = levelsAndLines.get(0).getLevel();
                    
                     levelsAndLines.remove(0);
                    if (Iffer.ifSTMT((ArrayList<Token>) program.get(lineCount).getCode())){//check if the condition is true
                          //dequeue from the list so u can check up to which statement you will have to execute that isn't another selection statement
//                          
//                          IFstack.add(new selection(true,IFctr));
//                          IFctr++;
                         for(i = program.get(lineCount).getIndex();i < levelsAndLines.get(0).getLine();i++){//execute these lines of code  when condition is true
                             Iffer.execute((ArrayList<Token>) program.get(i).getCode());
                         }

                         lineCount = i;
                         
                         //insert code here to skip  orif and go to end when 
                         if(program.get(lineCount).getCode().get(0).getToken().equals("orif") && thisLevel == levelsAndLines.get(0).getLevel()){
                             lineCount++;
                             levelsAndLines.remove(0);
                             
                             for(; thisLevel == levelsAndLines.get(0).getLevel();){
                               
                                if(!levelsAndLines.isEmpty()){
                                    lineCount = levelsAndLines.get(0).getLine();
                                
                                    if("end".equals(program.get(lineCount).getCode().get(0).getToken()) ){
                                        IFstack.pop();
//                                        IFctr--;
                                        break;
                                    }
                                    levelsAndLines.remove(0);
                                }
                                
                             }
                             
                         }else if(program.get(lineCount).getCode().get(0).getToken().equals("else") && thisLevel == levelsAndLines.get(0).getLevel()){
                             for(; thisLevel == levelsAndLines.get(0).getLevel();){
                                
                                levelsAndLines.remove(0);
                                lineCount = levelsAndLines.get(0).getLine();
                                if("end".equals(program.get(lineCount).getCode().get(0).getToken()) ){
//                                    IFctr--;
                                    IFstack.pop();
                                    break;
                                }
                             }
                             
                         }
                         
                     }else{//if the if condition is false
                         
                         ////////////////////////////////// this block of code is used when if condition is false and so now u check orif 
                          int ctr;
                         
                          for(ctr=0;levelsAndLines.get(ctr).getLevel() != thisLevel ;){
                                levelsAndLines.remove(ctr);
                          }
                          
                          lineCount = levelsAndLines.get(ctr).getLine();
                          
                     }
            }else if(program.get(lineCount).getCode().get(0).getToken().equals("orif") || program.get(lineCount).getCode().get(0).getToken().equals("else") ){//hit an orif and else
               
                if(program.get(lineCount).getCode().get(0).getToken().equals("orif") ){//if it hits orif
                  
                      
                    if (Iffer.ifSTMT((ArrayList<Token>) program.get(lineCount).getCode()) && thisLevel == levelsAndLines.get(0).getLevel()){//check if the orif condition is true
                         levelsAndLines.remove(0); //dequeue from the list so u can check up to which statement you will have to execute that isn't another selection statement
                         
                         int i=0;
                         for(i = program.get(lineCount).getIndex();i < levelsAndLines.get(0).getLine();i++){//execute these lines of code  when condition is true
                             Iffer.execute((ArrayList<Token>) program.get(i).getCode());
                         }

                         lineCount = i;
                         
                        if(program.get(lineCount).getCode().get(0).getToken().equals("orif")){
                             lineCount++;
                             levelsAndLines.remove(0);
                             
                             for(; thisLevel == levelsAndLines.get(0).getLevel();){
                                
                                if(!levelsAndLines.isEmpty()){
                                    lineCount = levelsAndLines.get(0).getLine();
                                
                                    if("end".equals(program.get(lineCount).getCode().get(0).getToken()) ){
                                        IFstack.pop();
//                                        IFctr--;
                                        break;
                                    }
                                    levelsAndLines.remove(0);
                                }
                                
                             }
//                             IFstack.pop();
//                             IFctr--;
                         }else if(program.get(lineCount).getCode().get(0).getToken().equals("else") ){
                             for(; thisLevel == levelsAndLines.get(0).getLevel();){
                               
                                levelsAndLines.remove(0);
                                lineCount = levelsAndLines.get(0).getLine();
                                if("end".equals(program.get(lineCount).getCode().get(0).getToken()) ){
                                    IFstack.pop();
//                                        IFctr--;
                                    break;
                                }
                             }
//                             IFstack.pop();
//                             IFctr--;
                         }
                        
                         

                     }else if( (Iffer.ifSTMT((ArrayList<Token>) program.get(lineCount).getCode()) && thisLevel != levelsAndLines.get(0).getLevel())|| (!Iffer.ifSTMT((ArrayList<Token>) program.get(lineCount).getCode()) && thisLevel != levelsAndLines.get(0).getLevel())){//jump to end
                         thisLevel = levelsAndLines.get(0).getLevel(); 
                         for(; thisLevel == levelsAndLines.get(0).getLevel() && !levelsAndLines.isEmpty();){
                                
                                levelsAndLines.remove(0);
                                lineCount = levelsAndLines.get(0).getLine();
                                if("end".equals(program.get(lineCount).getCode().get(0).getToken()) ){
                                    IFstack.pop();
//                                        IFctr--;
                                    break;
                                }
                             }
                     }else{
                        
                          thisLevel = levelsAndLines.get(0).getLevel();
                          levelsAndLines.remove(0);
                           
                          int ctr;
                   
                          for(ctr=0;levelsAndLines.get(ctr).getLevel() != thisLevel ;){
                              
                                levelsAndLines.remove(ctr);
                          }
                          
                          lineCount = levelsAndLines.get(ctr).getLine();
                          
                     }
                }else if(program.get(lineCount).getCode().get(0).getToken().equals("else") /*&& thisLevel == levelsAndLines.get(0).getLevel()*/){//execute 
                    
                     int i=0;
                     lineCount++;
                     levelsAndLines.remove(0); //dequeue from the list so u can check up to which statement you will have to execute that isn't another selection statement

//                         if(program.get(lineCount).getCode().get(0).getToken().equals("end") && thisLevel == levelsAndLines.get(0).getLevel()){
//                             
//                         }
                         for(i = program.get(lineCount).getIndex();i < levelsAndLines.get(0).getLine();i++){//execute these lines of code  when condition is true
                             Iffer.execute((ArrayList<Token>) program.get(i).getCode());
                         }

                         lineCount = i;


                }
            }else if (program.get(lineCount).getCode().get(0).getToken().equals("repeat")) {
                
                int i = 0;
                thisLevel = loopTracker.get(0).getLevel();
                loopTracker.remove(0);
                
                ArrayList<Token> oldLine = (ArrayList<Token>) program.get(lineCount).getCode();
                if (Iffer.ifSTMT(oldLine)){//check if the condition is true
                    //dequeue from the list so u can check up to which statement you will have to execute that isn't another selection statement
                    
                    System.out.println("code after");
                    for (Token t: (ArrayList<Token>) program.get(lineCount).getCode()) {
                        System.out.println(t.getToken());
                    }
                    while (Iffer.checkConditionLoops((ArrayList<Token>) program.get(lineCount).getCode())) {
                        for(i = program.get(lineCount).getIndex()+1;i < loopTracker.get(0).getLine(); i++){//execute these lines of code  when condition is true
                            System.out.println(i);
                            Iffer.execute((ArrayList<Token>) program.get(i).getCode());
                        } 
                        System.out.println("LOL");
                    }
                    
                    
                }else{//if the if condition is false
                    lineCount++;
                }
                
            }else if (program.get(lineCount).getCode().get(0).getToken().equals("do")){
                int i = 0;
                
                for(i = program.get(lineCount).getIndex()+1;i < loopTracker.get(0).getLine(); i++){//execute these lines of code  when condition is true
                    System.out.println(i);
                    Iffer.execute((ArrayList<Token>) program.get(i).getCode());
                }
            
            }else if(program.get(lineCount).getCode().get(0).getToken().equals("end")){
                   
                boolean isIf = false;
                boolean isLoop = false;
                for (int x = lineCount; x >= 0 && isIf == false && isLoop == false; x--) {
                    if (program.get(x).getCode().get(0).getToken().equals("repeat")) {
                        
                        loopTracker.remove(0);
                        isLoop = true;
                        
                    } else if (program.get(x).getCode().get(0).getToken().equals("if")
                            || program.get(x).getCode().get(0).getToken().equals("orif")
                            || program.get(x).getCode().get(0).getToken().equals("else")) {
                        
                        levelsAndLines.remove(0);
                        isIf = true;
                        
                    }
                }
                
                lineCount++;
                
                if (isIf) {
                    int i=0;
                    if(levelsAndLines.isEmpty()){
                        //lineCount++;
                        //System.out.println("WTF");
                        //IFstack.pop();
                        //IFctr--;a
                    }else{
                          if(program.get(lineCount).getCode().get(0).getToken().equals("if") && thisLevel == levelsAndLines.get(0).getLevel()){
                              lineCount++;
                              //insert code here for when end encounters an if on the same level
                              lineCount = levelsAndLines.get(0).getLine();
                          }else if(program.get(lineCount).getCode().get(0).getToken().equals("orif")  && thisLevel != levelsAndLines.get(0).getLevel()){
                              lineCount++;
                              //insert code here for when end encounters an orif on a different level 


                              for(;!"end".equals(program.get(levelsAndLines.get(0).getLine()).getCode().get(0).getToken());){
                                 levelsAndLines.remove(0);
                                 lineCount = levelsAndLines.get(0).getLine();
                             }

                          }else if(program.get(lineCount).getCode().get(0).getToken().equals("else")  && thisLevel != levelsAndLines.get(0).getLevel()){
                              lineCount++;


                              for(;!"end".equals(program.get(levelsAndLines.get(0).getLine()).getCode().get(0).getToken());){
                                 levelsAndLines.remove(0);
                                 lineCount = levelsAndLines.get(0).getLine();
                              }
                          }else{

                               System.out.println("WHAT"+levelsAndLines.get(0).getLevel());
                               for(i = program.get(lineCount).getIndex();i < levelsAndLines.get(0).getLine();i++){//execute these lines of code  when condition is true
                                 Iffer.execute((ArrayList<Token>) program.get(i).getCode());
                                 lineCount++;
                                 System.out.println(lineCount);
                               }
                          }
                    }
                } else if (isLoop) {
                    
                }
                
                  
            }else{//continue as normal
               
               if(program.get(lineCount).getCode().get(0).getToken().equals("group") ){
                   //code here to set the tokenType of the group identifier to data type instead of identifier
                   
                   program.get(lineCount).getCode().get(1).setTokenType(TokenType.DATA_TYPE);
                  
                   String groupIdentifier = program.get(lineCount).getCode().get(1).getToken();
                   List<member> members = new ArrayList<>();
                   lineCount++;
                   for(;!program.get(lineCount).getCode().get(0).getToken().equals("end");lineCount++){
                       
                       if(program.get(lineCount).getCode().get(0).getTokenType().equals(TokenType.DATA_TYPE) && program.get(lineCount).getCode().get(1).getTokenType().equals(TokenType.IDENTIFIER)){
                           members.add(new member(null,program.get(lineCount).getCode().get(0).getToken(),program.get(lineCount).getCode().get(1).getToken()));
                       }else{
                          System.out.println("ERROR: wrong syntax within group definition");
                          
                       }
                   }
                   levelsAndLines.remove(0);
                   lineCount++;
                   groupDefinitions.add(new groups((ArrayList<member>) members,groupIdentifier));
            
               }else if(program.get(lineCount).getCode().get(0).getToken().equals("job")){
                   
               }else{
                  //for(;;)
                  Iffer.execute((ArrayList<Token>) program.get(lineCount).getCode());
                  lineCount++;
               }
               
            }
            
            
           }
    }

}

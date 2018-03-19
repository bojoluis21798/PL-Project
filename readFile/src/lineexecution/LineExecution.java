package lineexecution;

import parser.Iffer;
import javax.script.ScriptException;
import java.util.Hashtable;
import java.util.ArrayList;
import readfile.tokenizer.Token;
import readfile.tokenizer.TokenType;
import static readfile.ReadFile.program;
import static readfile.ReadFile.q;

//Unused Imports TBD
import readfile.tokenizer.TokenData;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import static readfile.ReadFile.IFctr;
import static readfile.ReadFile.IFstack;
import static readfile.ReadFile.bigBoard;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import parser.InitAssign;


public class LineExecution {

    ArrayList<Token> tkStream;
    public static Hashtable<Object, Object> varList= new Hashtable<Object, Object>();


    public LineExecution(ArrayList<Token> tkStream) throws ScriptException {

        this.tkStream = tkStream;
        Start();

    }

    public void Start() throws ScriptException{

        for(int lineCount=0;lineCount<program.size();){//loop through the pre-processed lines of code


            if(program.get(lineCount).getCode().get(0).getToken().equals("if") ){//when you hit an if,or,else

                if(q.getFirst() == program.get(lineCount).getIndex()){//when a selection statement
                    int i=0;
                    if (Iffer.ifSTMT((ArrayList<Token>) program.get(lineCount).getCode())){
                        q.pollFirst(); //dequeue from the list so u can check up to which statement you will have to execute that isn't another selection statement


                        for(i = program.get(lineCount).getIndex();i < q.getFirst();i++){//execute these lines of code if when condition is true
                            Iffer.execute((ArrayList<Token>) program.get(i).getCode());
                        }

                        lineCount = i;


                    }else{
                        int ctr = 0;
                        for(ctr = lineCount;;ctr++){
                            //System.out.println(ctr);
                            if(program.get(ctr).getCode().get(0).getToken().equals("end")){
                                break;
                            }
                        }//after executing code above search for the end statement

                        for(;q.getFirst() < ctr;q.pollFirst()){}//dequeuing from the if statement que so that the top is end
                        q.pollFirst(); //dequeueing the end statement
                        lineCount = ctr+1;
                    }

                    System.out.println("RECUUUU");
                }else{//when condition is false

                    System.out.println(q.pollFirst());//dequeue from the if queue

                    lineCount = q.getFirst();

                }
            }else if(program.get(lineCount).getCode().get(0).getToken().equals("orif") || program.get(lineCount).getCode().get(0).getToken().equals("else")){
                int ctr=0;
                for( ctr = lineCount;;ctr++){
                    //System.out.println(ctr);
                    if(program.get(ctr).getCode().get(0).getToken().equals("end")){
                        break;
                    }
                }//after executing code above search for the end statement
                lineCount = ctr++;
            }else{//continue as normal
                Iffer.execute((ArrayList<Token>) program.get(lineCount).getCode());
                lineCount++;

            }

        }
    }

}

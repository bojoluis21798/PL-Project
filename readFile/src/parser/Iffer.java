package parser;

import tokenizer.Token;
import tokenizer.TokenType;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

import static readFile.readFile.IFctr;
import static readFile.readFile.IFstack;
import static readFile.readFile.bigBoard;

public class Iffer {
    public static boolean ifSTMT(ArrayList<Token> code) throws ScriptException {

        //System.out.println("tkStream.get(0).getToken() : "+tkStream.get(0).getToken());

        boolean retval = false;
        //EdCode if

        if(code.get(0).getTokenType() == TokenType.KEYWORD && code.get(0).getToken().equals("if")){

            if(checkCondition(code)){
                IFctr++;
                IFstack.push(new selection(true,IFctr));
                retval = true;
            }else{
                //IFstack.push(new selection(false,IFctr));
                retval = false;
            }


            //Edcode orif
        }else if(/*code.get(0).getTokenType() == TokenType.KEYWORD &&*/ code.get(0).getToken().equals("orif")){


            //IFstack.pop();
            //IFctr--;
            if(checkCondition(code)){

                IFstack.push(new selection(true,IFctr));
                retval = true;
            }else{
                IFstack.push(new selection(true,IFctr));
                retval = false;
            }


            //EdCode else
        }else if(code.get(0).getToken().equals("else")){//do else statement

            retval = true;

            //EdCode end
        }



        return retval;
    }

    public static boolean checkCondition(ArrayList<Token> code) throws ScriptException{
        String st = "";
        Object result = null;
        boolean retval = false;
        List<Token> boolE = code.subList(1, code.size());
        for(Token tok:boolE){
            if(tok.getToken().equals("then")){
                List<Token> boolE2 = boolE.subList(0, boolE.size()-1);

                int ctr = 0;
                for(Token token:boolE2){
                    if (token.getTokenType().equals(TokenType.IDENTIFIER)){
                        String variable = token.getToken();
                        if (InitAssign.isInitialized(variable) && InitAssign.isAccessible(variable)){
                            int levelOfVariable = InitAssign.accessLevelOf(variable);
                            String value = bigBoard.get(levelOfVariable,variable).toString();
                            st+=" "+value;
                        }else{
                            System.out.println("Error: Variable not in HashMap");
                        }
                    }else{
                        st+=" "+token.getToken();
                    }
                    ctr++;
                }
            }
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        result = engine.eval(st);
        System.out.println(result);
        if(result.equals(true)){


            retval =  true;
        }

        return retval;
    }

    public boolean checkStack(){
        boolean retval = true;
        for(int x=0;x<IFstack.size();x++){
            if(IFstack.get(x).getBool() == false){
                retval = false;
                break;
            }
        }
        return retval;
    }
}

package parser;

import readfile.tokenizer.Token;
import readfile.tokenizer.TokenType;
import readfile.tokenizer.Tokenizer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

import static readfile.ReadFile.IFctr;
import static readfile.ReadFile.IFstack;
import static readfile.ReadFile.bigBoard;

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
        List<Token> boolE = code.subList(2, code.size());
        for(Token tok:boolE){
            if(tok.getToken().equals(")")){
                List<Token> boolE2 = boolE.subList(0, boolE.size()-2);

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

    public static Token checkExpression(ArrayList<Token> code) throws ScriptException{
        String st = "";
        Object result;
        List<Token> arithmeticExpression;
        if(code.get(0).getTokenType().equals(TokenType.DATA_TYPE)){

            arithmeticExpression = code.subList(3, code.size());

        }else{

            arithmeticExpression = code.subList(2, code.size());

        }
        for(Token token:arithmeticExpression){
            if (token.getTokenType().equals(TokenType.IDENTIFIER)){
                String variable = token.getToken();
                if (InitAssign.isInitialized(variable) && InitAssign.isAccessible(variable)){
                    int levelOfVariable = InitAssign.accessLevelOf(variable);
                    String value;
                    if(bigBoard.getTokenType(levelOfVariable,variable) == TokenType.STRING_LITERAL){
                        value = "\""+bigBoard.get(levelOfVariable,variable).toString()+"\"";
                    }else{
                        value = bigBoard.get(levelOfVariable,variable).toString();
                    }
                    st+=" "+value;
                }else{
                    System.out.println("Error: Variable "+variable+" not in HashMap");
                }
            }else if(token.getTokenType().equals(TokenType.STRING_LITERAL)){
                st+=" "+"\""+token.getToken()+"\"";
            }else{
                st+=" "+token.getToken();
            }
        }

        int origCodeSize = code.size();
        for(int i=0; i < origCodeSize; i++){
            if (code.size() != 3 && code.get(0).getTokenType().equals(TokenType.DATA_TYPE)){
                code.remove(3);
            }else if(code.size() != 2 && code.get(0).getTokenType().equals(TokenType.IDENTIFIER)){
                code.remove(2);
            }
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        System.out.println(st);
        result = engine.eval(st);

        try {
            result = engine.eval(st);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        if(result.equals(true) || result.equals(false) || result instanceof Number){
            //proceed to tokenizing
        }else{
            result = "\""+result+"\"";
        }
        Tokenizer tknObj = new Tokenizer(result.toString());
        Token literal = tknObj.nextToken();
        System.out.println(literal.getToken());
        System.out.println(literal.getTokenType());
        return literal;
    }

    public static void execute(ArrayList<Token> code){


        if(code.get(0).getTokenType() == TokenType.DATA_TYPE || code.get(0).getTokenType() == TokenType.IDENTIFIER){
            if(code.size() == 4){//NORMAL INITIALIZATION

                InitAssign.initialize(code);
                //System.out.println("initialization");

            }else if(code.size() == 3){//NORMAL ASSIGNMENT

                InitAssign.assign(code);
                //System.out.println("declaration");

            }else if(code.size() == 2){//NULL INITIALIZATION

                InitAssign.initialize(code);
                //System.out.println("null init");

            }else if(code.size() > 4){//INITIALIZATION AND ASSIGNMENT WITH EXPRESSION
                int x = 0;
                while(x < code.size() && !code.get(x).getToken().equals("(")){ x++; }
                if(x==code.size()){
                    Token literal = null;
                    try {
                        System.out.println("number of tokens");
                        for(int i=0; i < code.size();i++){
                            System.out.print(code.get(i).getToken() + " ");
                        }
                        System.out.println();
                        for(int i=0; i < code.size();i++){
                            System.out.print(code.get(i).getTokenType() + " ");
                        }
                        System.out.println();
                        literal = checkExpression(code);
                    } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                    code.add(literal);
                    if(code.get(0).getTokenType().equals(TokenType.DATA_TYPE)){
                        InitAssign.initialize(code);
                    }else{
//                    if (code.size() == 3) {
//                        System.out.println(code.get(0).getToken() + " " + code.get(1).getToken() + " " + code.get(2).getToken());
//                    }else {
//                        System.out.println("Unexpected number of tokens");
//                        for(int i=0; i < code.size();i++){
//                            System.out.print(code.get(i).getToken() + " ");
//                        }
//                        System.out.println();
//                    }
                        InitAssign.assign(code);
                    }
                }else{
                    System.out.println("Vector init with multiple values");
                    for(int i=0; i < code.size();i++){
                        System.out.print(code.get(i).getToken() + " ");
                    }
                    System.out.println();
                    for(int i=0; i < code.size();i++){
                        System.out.print(code.get(i).getTokenType() + " ");
                    }
                    System.out.println();
                    InitAssign.initialize(code);
                }

                //System.out.println("expression init");
            }

        }else{
            System.out.println("Not a Declaration/Initialization");
        }


    }
}

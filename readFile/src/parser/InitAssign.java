package parser;

import tokenizer.Token;
import tokenizer.TokenType;
import tokenizer.Tokenizer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

import static readFile.readFile.IFctr;
import static readFile.readFile.IFstack;
import static readFile.readFile.bigBoard;

public class InitAssign {
    //INITIALIZATION
	/*
    	initialize ()
    	this function will be called to initialize a variable
    	using the functions: initPlaceIntoMemory() and initWithVariable()

    	This function throws and error if:
    		-there is a datatype mismatch
			-it is not an initialization
			-there is no variable identifier
			-there is no such datatype
    */
    public static void initialize(ArrayList<Token> code){

        switch (code.get(0).getToken()) {

            case "number":
            case "word":
            case "truth":
                if(code.get(1).getTokenType() == TokenType.IDENTIFIER){

                    if(code.get(2).getTokenType() == TokenType.KEYWORD && code.get(2).getToken().equals("is")){

                        String alert;
                        switch (code.get(3).getTokenType()){
                            case NUMBER_LITERAL:
                                alert = "number";
                                if(code.get(0).getToken().equals(alert)){
                                    initPlaceIntoMemory(alert,code);
                                }else{
                                    System.out.println(code.get(0).getToken());
                                    System.out.println("Error: Data Type Mismatch (Number)");
                                }
                                break;
                            case STRING_LITERAL:
                                alert = "word";
                                if(code.get(0).getToken().equals(alert)){
                                    initPlaceIntoMemory(alert,code);
                                }else{
                                    System.out.println("Error: Data Type Mismatch (Word)");
                                }
                                break;
                            case BOOLEAN_LITERAL:
                                alert = "truth";
                                if(code.get(0).getToken().equals(alert)){
                                    initPlaceIntoMemory(alert,code);
                                }else{
                                    System.out.println("Error: Data Type Mismatch (Truth)");
                                }
                                break;
                            case IDENTIFIER:
                                initWithVariable(code);
                                break;
                            default:
                                System.out.println("Error: No such Data Type");

                        }

                    }else{
                        System.out.println("Invalid Syntax: not an initialization");
                    }

                }else{
                    System.out.println("Invalid Syntax: no variable name");
                }
                break;

            case "numbers":
            case "words":
            case "truths":
                if(code.get(1).getTokenType() == TokenType.IDENTIFIER){

                    if(code.get(2).getTokenType() == TokenType.KEYWORD && code.get(2).getToken().equals("is")){

                        if(code.get(3).getToken().equals("(")){
                            int lineSize = code.size();
                            for (int i = 3; i < lineSize && !code.get(i).getToken().equals(")"); i++){
                                switch(code.get(i).getToken()){
                                    case "(":
                                }
                            }
                        }


                    }else{
                        System.out.println("Invalid Syntax: not a vector initialization");
                    }

                }else{
                    System.out.println("Invalid Syntax: no variable name");
                }

            default:
                System.out.println("Invalid Syntax: No such Data Type INIT");

        }

    }



    /*
    	initWithVariable ()
    	this function will  be called to initialize a variable
    	using an existing variable in the hashmap:

    	number x is y

    	where y is already in the hashmap

    	This function throws and error if:
    		-the variable to be used as a value for the new variable is not in the hashmap
			-the variable to be used as a value is not accessible by that specific code block
			-the tokenTypes of the variables do not match
    */
    public static void initWithVariable (ArrayList<Token> code){
        String variable = code.get(3).getToken();
        int levelOfVariable = accessLevelOf(variable);
        if(isInitialized(variable) && isAccessible(variable) && bigBoard.getTokenType(levelOfVariable,variable) == TokenType.NUMBER_LITERAL){
            bigBoard.put(levelOfVariable,code.get(1).getToken(),new memory(bigBoard.get(levelOfVariable,variable), (TokenType) bigBoard.getTokenType(levelOfVariable,variable)));
        }else{
            if (isInitialized(variable) == false)
                System.out.println("Error: variable "+variable+" is not Initialized");
            else if (isAccessible(variable) == false)
                System.out.println("Error: variable "+variable+" is not Accessible in this Level");
            else if (bigBoard.getTokenType(levelOfVariable,variable) != TokenType.NUMBER_LITERAL)
                System.out.println("Error: Data Type Mismatch. Not a Number");
        }
    }



    /*
    	initPlaceIntoMemory ()
    	this function will  be called to initialize a variable
    	using a literal and also alerts the programmer what datatype
    	is used in the initialization:

    	number x is 5

		NOTE: the code should have a value at
		code.get(3).getToken(),code.get(3).getTokenType()

    */
    public static void initPlaceIntoMemory (String alert, ArrayList<Token> code){
        System.out.println("This is a "+alert+" initialization");
        bigBoard.put(IFstack.peek().getLevel(),code.get(1).getToken(),new memory(code.get(3).getToken(),code.get(3).getTokenType()));
    }



    //ASSIGNMENT / DECLARATION
    /*
    	assign ()
    	this function will  be called to assign a variable
    	using a literal and also alerts the programmer what datatype
    	is used in the initialization:

    	x is 5
    	x is y

		This function throws and error if:
			-the variable to be reassigned has yet to be initialized
			-the variable to be reassigned cannot be accessed by the specific code block
    		-the variable to be used as a value for the new variable is not in the hashmap
			-the variable to be used as a value is not accessible by that specific code block
			-the tokenTypes of the variables do not match

    */
    public static void assign(ArrayList<Token> code){
        if(code.get(0).getTokenType() == TokenType.IDENTIFIER) {

            if (code.get(1).getTokenType() == TokenType.KEYWORD
                    && code.get(1).getToken().equals("is")
                        && code.get(2).getTokenType() != TokenType.IDENTIFIER) { //FOR LITERALS

                switch (code.get(2).getTokenType()) {
                    case NUMBER_LITERAL:
                    case STRING_LITERAL:
                    case BOOLEAN_LITERAL:
                        String variable = code.get(0).getToken();
                        int levelOfVariable = accessLevelOf(code.get(0).getToken());

                        if(isInitialized(variable) && isAccessible(variable)){
                            System.out.println("I am relevant");
                            bigBoard.put(levelOfVariable,code.get(0).getToken(),new memory(code.get(2).getToken(),code.get(2).getTokenType()));

                        }else{
                            if (isInitialized(variable) == false) {
                                System.out.println("Error: variable " + variable + " is not Initialized");
                            }else if (isAccessible(variable) == false) {
                                System.out.println("Error: variable " + variable + " is not Accessible in this Level");
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid Syntax: No such data type DECL");
                }

            }else if (code.get(1).getTokenType() == TokenType.KEYWORD
                        && code.get(1).getToken().equals("is")
                            && code.get(2).getTokenType() == TokenType.IDENTIFIER) { //FOR VARIABLES

                String variable = code.get(2).getToken();
                int levelOfVariable = accessLevelOf(variable);

                if (isInitialized(variable) && isAccessible(variable)){

                    bigBoard.put(IFstack.peek().getLevel(),code.get(0).getToken(),new memory(bigBoard.get(levelOfVariable,variable), (TokenType) bigBoard.getTokenType(levelOfVariable,variable)));

                }else{

                    if (isInitialized(variable) == false)
                        System.out.println("Error: variable "+variable+" is not Initialized");
                    else if (isAccessible(variable) == false)
                        System.out.println("Error: variable "+variable+" is not Accessible in this Level");
                    else if (bigBoard.getTokenType(levelOfVariable,variable) != TokenType.NUMBER_LITERAL)
                        System.out.println("Error: Data Type Mismatch. Not a Number");

                }

            }
            //else if(){} for vectors TBD
            else{
                System.out.println("Invalid Syntax: not an assignment");
            }

        }else{
            System.out.println("Invalid Syntax: no variable name");
        }

    }

    public static boolean isInitialized(String token){
        boolean val=false;
        for (int i=IFctr; i>=0; i--){
            if(bigBoard.containsKeys(i, token)){
                val = true;
            }else{
                val = false;
            }
        }
        //System.out.println("isInitialized() is : "+val);
        return val;
    }

    public static boolean isAccessible(String token){
        boolean val = false;
        int ctr=IFstack.peek().getLevel();
        //System.out.println("ctr: "+ctr);

        for(; ctr > 0 && IFstack.get(ctr) instanceof selection;ctr--){

            if( bigBoard.containsKeys(ctr, token) ){
                val=true;
                break;
            }

        }
        if( bigBoard.containsKeys(ctr, token) ){ val = true; }
        // System.out.println("isAccessible() is : "+val);
        // System.out.println("Level of variable found : "+ctr);
        return val;
    }

    public static int accessLevelOf(String token){
        boolean val = false;
        int ctr=IFstack.peek().getLevel();
        //System.out.println("ctr: "+ctr);

        for(; ctr > 0 && IFstack.get(ctr) instanceof selection;ctr--){

            if( bigBoard.containsKeys(ctr, token) ){
                val=true;
                break;
            }

        }
        if( bigBoard.containsKeys(ctr, token) ){ val = true; }
        return ctr;
    }

    public boolean checkCondition(ArrayList<Token> code) throws ScriptException{
        String st = "";
        Object result = null;
        boolean retval = false;
        List<Token> boolE = code.subList(1, code.size());
        for(Token tok:boolE){
            if(tok.getToken().equals("then")){
                List<Token> boolE2 = boolE.subList(0, boolE.size()-1);

                for(Token token:boolE2){
                    if(token.getToken().equals(""))
                    st+=" "+token.getToken();
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

    public Token evaluateExpression(String expression){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        Object result = null;
        try {
            result = engine.eval(expression);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        Tokenizer tknObj = new Tokenizer(result.toString());
        Token literal = tknObj.nextToken();
        return literal;
    }

    public Object access(String var){
        int ctr = IFctr;
        Object retval = null;
        while(bigBoard.containsKeys(ctr, var) == false){
            ctr--;
        }
        if(bigBoard.containsKeys(ctr, var) == true){
            retval = bigBoard.get(ctr, var);
        }

        return retval;
    }

}

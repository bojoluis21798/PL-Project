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
import static readfile.tokenizer.TokenType.NUMBER_LITERAL;
import static readfile.tokenizer.TokenType.STRING_LITERAL;

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
                if(code.size() == 2){

                    Token is = new Token("is",TokenType.KEYWORD);
                    Token defaultToken;
                    code.add(is);
                    switch (code.get(0).getToken()) {
                        case "number":
                            defaultToken = new Token("0",TokenType.NUMBER_LITERAL);
                            code.add(defaultToken);
                            break;
                        case "word":
                            defaultToken = new Token("", STRING_LITERAL);
                            code.add(defaultToken);
                            break;
                        case "truth":
                            defaultToken = new Token("false",TokenType.BOOLEAN_LITERAL);
                            code.add(defaultToken);
                            break;
                    }
                    initPlaceIntoMemory(code);

                }else{

                    String alert;
                    switch (code.get(3).getTokenType()){
                        case NUMBER_LITERAL:
                            alert = "number";
                            if(code.get(0).getToken().equals(alert)){
                                initPlaceIntoMemory(code);
                            }else{
                                System.out.println(code.get(0).getToken());
                                System.out.println("Error: Data Type Mismatch (Number)");
                            }
                            break;
                        case STRING_LITERAL:
                            alert = "word";
                            if(code.get(0).getToken().equals(alert)){
                                initPlaceIntoMemory(code);
                            }else{
                                System.out.println("Error: Data Type Mismatch (Word)");
                            }
                            break;
                        case BOOLEAN_LITERAL:
                            alert = "truth";
                            if(code.get(0).getToken().equals(alert)){
                                initPlaceIntoMemory(code);
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

                }

                break;

            case "numbers":
            case "words":
            case "truths":
                    if (code.size() == 2) {
                        Token is = new Token("is", TokenType.KEYWORD);
                        Token defaultToken;
                        code.add(is);
                        switch (code.get(0).getToken()) {
                            case "numbers":
                                code.add(new Token("(", TokenType.TOKEN));
                                defaultToken = new Token("0", TokenType.NUMBER_LITERAL);
                                code.add(defaultToken);
                                code.add(new Token(")", TokenType.TOKEN));
                                break;
                            case "words":
                                code.add(new Token("(", TokenType.TOKEN));
                                defaultToken = new Token("", STRING_LITERAL);
                                code.add(defaultToken);
                                code.add(new Token(")", TokenType.TOKEN));
                                break;
                            case "truths":
                                code.add(new Token("(", TokenType.TOKEN));
                                defaultToken = new Token("false", TokenType.BOOLEAN_LITERAL);
                                code.add(defaultToken);
                                code.add(new Token(")", TokenType.TOKEN));
                                break;
                        }
                        initPlaceIntoMemory(code);
                    } else {
                        String alert;
                        //loop through the values in between the parentheses
                        int x = 0;
                        while(x < code.size() && !code.get(x).getToken().equals("(")){
                            System.out.println(code.get(x).getToken());
                            x++;
                        }
                        System.out.println(x);
                        if(code.get(x).getToken().equals("(")){
                            for(int i = x+1; i < code.size() && !code.get(x).getToken().equals(")"); i+=2){
                                switch (code.get(x).getTokenType()) {
                                    case NUMBER_LITERAL:
                                        alert = "numbers";
                                        if (code.get(0).getToken().equals(alert)) {
                                            System.out.println(code.get(x).getToken()+"fits dataType");
                                        } else {
                                            System.out.println("Error: Data Type Mismatch (Number Value Required)");
                                        }
                                        break;
                                    case STRING_LITERAL:
                                        alert = "words";
                                        if (code.get(0).getToken().equals(alert)) {
                                            System.out.println(code.get(x).getToken()+"fits dataType");
                                        } else {
                                            System.out.println("Error: Data Type Mismatch (Word Value Required)");
                                        }
                                        break;
                                    case BOOLEAN_LITERAL:
                                        alert = "truths";
                                        if (code.get(0).getToken().equals(alert)) {
                                            System.out.println(code.get(x).getToken()+"fits dataType");
                                        } else {
                                            System.out.println("Error: Data Type Mismatch (Truth Value Required)");
                                        }
                                        break;
                                    case IDENTIFIER:
                                        initWithVariable(code);
                                        break;
                                    default:
                                        System.out.println("Error: No such Data Type");
                                }
                            }
                        }else{
                            
                        }


                    }
                    break;
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
        String dataTypeofInit = code.get(0).getToken();
        if(isInitialized(variable) && isAccessible(variable)){
            switch (bigBoard.getTokenType(levelOfVariable,variable)){
                case STRING_LITERAL:
                    if (dataTypeofInit.equals("word")){
                        System.out.println("this is an initialization with a variable");
                        bigBoard.put(levelOfVariable,code.get(1).getToken(),new memory(bigBoard.get(levelOfVariable,variable), (TokenType) bigBoard.getTokenType(levelOfVariable,variable)));
                    }else{
                        System.out.println("Initialization Error: Data Type Mismatch. Not a Word");
                    }
                    break;
                case NUMBER_LITERAL:
                    if (dataTypeofInit.equals("number")){
                        System.out.println("this is an initialization with a variable");
                        bigBoard.put(levelOfVariable,code.get(1).getToken(),new memory(bigBoard.get(levelOfVariable,variable), (TokenType) bigBoard.getTokenType(levelOfVariable,variable)));
                    }else{
                        System.out.println("Initialization Error: Data Type Mismatch. Not a Number");
                    }
                    break;
                case BOOLEAN_LITERAL:
                    if (dataTypeofInit.equals("truth")){
                        System.out.println("this is an initialization with a variable");
                        bigBoard.put(levelOfVariable,code.get(1).getToken(),new memory(bigBoard.get(levelOfVariable,variable), (TokenType) bigBoard.getTokenType(levelOfVariable,variable)));
                    }else{
                        System.out.println("Initialization Error: Data Type Mismatch. Not a Truth");
                    }
                    break;
                default:
                    System.out.println("Error: Uknown Literal");
            }
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
    	numbers x is (0)

		NOTE: the code should have a value at
		code.get(3).getToken(),code.get(3).getTokenType()

    */
    public static void initPlaceIntoMemory (ArrayList<Token> code){
        switch (code.get(0).getToken()) {
            case "numbers":
            case "words":
            case "truths":
                ArrayList<Token> vectorTok = new ArrayList<Token>();
                System.out.println("values:");
                int i=0;
                while(i < code.size() && !code.get(i).getToken().equals("(")){ i++; }
                String values = "";
                List<Token> containerOfValues = code.subList(i,code.size());

                if(code.get(i).getToken().equals("(")){
                    for (int x=i+1; x < code.size() && !code.get(x).getToken().equals(")"); x+=2){
                        values += code.get(x).getToken();
                        if(!code.get(x+1).getToken().equals(")")){
                            values += ",";
                        }
                        System.out.print(code.get(x).getToken() + " " + code.get(x).getTokenType() + " ");
                    }
                    System.out.println();
                }
                if(!isInitialized(code.get(1).getToken())){
                    System.out.println(values);
                    bigBoard.put(IFstack.peek().getLevel(), code.get(1).getToken(), new memory(values, code.get(3).getTokenType()));
                }else{
                    System.out.println(code.get(1).getToken()+" has already been initialized");
                }
                break;
            default:
                if(!isInitialized(code.get(1).getToken())) {
                    bigBoard.put(IFstack.peek().getLevel(), code.get(1).getToken(), new memory(code.get(3).getToken(), code.get(3).getTokenType()));
                }else{
                    System.out.println(code.get(1).getToken()+" has already been initialized");
                }
        }
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

                        if(isInitialized(variable) && isAccessible(variable)
                                && bigBoard.getTokenType(levelOfVariable,variable) == code.get(2).getTokenType()){

                            bigBoard.put(levelOfVariable,code.get(0).getToken(),new memory(code.get(2).getToken(),code.get(2).getTokenType()));

                        }else{
                            if (isInitialized(variable) == false) {
                                System.out.println("Assignment Error: variable " + variable + " is not Initialized");
                            }else if (isAccessible(variable) == false) {
                                System.out.println("Assignment Error: variable " + variable + " is not Accessible in this Level");
                            }else if (bigBoard.getTokenType(levelOfVariable,variable) != code.get(2).getTokenType())
                                System.out.println("Assignment Error: Data Type Mismatch. Not a "+bigBoard.getTokenType(levelOfVariable,variable));
                        }
                        break;
                    default:
                        System.out.println("Invalid Syntax: No such data type DECL");
                }

            }else if (code.get(1).getTokenType() == TokenType.KEYWORD
                        && code.get(1).getToken().equals("is")
                            && code.get(2).getTokenType() == TokenType.IDENTIFIER) { //FOR VARIABLES

                String assignee = code.get(0).getToken();
                int levelOfAssignee = accessLevelOf(assignee);

                String variable = code.get(2).getToken();
                int levelOfVariable = accessLevelOf(variable);

                if (isInitialized(variable) && isAccessible(variable)
                        && bigBoard.getTokenType(levelOfAssignee,assignee) == bigBoard.getTokenType(levelOfVariable,variable)){ //DataType Matching to be placed here

                    bigBoard.put(IFstack.peek().getLevel(),code.get(0).getToken(),new memory(bigBoard.get(levelOfVariable,variable), (TokenType) bigBoard.getTokenType(levelOfVariable,variable)));

                }else{

                    if (isInitialized(variable) == false)
                        System.out.println("Assignment Error: variable "+variable+" is not Initialized");
                    else if (isAccessible(variable) == false)
                        System.out.println("Assignment Error: variable "+variable+" is not Accessible in this Level");
                    else if (bigBoard.getTokenType(levelOfAssignee,assignee) != bigBoard.getTokenType(levelOfVariable,variable))
                        System.out.println("Assignment Error: Data Type Mismatch. Not a "+bigBoard.getTokenType(levelOfAssignee,assignee));

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
        Token literal = null;
        try {
            literal = tknObj.nextToken();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
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

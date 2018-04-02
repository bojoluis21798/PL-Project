package parser;

import readfile.tokenizer.Token;
import readfile.tokenizer.TokenType;
import readfile.tokenizer.Tokenizer;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import static parser.groups.accessGroup;
import static readfile.ReadFile.IFctr;
import static readfile.ReadFile.IFstack;
import static readfile.ReadFile.bigBoard;

//unused imports
import static readfile.ReadFile.groupDefinitions;
import static readfile.ReadFile.groupInstances;
import static parser.groups.allocateMemory;
import static parser.grpInstance.assignMember;
import static parser.grpInstance.isInstanceDefined;
import static readfile.ReadFile.program;

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
        }else if (code.get(0).getToken().equals("repeat")) {

            if (checkConditionLoops(code)) {

                IFctr++;
                IFstack.push(new selection(true,IFctr));
                retval = true;

            } else {
                retval = false;
            }

        }
        
        return retval;
    }

    public static boolean checkCondition(ArrayList<Token> code) throws ScriptException{
        String st = "";
        Object result;
        boolean retVal = false;
        List<Token> boolE = code.subList(2, code.size());
        List<Token> boolExpression = null;
        for(Token tok:boolE) {
            if (tok.getToken().equals(")")) {
                boolExpression = boolE.subList(0, boolE.size() - 2);
                break;
            }
        }
        int indexOfVector = -1;
        for(int x = 0; x < boolExpression.size(); x++){
            if (boolExpression.get(x).getTokenType().equals(TokenType.IDENTIFIER)){
                String variable = boolExpression.get(x).getToken();
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
                    throw new IllegalStateException("Error: Variable "+variable+" not in HashMap");
                }
            }else if(boolExpression.get(x).getTokenType().equals(TokenType.STRING_LITERAL)){
                st+=" "+"\""+boolExpression.get(x).getToken()+"\"";
            }else if(boolExpression.get(x).getTokenType().equals(TokenType.ORDINAL)){
                indexOfVector = Integer.parseInt(boolExpression.get(x).getToken().substring(0,1))-1;
                String vectorVariable = boolExpression.get(x+2).getToken();
                if(     InitAssign.isInitialized(vectorVariable) &&
                        InitAssign.isAccessible(vectorVariable) &&
                        bigBoard.get(IFstack.peek().getLevel(),vectorVariable) instanceof ArrayList){
                    ArrayList<Token> vector = (ArrayList<Token>)bigBoard.get(IFstack.peek().getLevel(),vectorVariable);
                    if(indexOfVector < vector.size()){
                        st+=" "+vector.get(indexOfVector).getToken();
                        x += 2;
                    }else{
                        throw new IllegalStateException("Error: vector has no "+boolExpression.get(x).getToken()+" ordinal");
                    }
                }else{
                    if(!InitAssign.isInitialized(vectorVariable))
                        throw new IllegalStateException("Error: variable not initialized");
                    if(!InitAssign.isAccessible(vectorVariable))
                        throw new IllegalStateException("Error: variable cannot be accessed in level");
                    if(!(bigBoard.get(IFstack.peek().getLevel(),vectorVariable) instanceof ArrayList)){
                        throw new IllegalStateException("Error: variable is not a vector");
                    }
                }
            }else{
                st+=" "+boolExpression.get(x).getToken();
            }
        }
        
        
        
        for(int i=0; i < boolExpression.size(); i++){
            if (boolExpression.get(i).getTokenType().equals(TokenType.DATA_TYPE)){    //removing identifiers in Primitive initialization
                boolExpression.remove(i);
            }else if(boolExpression.get(i).getTokenType().equals(TokenType.IDENTIFIER)){  //removing identifiers in Primitive Assignment
                boolExpression.remove(i);
            }else if(boolExpression.get(0).getTokenType().equals(TokenType.ORDINAL)){ //removing identifiers in Ordinal Assignment
                boolExpression.remove(i);
            }
        }
        
        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try{
            result = engine.eval(st);
        }catch (ScriptException e){
            throw new IllegalStateException("Error: Wrong Syntax in if condition");
        }
        if(result.getClass().getTypeName().equals("java.lang.Boolean")){
            if(result.equals(true)){
                retVal =  true;
            }
        }else{
            throw new IllegalStateException("Error: Condition does not yield boolean value");
        }
        System.out.println(result);

        return retVal;
    }

    // condition for Loops
    public static boolean checkConditionLoops(ArrayList<Token> code) {
        String st = "";
        Object result;
        boolean retVal = false;
        ArrayList<Token> codeCopy = new ArrayList<Token>(code);
        List<Token> boolE = codeCopy.subList(3, codeCopy.size());
        List<Token> boolExpression = null;
        for(Token tok:boolE) {
            if (tok.getToken().equals(")")) {
                boolExpression = boolE.subList(0, boolE.size() - 1);
                break;
            }
        }
       
        ArrayList<Token> boolEvalList = new ArrayList<Token>(boolExpression);
        int indexOfVector = -1;
        for(int x = 0; x < boolEvalList.size(); x++){
            if (boolEvalList.get(x).getTokenType().equals(TokenType.IDENTIFIER)){
                String variable = boolEvalList.get(x).getToken();
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
                    throw new IllegalStateException("Error: Variable "+variable+" not in HashMap");
                }
            }else if(boolEvalList.get(x).getTokenType().equals(TokenType.STRING_LITERAL)){
                st+=" "+"\""+boolEvalList.get(x).getToken()+"\"";
            }else if(boolEvalList.get(x).getTokenType().equals(TokenType.ORDINAL)){
                indexOfVector = Integer.parseInt(boolEvalList.get(x).getToken().substring(0,1))-1;
                String vectorVariable = boolEvalList.get(x+2).getToken();
                if(     InitAssign.isInitialized(vectorVariable) &&
                        InitAssign.isAccessible(vectorVariable) &&
                        bigBoard.get(IFstack.peek().getLevel(),vectorVariable) instanceof ArrayList){
                    ArrayList<Token> vector = (ArrayList<Token>)bigBoard.get(IFstack.peek().getLevel(),vectorVariable);
                    if(indexOfVector < vector.size()){
                        st+=" "+vector.get(indexOfVector).getToken();
                        x += 2;
                    }else{
                        throw new IllegalStateException("Error: vector has no "+boolExpression.get(x).getToken()+" ordinal");
                    }
                }else{
                    if(!InitAssign.isInitialized(vectorVariable))
                        throw new IllegalStateException("Error: variable not initialized");
                    if(!InitAssign.isAccessible(vectorVariable))
                        throw new IllegalStateException("Error: variable cannot be accessed in level");
                    if(!(bigBoard.get(IFstack.peek().getLevel(),vectorVariable) instanceof ArrayList)){
                        throw new IllegalStateException("Error: variable is not a vector");
                    }
                }
            }else{
                st+=" "+boolEvalList.get(x).getToken();
            }
            
            
        }
        

        for(int i=0; i < boolEvalList.size(); i++){
            if (boolEvalList.get(i).getTokenType().equals(TokenType.DATA_TYPE)){    //removing identifiers in Primitive initialization
                boolEvalList.remove(i);
            }else if(boolEvalList.get(i).getTokenType().equals(TokenType.IDENTIFIER)){  //removing identifiers in Primitive Assignment
                boolEvalList.remove(i);
            }else if(boolEvalList.get(0).getTokenType().equals(TokenType.ORDINAL)){ //removing identifiers in Ordinal Assignment
                boolEvalList.remove(i);
            }
        }
        

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try{
            result = engine.eval(st);
        }catch (ScriptException e){
            throw new IllegalStateException("Error: Wrong Syntax in if condition");
        }
        if(result.getClass().getTypeName().equals("java.lang.Boolean")){
            if(result.equals(true)){
                retVal =  true;
            }
        }else{
            throw new IllegalStateException("Error: Condition does not yield boolean value");
        }
        

        return retVal;
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

        System.out.println("showing you the tokens");
                                for(int i=0; i < code.size();i++){
                                    System.out.print(code.get(i).getToken() + " ");
                                }
                                System.out.println();
                                for(int i=0; i < code.size();i++){
                                    System.out.print(code.get(i).getTokenType() + " ");
                                }
                                System.out.println();
                               
        if(code.get(0).getTokenType().equals(TokenType.DATA_TYPE)){

            arithmeticExpression = code.subList(3, code.size());

        }else if(code.get(0).getTokenType().equals(TokenType.ORDINAL)){

            arithmeticExpression = code.subList(4, code.size());

        }else if(code.get(0).getTokenType().equals(TokenType.KEYWORD) && (code.get(0).getToken().equals("add") || code.get(0).getToken().equals("remove"))){
            int x = 1;

            while(  x < code.size() &&
                    !code.get(x).getToken().equals("to")
                    ){ x++; }
            arithmeticExpression = code.subList(1, x);

        }else{
            
            arithmeticExpression = code.subList(2, code.size());
             
        }
        
        int indexOfVector = -1;
        for(int x=0; x < arithmeticExpression.size(); x++){
            if(arithmeticExpression.get(x).getTokenType().equals(TokenType.IDENTIFIER)){
                   
                String variable = arithmeticExpression.get(x).getToken();
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
                    System.out.println(InitAssign.isInitialized(variable));
                    System.out.println(InitAssign.isAccessible(variable));
                    throw new IllegalStateException("Error: Variable "+variable+" not in HashMap");
                }
                
            }else if(arithmeticExpression.get(x).getTokenType().equals(TokenType.STRING_LITERAL)){
                st+=" "+"\""+arithmeticExpression.get(x).getToken()+"\"";
            }else if(arithmeticExpression.get(x).getTokenType().equals(TokenType.ORDINAL)){
                indexOfVector = Integer.parseInt(arithmeticExpression.get(x).getToken().substring(0,1))-1;
                String vectorVariable = arithmeticExpression.get(x+2).getToken();
                if(     InitAssign.isInitialized(vectorVariable) &&
                        InitAssign.isAccessible(vectorVariable) &&
                        bigBoard.get(IFstack.peek().getLevel(),vectorVariable) instanceof ArrayList){
                    ArrayList<Token> vector = (ArrayList<Token>)bigBoard.get(IFstack.peek().getLevel(),vectorVariable);
                    if(indexOfVector < vector.size()){
                        st+=" "+vector.get(indexOfVector).getToken();
                        x += 2;
                    }else{
                        throw new IllegalStateException("Error: vector has no "+arithmeticExpression.get(x).getToken()+" ordinal");
                    }
                }else{
                    if(!InitAssign.isInitialized(vectorVariable))
                        throw new IllegalStateException("Error: variable not initialized");
                    if(!InitAssign.isAccessible(vectorVariable))
                        throw new IllegalStateException("Error: variable cannot be accessed in level");
                    if(!(bigBoard.get(IFstack.peek().getLevel(),vectorVariable) instanceof ArrayList)){
                        throw new IllegalStateException("Error: variable is not a vector");
                    }
                }
            }else{
                st+=" "+arithmeticExpression.get(x).getToken();
            } 
        }
        
        
        int origCodeSize = code.size();
        for(int i=0; i < origCodeSize; i++){
            if (code.size() != 3 && code.get(0).getTokenType().equals(TokenType.DATA_TYPE)){    //removing identifiers in Primitive initialization
                code.remove(3);
            }else if(code.size() != 2 && code.get(0).getTokenType().equals(TokenType.IDENTIFIER)){  //removing identifiers in Primitive Assignment
                code.remove(2);
            }else if(code.size() != 4 && code.get(0).getTokenType().equals(TokenType.ORDINAL)){ //removing identifiers in Ordinal Assignment
                code.remove(4);
            }
        }
        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        System.out.println("String to be evaled: "+st);

        try {
            result = engine.eval(st);
        } catch (ScriptException e) {
            throw new IllegalStateException("Wrong Syntax");
        }
        if(result.equals(true) || result.equals(false) || result instanceof Number){
            //proceed to tokenizing
        }else{
            result = "\""+result+"\"";
        }
        Tokenizer tknObj = new Tokenizer(result.toString());
        Token literal = tknObj.nextToken();
    
        return literal;
    }

    public static void execute(ArrayList<Token> codeOrig) throws ScriptException{
        
        ArrayList<Token> code = new ArrayList<Token>(codeOrig);
        
        switch(code.get(0).getTokenType()){
            case DATA_TYPE:
            case IDENTIFIER:
            case ORDINAL:
                //PRIMITIVE INITIALIZATION
                if(code.size() == 4 && code.get(0).getTokenType().equals(TokenType.DATA_TYPE)){

                    InitAssign.initialize(code);
                    //System.out.println("initialization");


                //PRIMITIVE ASSIGNMENT
                }else if(code.size() == 3 && code.get(0).getTokenType().equals(TokenType.IDENTIFIER)){

                    InitAssign.assign(code);
                    //System.out.println("declaration");

                //NULL INITIALIZATION
                }else if(code.size() == 2 && code.get(0).getTokenType().equals(TokenType.IDENTIFIER) && !code.get(1).getTokenType().equals(TokenType.IDENTIFIER)){
                    
                    if(groups.isDefined(code.get(0).getToken())){
                        
                        
                        code.get(0).setTokenType(TokenType.DATA_TYPE);
                        InitAssign.initPlaceIntoMemory(code);
                    }else{
                        
                        InitAssign.initialize(code);
                    }

                    //number x
                    //word x
                    //truth x
                    //System.out.println("null init");

                //INITIALIZATION AND ASSIGNMENT WITH EXPRESSION, VECTOR INITIALIZATION, VECTOR ADD AND REMOVE
                }else if(code.size() >= 4){
                    
                     for (Token t : code) {
                        System.out.println("Code: "+t.getToken());
                     }
                    if(code.get(0).getTokenType().equals(TokenType.IDENTIFIER) && code.get(1).getToken().equals("of")){//give value to one of the members
                        if(InitAssign.isInitialized(code.get(2).getToken())){
                            
                            assignMember(code);
                        }else{
                            System.out.println("Group variable undefined");
                        }

                    }else{
                        //System.out.println("Complex Init,Assign,Op");
                        int x = 0;

                        while(x < code.size() && !code.get(x).getToken().equals(",") &&
                                !code.get(x).getTokenType().equals(TokenType.OPERATION) &&
                                !code.get(x).getTokenType().equals(TokenType.ORDINAL)){ x++; }
                        
                        if(!code.get(0).getToken().equals("print") && x < code.size() && (code.get(x).getTokenType().equals(TokenType.OPERATION) || code.get(x).getTokenType().equals(TokenType.ORDINAL))){ //OPERATOR FOUND IN LINE
                            Token literal = null;
                            try {
                               
                                
                                literal = checkExpression(code);
                            } catch (ScriptException e) {
                                e.printStackTrace();
                            }
                            code.add(literal);
                           

                           
                          
                           
                            
                            //  AFTER EVALUATING EXPRESSION AND LITERAL IS ADDED TO CODE
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
                        }else if(x < code.size() && code.get(x).getToken().equals(",")){  //THERE IS A COMMA ENCOUNTERED IN THE LINE
                           
                          
                        
                        
                            InitAssign.initialize(code);
                        }else if(code.get(0).getToken().equals("print")){
                            print.printIt(code);
                        }else{
                          
                            List<Token> expression;
                            List<Token> objToSend;
                            if(code.get(0).getTokenType().equals(TokenType.DATA_TYPE)){
                                
                                expression = code.subList(3,code.size());
                                
                                Object retval = accessGroup(expression.get(2).getToken(),expression.get(0).getToken());
                               
                                objToSend = code.subList(0,3);
                                  
                                Tokenizer tknObj = new Tokenizer(retval.toString());
                                
                                Token literal = tknObj.nextToken();
                                objToSend.add((Token) literal);
                              
                                InitAssign.initPlaceIntoMemory(objToSend);
                               
                            }else if(code.get(0).getTokenType().equals(TokenType.IDENTIFIER)){
                                
                                expression =  code.subList(2,code.size());
                                Object retval = accessGroup(expression.get(2).getToken(),expression.get(0).getToken());
                                objToSend = code.subList(0,2);
                                Tokenizer tknObj = new Tokenizer(retval.toString());
                                Token literal = tknObj.nextToken();
                                objToSend.add((Token) literal);
                                
                                InitAssign.initPlaceIntoMemory( objToSend);
                                
                            }else{
                                System.out.println("New Init or Assign!");
                            }
                            
                        }
                    }

                }
                break;

            case KEYWORD:
                if(code.get(0).getToken().equals("add")){
                    Token literal = null;
                    try {
                        literal = checkExpression(code);
                    } catch (ScriptException e) {
                        e.printStackTrace();

                    }
                    while(!code.get(1).getToken().equals("to")){
                        code.remove(1);
                    }
                    code.add(1,literal);
                  
                  
                    InitAssign.addToVector(code);

                }else if(code.get(0).getToken().equals("remove")){
                   
                    InitAssign.removeFromVector(code);
                }
                break;
            default:

                throw new IllegalStateException("Not a Declaration/Initialization");


        }
    }
    
    

}

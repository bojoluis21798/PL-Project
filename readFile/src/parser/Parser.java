/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import readfile.tokenizer.Token;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import readfile.pointers;
import static readfile.ReadFile.IFstack;
import static readfile.ReadFile.bigBoard;

//unused imports
import java.util.Iterator;
import java.util.LinkedList;
import readfile.tokenizer.TokenData;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import readfile.tokenizer.TokenType;
import static readfile.ReadFile.IFctr;
import static readfile.ReadFile.program;
import static readfile.ReadFile.q;

/**
 *
 * @author User
 */
public class Parser {
    ArrayList<Token> code = new ArrayList<>();
    List<pointers> program;
    ArrayList<Token> tkStream;
     public static Hashtable<Object, Object> varList= new Hashtable<Object, Object>();
    public String type;
     
    public Parser(ArrayList<Token> tkStream) throws ScriptException{
        this.tkStream = tkStream;
        String line = stringify(); 
        //println(line);
        
        String[] lexeme = line.split(" ");
        Start(lexeme);
    }
    
    public String stringify(){
        String s = "";
        for(int i=0; i<tkStream.size(); i++){
            switch(tkStream.get(i).getTokenType()){
                case DATA_TYPE:
                    s+="<type>";
                    break;
                case BOOLEAN_LITERAL:
                    s+="<bool>";
                    break;
                case IDENTIFIER:
                    s+="<identifier>";
                    break;
                case STRING_LITERAL:
                    s+="<string>";
                    break;
                case NUMBER_LITERAL:
                    s+="<number>";
                    break;
                case OPERATION:
                    s+="<operation>";
                    break;
                case ORDINAL:
                    s+="<ordinal>";
                    break;
                default:
                    s+=tkStream.get(i).getToken();
                    ////println("--->"+tkStream.get(i).getToken()+" is "+tkStream.get(i).getTokenType());
            }
            s+=(i==tkStream.size()-1)?"":" ";
        }
        return s;
    }
    
    /*private String parseExpression(int start, int end){
        String expr = "";
        
        for(int i=start; i<=end; i++){
            expr+=(lexeme[i].equals("<operation>"))?tkStream.get(i).getToken():lexeme[i];
        }
        return expr;
    }
    
    private boolean isExpression(String expr){
        
        //println("Expression: "+expr);
        
        if(expr.contains("<identifier>using(")){
            int opening = expr.indexOf("<identifier>using(");
            opening+=17;
            //println("Char: "+expr.charAt(opening));
            //parenthesis offset = 17
            int closing = opening+1;
            int open = -1;
            String innerExp = "";
            //println("Length "+expr.length());
            //println("Opening "+opening);
            for(; closing<expr.length() && !(expr.charAt(closing) == ')' && open == -1); closing++){
                innerExp+=expr.charAt(closing);
                if(expr.charAt(closing) == '(') open++;
                else if(expr.charAt(closing) == ')')open--;
            }
            //println("Close "+closing);
            //println("Inner Exp "+innerExp);
            if(isExpression(innerExp)){
                expr=expr.replaceAll("<identifier>using", "");
            }else{
                return false;
            }
        }
        expr=expr.replaceAll("<string>", "\" \"");
        expr=expr.replaceAll("<number>","0");
        expr=expr.replaceAll("<bool>","true");
        //expr=expr.replaceAll("(<identifier>)\\s(using)\\s\\())\\)", "");
        expr=expr.replaceAll("<identifier>", "0");
        
        //println("Expression: "+expr);
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        
        Object result = null;
        try{
            result = engine.eval(expr);
        }catch(ScriptException e){
            return false;
        }
        String type = result.getClass().getTypeName();
        //println("Type: "+type);
        
        return type.equals("java.lang.String") || 
               type.equals("java.lang.Integer") || 
               type.equals("java.lang.Boolean") ||
               type.equals("java.lang.Double");
    }*/
    
    private boolean allDeclarations(String[] lexeme, int start, int end) throws ScriptException{
        String params = "";
        for(int i=start; i<=end; i++){
            params+=lexeme[i];
        }
        //print(params);
        return params.matches("((<type>)|(<identifier>))(<identifier>)(\\,((<type>)|(<identifier>))(<identifier>))*");
    }
    
    public String Start(String[] lexeme) throws ScriptException{
        if(lexeme.length == 2 && (lexeme[0]+" "+lexeme[1]).matches("<type>\\s<identifier>")){
            type = "DECLARATION!";
        }else if(lexeme.length > 3 && (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]).matches("<type>\\s<identifier>\\sis")){
            type = "INITIALIZATION!";
        }else if(
            lexeme.length >= 5 && 
            (lexeme[0]+" "+lexeme[1]+"<expr>"+lexeme[lexeme.length-2]+" "+lexeme[lexeme.length-1]).matches("if\\s\\(<expr>\\)\\sthen")
        ){ 
            type = "IF STATEMENT!";
        }else if(lexeme.length >= 5 && 
            (lexeme[0]+" "+lexeme[1]+"<expr>"+lexeme[lexeme.length-2]+" "+lexeme[lexeme.length-1]).matches("orif\\s\\(<expr>\\)\\sthen"))
        { //orif statement
            type = "ORIF STATEMENT!";
        }else if(lexeme.length == 2 && (lexeme[0]+" "+lexeme[1]).matches("else\\sthen")){ //else statement
            type = "ELSE STATEMENT!";
        }else if(
            (lexeme.length > 2 && (lexeme[0]+" "+lexeme[1]).matches("<identifier>\\sis")) ||
            (lexeme.length > 4 && (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]+" "+lexeme[3]).matches("<ordinal>\\sof\\s<identifier>\\sis")) ||
            (lexeme.length >4 && (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]+" "+lexeme[3]).matches("<identifier>\\sof\\s<identifier>\\sis"))
        ){ //assignment
            type = ("ASSIGNMENT!");
        }else if(lexeme.length == 1 && (lexeme[0].matches("end"))){
            type = ("END!");
        }else if(
            lexeme.length >= 4 && 
            (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]+"<expr>"+lexeme[lexeme.length-1]).matches("<identifier>\\susing\\s\\(<expr>\\)")
        ){ 
            type = ("FUNCTION CALL!");
        }else if(lexeme.length == 2 && (lexeme[0]+" "+lexeme[1]).matches("<identifier>\\s<identifier>")){
            type = ("GROUP VARIABLE DECLARATION!");
        }else if(lexeme.length == 3 && (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]).matches("group\\s<identifier>\\scontains")){
            type = ("GROUP DECLARATION!");
        }else if(lexeme.length > 3 && (lexeme[0]+" <expr> "+lexeme[lexeme.length-2]+" "+lexeme[lexeme.length-1]).matches("add\\s<expr>\\sto\\s<identifier>")){
            type = ("VECTOR ADD!");
        }else if(lexeme.length == 4 && (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]+" "+lexeme[3]).matches("remove\\s<ordinal>\\sof\\s<identifier>")){
            type = ("VECTOR REMOVE!");
        }else if(lexeme.length > 4 && (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]+"<expr>"+lexeme[lexeme.length-1]).matches("repeat\\swhile\\s\\(<expr>\\)")){
            type = ("PRE TEST LOOP!");
        }else if(lexeme.length == 1 && (lexeme[0]).matches("do")){
            type = ("DO!");
        }else if(lexeme.length > 3 && (lexeme[0]+" "+lexeme[1]+"<expr>"+lexeme[lexeme.length-1]).matches("while\\s\\(<expr>\\)")){
            type = ("WHILE!");
        }else if(lexeme.length == 2 && (lexeme[0]+" "+lexeme[1]).matches("job\\s<identifier>")){
            type = "JOB DECLARATION WITHOUT PARAMS AND RETURN TYPE!";
        }else if(lexeme.length == 4 && (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]+" "+lexeme[3]).matches("job\\s<identifier>\\soutputs\\s(<type>|<identifier>)")){
            type = "JOB DECLARATION WITHOUT PARAMS!";
        }else if(
            lexeme.length > 5 && 
            (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]+" "+lexeme[3]+"<declarations>"+lexeme[lexeme.length-1]).matches("job\\s<identifier>\\susing\\s\\(<declarations>\\)")
            && allDeclarations(lexeme,4,lexeme.length-2)
        ){
            type = "JOB DECLARATION WITHOUT RETURN TYPE!";
        }else if(
            lexeme.length > 7 && (lexeme[0]+" "+lexeme[1]+" "+lexeme[2]+" "+lexeme[3]+" "+lexeme[4]+" "+lexeme[5]+"<declarations>"+lexeme[lexeme.length-1]).matches("job\\s<identifier>\\soutputs\\s(<type>|<identifier>)\\susing\\s\\(<declarations>\\)")
            && allDeclarations(lexeme,6,lexeme.length-2)
        ){
            type = "JOB DECLARATION!";
        }else if(lexeme.length >= 2 && (lexeme[0]+" <expr>").matches("return\\s<expr>")){
            type = "RETURN STATEMENT";
        }else{
            throw new IllegalStateException("Wrong Syntax");
        } 
        //println(type);
        return type;
    }
    
  public boolean isDeclared(String token){
      boolean val=false;
     if(varList.containsKey(token)){
       val = true;
     }else{
       val = false;
     }
     
     return val;
  }
  
  public boolean isAccessible(String token){
     boolean val = false;
     
     
     
     for(int ctr=0;ctr<=IFstack.peek().getLevel();ctr++){
         
         if( bigBoard.containsKeys(ctr, token)){
            val=true;
            break;
         }
     }
     
     return val;
  } 
    
}

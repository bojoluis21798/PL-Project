/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile.tokenizer;

import java.util.ArrayList;
import javax.script.ScriptException;

//unused imports
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author User
 */
public class Tokenizer {
    private ArrayList<TokenData> tokDatas;

    private String str;
    private Token lastToken=null;
    private boolean pushBack;

    public Tokenizer(String str){
       this.tokDatas = new ArrayList<TokenData>();
       this.str = str;
       
       for(String t:new String[]{"=","\\)","\\(",",",":"}){
         tokDatas.add(new TokenData(t,TokenType.TOKEN));
       }
       
       tokDatas.add(new TokenData("(1st)|(2nd)|(3rd)|([^123]th)|(\\d+\\dth)",TokenType.ORDINAL));
       tokDatas.add(new TokenData("(false)|(true)",TokenType.BOOLEAN_LITERAL));
       tokDatas.add(new TokenData("\"[^\"]*\"",TokenType.STRING_LITERAL));
       tokDatas.add(new TokenData("[a-zA-Z][a-zA-Z0-9]*",TokenType.IDENTIFIER));
       tokDatas.add(new TokenData("[+*-/<>%=]|(not)|(not=)|(or)|(and)",TokenType.OPERATION));
       tokDatas.add(new TokenData("[-]?\\d*(\\.\\d*)?",TokenType.NUMBER_LITERAL));}

    public Token nextToken() throws ScriptException{
        str = str.trim();
        if(pushBack) {
            pushBack = false;
            return lastToken;
        }

        if(str.isEmpty()){
            return (lastToken = new Token("",TokenType.EMPTY));
        }
        //tokenize(str)
        //while{}


        for(TokenData data: tokDatas) {
            String token = str;
            if(str.matches(data.getPattern())){
                if(data.getType() == TokenType.STRING_LITERAL) {
                    return (lastToken = new Token(token.substring(1,token.length()-1),TokenType.STRING_LITERAL));
                } else {
                    if(
                            token.equals("else")  ||
                            token.equals("orif")  ||
                            token.equals("if")    ||
                            token.equals("then")  ||
                            token.equals("end")   ||
                            token.equals("repeat") ||
                            token.equals("while") ||
                            token.equals("add")   ||
                            token.equals("is")    ||
                            token.equals("and")   ||
                            token.equals("or")    ||
                            token.equals("of")    ||
                            token.equals("to")    ||
                            token.equals("remove")||
                            token.equals("using") ||
                            token.equals("and") ||
                            token.equals("not") ||
                            token.equals("not=") ||
                            token.equals("=") ||
                            token.equals("group") ||
                            token.equals("contains") ||
                            token.equals("job") ||
                            token.equals("outputs") ||
                            token.equals("return") ||
                            token.equals("do")
                    ){
                        if(token.equals("and")){
                            return (lastToken = new Token("&&",TokenType.OPERATION));
                        }else if(token.equals("or")){
                            return (lastToken = new Token("||",TokenType.OPERATION));
                        }else if(token.equals("not")){
                            return (lastToken = new Token("!",TokenType.OPERATION));
                        }else if(token.equals("not=")){
                            return (lastToken = new Token("!=",TokenType.OPERATION)); //5+5
                        }else if(token.equals("=")){
                            return (lastToken = new Token("==",TokenType.OPERATION));
                        }
                        return (lastToken = new Token(token,TokenType.KEYWORD));
                    }else if(token.equals("number") || token.equals("word") || token.equals("truth") ||
                            token.equals("numbers") || token.equals("words") || token.equals("truths")){
                        return (lastToken = new Token(token,TokenType.DATA_TYPE));
                    }else if(data.getType()==TokenType.OPERATION){
                        return (lastToken = new Token(token,TokenType.OPERATION));
                    }else if(data.getType()==TokenType.IDENTIFIER){

                        return (lastToken = new Token(token,TokenType.IDENTIFIER));
                    }else{
                        return (lastToken = new Token(token,data.getType()));
                    }

                }

            }
        }
        throw new IllegalStateException("Could not parse!");

    }

    public boolean hasNextToken(){
        return !str.isEmpty();
    }

    public void pushBack(){
        if(lastToken!=null){
            this.pushBack = true;
        }
    }
}

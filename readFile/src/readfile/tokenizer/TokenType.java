/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile.tokenizer;

/**
 *
 * @author User
 */
public enum TokenType {

    EMPTY,

    /*A token. For example:(,),:,*,+,-,,,*/
    TOKEN,

    /*A name for a variable. First character is a letter and any proceeding characters are letter or numbers*/
    IDENTIFIER,

    /*A word enclose with quotation marks ("") */
    STRING_LITERAL,

    /*A number*/
    NUMBER_LITERAL,

    /*either word,words,number,numbers,flag,flags*/
    DATA_TYPE,

    /*a reserved word*/
    KEYWORD,
    
    OPERATION,
    
    BOOLEAN_LITERAL,
    
    ORDINAL,
}

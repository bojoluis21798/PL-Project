/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import parser.Parser;
import readfile.tokenizer.Token;
import readfile.tokenizer.Tokenizer;
/**
 *
 * @author User
 */
public class ReadFile {
    private static ArrayList<Token> tkStream = new ArrayList<Token>();
    /**
     * @param args the command line arguments
     */
    public ReadFile(){
         
    }
    private static final String FILENAME = "C:\\Users\\User\\source.txt";
    public static void main(String[] args) {
        // TODO code application logic here
        
        BufferedReader br = null;
		FileReader fr = null;
                ArrayList<Token> tokGroup = new ArrayList<Token>();
                try {

			br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
                        
			String sCurrentLine;
                        int ctr=0;
			while ((sCurrentLine = br.readLine()) != null) {
                                Tokenizer tknObj = new Tokenizer(sCurrentLine);
				System.out.println("\n Line"+ctr+":");
                                while(tknObj.hasNextToken()){
                                   Token retVal = tknObj.nextToken();
                                   tkStream.add(new Token(retVal.getToken(),retVal.getTokenType()));
                                   tokGroup.add(new Token(retVal.getToken(),retVal.getTokenType()));
                                   
                                   System.out.println(retVal.getToken()+"=>"+retVal.getTokenType());
                                }
                                Parser p = new Parser(tkStream);
                                ctr++;
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
    }
    
}

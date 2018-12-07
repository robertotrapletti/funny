package compiler;

import compiler.exceptions.PrevAlreadyCalledException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class TokenizerTest {

    @Test
    public void tokenizerTest1() {
        ArrayList<Token> expectedTokens=new ArrayList<>();

        Tokenizer tokenizer = new Tokenizer(new StringReader("f=()->{c=3.43;c>3;print(\"c\")}"));
        Token t = tokenizer.next();
        expectedTokens.add(Token.createToken(Type.Id,null,"f"));
        expectedTokens.add(Token.createToken(Type.EQUAL,null,"="));
        expectedTokens.add(Token.createToken(Type.OPEN_PAREN,null,"("));
        expectedTokens.add(Token.createToken(Type.CLOSE_PAREN,null,")"));
        expectedTokens.add(Token.createToken(Type.LAMBDA,null,"->"));
        expectedTokens.add(Token.createToken(Type.OPEN_BRACE,null,"{"));
        expectedTokens.add(Token.createToken(Type.Id,null,"c"));
        expectedTokens.add(Token.createToken(Type.EQUAL,null,"="));
        expectedTokens.add(Token.createToken(Type.Num,new BigDecimal(3.43),"3.43"));
        expectedTokens.add(Token.createToken(Type.SEMICOLON,null,";"));
        expectedTokens.add(Token.createToken(Type.Id,null,"c"));
        expectedTokens.add(Token.createToken(Type.GREATHER_THAN,null,">"));
        expectedTokens.add(Token.createToken(Type.Num,new BigDecimal(3),"3"));
        expectedTokens.add(Token.createToken(Type.SEMICOLON,null,";"));
        expectedTokens.add(Token.createToken(Type.Id,null,"print"));
        expectedTokens.add(Token.createToken(Type.OPEN_PAREN,null,"("));
        expectedTokens.add(Token.createToken(Type.String,null,"c"));
        expectedTokens.add(Token.createToken(Type.CLOSE_PAREN,null,")"));
        expectedTokens.add(Token.createToken(Type.CLOSE_BRACE,null,"}"));
        Iterator<Token> iterExpected= expectedTokens.iterator();
        while (t.getType()!=Type.EOS) {
            assertEquals(iterExpected.next(),t);
            t=tokenizer.next();
        }
    }

    @Test
    public void commentTest() {
        ArrayList<Token> expectedTokens=new ArrayList<>();
        Tokenizer tokenizer = new Tokenizer(new StringReader("false /* da print/*Ciaoo */ asd/*c*/ */true"));
        expectedTokens.add(Token.createToken(Type.False,null,"false"));
        expectedTokens.add(Token.createToken(Type.True,null,"true"));
        Iterator<Token> iterExpected= expectedTokens.iterator();
        Token t = tokenizer.next();
        while (t.getType()!=Type.EOS) {
            assertEquals(iterExpected.next(),t);
            t=tokenizer.next();
        }
    }

    @Test
    public void prevTest() throws PrevAlreadyCalledException {
        ArrayList<Token> expectedTokens=new ArrayList<>();
        expectedTokens.add(Token.createToken(Type.Id,null,"f"));
        expectedTokens.add(Token.createToken(Type.EQUAL,null,"="));
        expectedTokens.add(Token.createToken(Type.Num,new BigDecimal(3),"3"));
        Tokenizer tokenizer = new Tokenizer(new StringReader("f=3"));
        Token t=tokenizer.next();
        assertEquals(expectedTokens.get(0),t);
        t=tokenizer.next();
        assertEquals(expectedTokens.get(1),t);
        tokenizer.prev();
        t=tokenizer.next();
        assertEquals(expectedTokens.get(1),t);
        t=tokenizer.next();
        assertEquals(expectedTokens.get(2),t);
        assertEquals(expectedTokens.get(2),tokenizer.getCurrentToken());
    }

    @Test
    public void prevExceptionTest() throws PrevAlreadyCalledException {
        Tokenizer tokenizer = new Tokenizer(new StringReader("f=3"));
        tokenizer.next();
        tokenizer.next();
        tokenizer.prev();
        assertThrows(PrevAlreadyCalledException.class, tokenizer::prev);
    }

    @Test
    public void numTest(){
        Tokenizer tokenizer = new Tokenizer(new StringReader("=13.43e-3+f"));
        tokenizer.next();
        assertEquals(new BigDecimal("13.43e-3"),tokenizer.next().getNum());
        assertEquals(Type.PLUS,tokenizer.next().getType());
    }

    @Test
    public void bracketTest(){
        Tokenizer tokenizer = new Tokenizer(new StringReader("[2]"));
        assertEquals(Type.OPEN_BRACKET,tokenizer.next().getType());
        tokenizer.next();
        assertEquals(Type.CLOSE_BRACKET,tokenizer.next().getType());
    }

}

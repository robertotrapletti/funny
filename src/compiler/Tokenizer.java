package compiler;

import compiler.exceptions.EscapeException;
import compiler.exceptions.PrevAlreadyCalledException;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.regex.Pattern;


public class Tokenizer {

    private Reader reader;
    private Token token, prevToken;
    private boolean prev;

    public Tokenizer(Reader reader) {
        this.prev = false;
        this.reader = reader;
    }

    public Token next() {
        if (prev) {
            prev = false;
            return token;
        }
        prevToken = token;

        skipWhite();
        int value = markAndRead(1);
        if (Character.isJavaIdentifierStart(value)) {
            return word(value);
        }
        if (Character.isDigit(value)||value=='.') {
            return number(value);
        }
        switch (value) {
            case '"':
                return string();
            case '-':
                return minusOrLambdaOrMinusEqual();
            case '+':
                return opOrOpEqual(Type.PLUS, "+", Type.PLUS_EQUAL, "+=");
            case '/':
                return commentOrOp();
            case '>':
                return opOrOpEqual(Type.GREATHER_THAN, ">", Type.GREATHER_EQUAL_THEN, ">=");
            case '<':
                return opOrOpEqual(Type.LESS_THAN, "<", Type.LESS_EQUAL_THAN, "<=");
            case '*':
                return opOrOpEqual(Type.MULTIPLICATION, "*", Type.MULTIPLICATION_EQUAL, "*=");
            case '=':
                return opOrOpEqual(Type.EQUAL, "=", Type.EQUALITY, "==");
            case ';':
                return returnToken(Type.SEMICOLON, null, ";");
            case ',':
                return returnToken(Type.COMMA, null, ",");
            case '(':
                return returnToken(Type.OPEN_PAREN, null, "(");
            case ')':
                return returnToken(Type.CLOSE_PAREN, null, ")");
            case '{':
                return returnToken(Type.OPEN_BRACE, null, "{");
            case '[':
                return returnToken(Type.OPEN_BRACKET,null,"[");
            case '%':
                return returnToken(Type.MODULO, null, "%");
            case ']':
                return returnToken(Type.CLOSE_BRACKET,null,"]");
            case '}':
                return returnToken(Type.CLOSE_BRACE, null, "}");
            case '!':
                return opOrOpEqual(Type.BANG, "!",Type.INEQUALITY, "!=");
            case -1:
                return returnToken(Type.EOS, null, "EoS");
        }
        return returnToken(Type.UNKNOWN, null, "UNKNOWN");
    }

    private Token commentOrOp() {
        int next = markAndRead(1);
        if (next == '*') {
            skipBlockComment(1);
            return next();
        } else if(next=='/'){
            skipLineComment();
            return next();
        }else {
            reset();
            return opOrOpEqual(Type.DIVISION, "/", Type.DIVISION_EQUAL, "/=");
        }

    }

    private void skipLineComment() {
        int value = markAndRead(1);
        while (value != '\n') {
            value = markAndRead(1);
        }
    }


    private void skipBlockComment(int nNestedComment) {
        int value = markAndRead(1);
        while (value != '/' && value != '*') {
            value = markAndRead(1);
        }
        int nextValue = markAndRead(1);
        if (value == '/' && nextValue == '*') {
            skipBlockComment(++nNestedComment);
        } else if (value == '*' && nextValue == '/') {
            if (--nNestedComment != 0) {
                skipBlockComment(nNestedComment);
            }
        } else {
            skipBlockComment(nNestedComment);
        }
    }

    private Token minusOrLambdaOrMinusEqual() {
        int next = markAndRead(1);
        if (next == '>') {
            return returnToken(Type.LAMBDA, null, "->");
        } else {
            reset();
            return opOrOpEqual(Type.MINUS, "-", Type.MINUS_EQUAL, "-=");
        }
    }

    private Token opOrOpEqual(Type op, String opString, Type opEqual, String opEqualString) {
        int next = markAndRead(1);
        if (next == '=') {
            return returnToken(opEqual, null, opEqualString);
        } else {
            reset();
            return returnToken(op, null, opString);
        }
    }


    private Token string() {
        int value = markAndRead(1);
        StringBuilder name = new StringBuilder();
        while (value != '"') {
            if(value==-1)returnToken(Type.EOS,null,"EoS");
            if(value=='\\'){
                value=markAndRead(1);
                name.append(escapeCharacter(value));
            }else {
                name.append((char) value);
            }
            value = markAndRead(1);
        }
        return returnToken(Type.String, null, name.toString());
    }

    private char escapeCharacter(int value) {
        switch (value){
            case 'n':return '\n';
            case 't':return '\t';
            case 'r':return '\r';
            case '"':return '\"';
            case 'b':return '\b';
            case 'f':return '\f';
            case '\\':return '\\';
            case '\'':return '\'';
            default: throw new EscapeException("error");
        }
    }

    private Token number(int value) {
        StringBuilder number = new StringBuilder();
        //example: 13.24E-3
        while (Character.isDigit(value)) {
            number.append((char) value);
            value = markAndRead(1);
        }
        if (value == '.') {
            number.append((char) value);
            value = markAndRead(1);
            while (Character.isDigit(value)) {
                number.append((char) value);
                value = markAndRead(1);
            }
        }
        if(value=='e'||value=='E'){
            number.append((char) value);
            value = markAndRead(1);
            if(value=='+'||value=='-'){
                number.append((char)value);
                value=markAndRead(1);
            }
            while (Character.isDigit(value)) {
                number.append((char) value);
                value = markAndRead(1);
            }
        }
        reset();
        return returnToken(Type.Num, new BigDecimal(number.toString()), number.toString());
    }

    private Token word(int startChar) {
        StringBuilder name = new StringBuilder();
        name.append((char) startChar);
        int value = markAndRead(1);
        while (Character.isJavaIdentifierPart(value)) {
            name.append((char) value);
            value = markAndRead(1);
        }
        reset();
        return returnToken(Type.Id, null, name.toString());
    }

    private void skipWhite() {
        if (Character.isWhitespace(markAndRead(1))) {
            skipWhite();
        } else {
            reset();
        }
    }


    private int markAndRead(int w) {
        try {
            reader.mark(w);
            return reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void reset() {
        try {
            reader.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean matches(String regex, int value) {
        return Pattern.matches(regex, String.valueOf((char) value));
    }

    public Token returnToken(Type type, BigDecimal numValue, String stringValue) {
        token = Token.createToken(type, numValue, stringValue);
        return token;
    }

    public Token prev() throws PrevAlreadyCalledException {
        if (prev)
            throw new PrevAlreadyCalledException();
        prev = true;
        return prevToken;
    }

    public Token getCurrentToken() {
        return prev ? prevToken : token;
    }

}

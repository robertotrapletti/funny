package compiler;

public enum Type {
    BoR,

    Nil, False, True,
    If, Ifnot, Then, Else, Fi,
    While, Whilenot, Do, Od,
    Print, Println,

    EoR,

    Id, Num, String,

    SEMICOLON, COMMA, LAMBDA, TO, HASHTAG, DOUBLE_DOTS,DOT,

    OPEN_PAREN, CLOSE_PAREN,
    OPEN_BRACE, CLOSE_BRACE,
    OPEN_BRACKET, CLOSE_BRACKET,
    BANG,
    MULTIPLICATION, DIVISION, MODULO,
    PLUS, MINUS,

    LESS_THAN, LESS_EQUAL_THAN, GREATHER_THAN, GREATHER_EQUAL_THEN,
    EQUALITY, INEQUALITY,
    AND, OR,
    EQUAL,
    PLUS_EQUAL, MINUS_EQUAL, MULTIPLICATION_EQUAL, DIVISION_EQUAL, MODULO_EQUAL,
    EOS,
    UNKNOWN;

    public String toString(){
        return name().toLowerCase();
    }

}

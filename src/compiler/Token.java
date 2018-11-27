package compiler;

import java.math.BigDecimal;
import java.util.Arrays;

public class Token {
    private final BigDecimal numValue;
    private final String stringValue;
    private final Type type;
    private static Type[] Reserved;

    static {
        Reserved = new Type[Type.EoR.ordinal() - Type.BoR.ordinal()];
        Type[] values = Type.values();
        for (int i = Type.BoR.ordinal(); i < Type.EoR.ordinal(); i++) {
            Reserved[i] = values[i];
        }
    }

    public static Token createToken(Type type, BigDecimal numValue, String stringValue){
        if (nameIsReserved(stringValue)) {
            return new Token(Type.valueOf(Character.toUpperCase(stringValue.charAt(0)) + stringValue.substring(1)),
                    null,stringValue);
        } else {
            return new Token(type,numValue,stringValue);
        }
    }

    private Token(Type type, BigDecimal numValue, String stringValue) {
        this.numValue = numValue;
        this.stringValue = stringValue;
        this.type = type;
    }

    private static boolean nameIsReserved(String name) {
        return Arrays.stream(Reserved).anyMatch(t->name.equals(t.toString()));
    }

    public Type getType() {
        return this.type;
    }

    public BigDecimal getNum() {
        return this.numValue;
    }

    public String getString() {
        return stringValue;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Token){
            return this.getType()==((Token) obj).getType()&&this.getString().equals(((Token) obj).getString());
        }
        return false;
    }
}

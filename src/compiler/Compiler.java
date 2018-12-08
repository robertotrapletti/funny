package compiler;

import compiler.exceptions.*;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 *
 * EBNF grammar for Funny

variable *names* start with lowercase, token *names* start with Uppercase.

----

program ::= function Eos .

function ::= "{" optParams optLocals optSequence "}" .
optParams ::= ( "(" optIds ")" )? .
optLocals ::= optIds .
optSequence ::= ( "->" sequence )? .
optIds::= id* .
id ::= Id .

sequence ::= optAssignment ( ";" optAssignment )* .
optAssignment := assignment? .
assignment ::= Id ( "=" | "+=" | "-=" | "*=" | "/=" | "%=" ) assignment
	| logicalOr .

logicalOr ::= logicalAnd ( "||" logicalOr )? .
logicalAnd ::= equality ( "&&" logicalAnd )? .
equality ::= comparison ( ( "==" | "!=" ) comparison )? .
comparison ::= add ( ( "<" | "<=" | ">" | ">=" ) add )? .
add ::= mult ( ( "+" | "-" ) mult )* .
mult ::= unary ( ( "*" | "/" | "%" ) unary )* .
unary ::= ( "+" | "-" | "!" ) unary
	| postfix .

postfix ::= primary args* .
args ::= "(" ( sequence ( "," sequence )* )? ")" .
primary ::= checkNum | bool | nil | string
	| getId
	| function
	| subsequence
	| cond
	| loop
	| print .

checkNum ::= Num .
bool ::= True | False .
nil ::= Nil .
string ::= String .
getId ::= Id .
subsequence ::= "(" sequence ")" .
cond ::= ( "if" | "ifnot" ) sequence "then" sequence ( "else" sequence )? "fi" .
loop ::= ( "while" | "whilenot" ) sequence ( "do" sequence )? "od" .
print ::= ( "print" | "println" ) args .
*/


public class Compiler {


    ////////////////////////////////////////////////////////////////////////////////////////
    //	Local Fields
    ////////////////////////////////////////////////////////////////////////////////////////

    private Tokenizer tokenizer;

    public Compiler(Reader in) throws Exception {
        tokenizer = new Tokenizer(in);

    }

    //TODO: tokenizer with currentToken as field (next as void)
    private void next() {
        tokenizer.next();
    }



    /*#####################################################################################
     * program ::= function Eos .
     #####################################################################################*/

    public Expr program() throws IOException, SyntaxErrorException {
        tokenizer.next();
        Expr expr = functionExpression(null);
        check(Type.EOS, tokenizer.getCurrentToken());
        return expr;
    }

    /*#####################################################################################
     * function ::= "{" optParams optLocals optSequence "}" .
     #####################################################################################*/

    private Expr functionExpression(Scope scope) throws IOException, SyntaxErrorException {
        checkAndNext(Type.OPEN_BRACE, tokenizer.getCurrentToken());
        ArrayList<String> params = optParams();
        ArrayList<String> locals = optLocals();
        //TODO:check duplicates
        Expr expr = optSequence(new Scope(locals, params, scope));
        checkAndNext(Type.CLOSE_BRACE, tokenizer.getCurrentToken());
        FunExpr funExpr = new FunExpr(expr, params, locals);
        return funExpr;
    }



        /*#####################################################################################
         * optParams ::= ( "(" optIds ")" )? .
         #####################################################################################*/

    private ArrayList<String> optParams() throws IOException, SyntaxErrorException {
        ArrayList<String> params = new ArrayList<>();
        if (!(Type.OPEN_PAREN == getCurrentType())) {
            return params;
        } else {
            next();
            params = optIds();
            checkAndNext(Type.CLOSE_PAREN, tokenizer.getCurrentToken());
            return params;
        }
    }

        /*#####################################################################################
         * optLocals ::= optIds .
         #####################################################################################*/

    private ArrayList<String> optLocals() throws IOException, SyntaxErrorException {
        return optIds();
    }

        /*#####################################################################################
         * optIds::= id* .
         #####################################################################################*/

    private ArrayList<String> optIds() {
        ArrayList<String> ids = new ArrayList<>();
        while (getCurrentType() == Type.Id) {
            ids.add(tokenizer.getCurrentToken().getString());
            next();
        }
        return ids;
    }


    /*#####################################################################################
     * optSequence ::= ( "->" sequence )? .
     #####################################################################################*/
    //arrow already checked in functionExpression()
    private Expr optSequence(Scope scope) throws IOException, SyntaxErrorException {
        if (Type.LAMBDA == getCurrentType()) {
            next();
            return sequence(scope);
        }
        return NilVal.nil;
    }


    /*#####################################################################################
     * sequence ::= optAssignment ( ";" optAssignment )* .
     #####################################################################################*/
    private Expr sequence(Scope scope) throws IOException, SyntaxErrorException {
        ArrayList<Expr> optAssignments = new ArrayList<>();
        Expr expr = optAssignement(scope);
        if (expr != null)
            optAssignments.add(expr);
        while (Type.SEMICOLON == getCurrentType()) {
            next();
            expr = optAssignement(scope);
            if (expr != null)
                optAssignments.add(expr);
        }
        return optAssignments.size() == 0 ? NilVal.nil : optAssignments.size() == 1 ? optAssignments.get(0) : new SequenceExpr(optAssignments);
    }

    /*#####################################################################################
    * optAssignment := assignment? .
    #####################################################################################*/
    private Expr optAssignement(Scope scope) throws SyntaxErrorException, IOException {
        return isBeginOfAssignment()?assignment(scope):null;
    }

    /*#####################################################################################
    * assignment ::= Id ( "=" | "+=" | "-=" | "*=" | "/=" | "%=" ) assignment
    *              | logicalOr .
    #####################################################################################*/
    private Expr assignment(Scope scope) throws IOException, SyntaxErrorException {
        if (Type.Id == getCurrentType()) {
            Token idToken = tokenizer.getCurrentToken();
            scope.checkInScope(idToken.getString());
            next();
            switch (getCurrentType()) {
                case EQUAL:
                case PLUS_EQUAL:
                case MINUS_EQUAL:
                case MULTIPLICATION_EQUAL:
                case DIVISION_EQUAL:
                case MODULO_EQUAL:
                    Token opToken = tokenizer.getCurrentToken();
                    next();
                    return new SetVarExpr(idToken.getString(), opToken.getType(), assignment(scope));
                default:
                    tokenizer.prev();
            }
        }
        return logicalOr(scope);
    }

    /*#####################################################################################
    * logicalOr ::= logicalAnd ( "||" logicalOr )? .
    #####################################################################################*/
    private Expr logicalOr(Scope scope) throws SyntaxErrorException, IOException {
        Expr lAnd = logicalAnd(scope);
        if (Type.OR == getCurrentType()) {
            next();
            return new BinaryExpr(lAnd, Type.OR,logicalOr(scope));
        }
        return lAnd;
    }

    /*#####################################################################################
    * logicalAnd ::= equality ( "&&" logicalAnd )? .
    #####################################################################################*/
    private Expr logicalAnd(Scope scope) throws SyntaxErrorException, IOException {
        Expr equality = equality(scope);
        if (Type.AND == getCurrentType()) {
            next();
            return new BinaryExpr(equality, Type.AND,logicalAnd(scope));
        }
        return equality;
    }

    /*#####################################################################################
    * equality ::= comparison ( ( "==" | "!=" ) comparison )? .
    #####################################################################################*/
    private Expr equality(Scope scope) throws SyntaxErrorException, IOException {
        Expr comparision = comparision(scope);
        if (Type.EQUALITY == getCurrentType() || Type.INEQUALITY == getCurrentType()) {
            Type logicalType = getCurrentType();
            next();
            return new BinaryExpr(comparision, logicalType, comparision(scope));
        }
        return comparision;
    }

    /*#####################################################################################
    * comparison ::= add ( ( "<" | "<=" | ">" | ">=" ) add )? .
    #####################################################################################*/
    private Expr comparision(Scope scope) throws SyntaxErrorException, IOException {
        Expr add = add(scope);
        if (Type.LESS_THAN == getCurrentType() ||
                Type.LESS_EQUAL_THAN == getCurrentType() ||
                Type.GREATHER_THAN == getCurrentType() ||
                Type.GREATHER_EQUAL_THEN == getCurrentType()) {
            Type logicalType = getCurrentType();
            next();
            return new BinaryExpr(add, logicalType, add(scope));
        }
        return add;
    }

    /*#####################################################################################
    add ::= mult ( ( "+" | "-" ) mult )* .
    #####################################################################################*/
    private Expr add(Scope scope) throws SyntaxErrorException, IOException {
        Expr expr=mult(scope);
        while (Type.PLUS == getCurrentType() ||
                Type.MINUS == getCurrentType()) {
            Type type=getCurrentType();
            next();
            expr= new BinaryExpr(expr,type,mult(scope));
        }
        return expr;
    }

    /*#####################################################################################
    mult ::= unary ( ( "*" | "/" | "%" ) unary )* .
    #####################################################################################*/
    private Expr mult(Scope scope) throws SyntaxErrorException, IOException {
        Expr expr=unary(scope);
        while (Type.MULTIPLICATION == getCurrentType() ||
                Type.DIVISION == getCurrentType() ||
                Type.MODULO == getCurrentType()) {
            Type type=getCurrentType();
            next();
            expr= new BinaryExpr(expr,type,unary(scope));
        }
        return expr;
    }

    /*#####################################################################################
    unary ::= ( "+" | "-" | "!" ) unary
	| postfix .
    #####################################################################################*/
    private Expr unary(Scope scope) throws SyntaxErrorException, IOException {
        if (Type.PLUS == getCurrentType() ||
                Type.MINUS == getCurrentType() ||
                Type.BANG == getCurrentType()) {
            Type sign = getCurrentType();
            next();
            return new UnaryExpr(sign, unary(scope));
        }
        return postfix(scope);
    }

    /*#####################################################################################
    postfix ::= primary args* .
    #####################################################################################*/
    private Expr postfix(Scope scope) throws SyntaxErrorException, IOException {
        Expr expr = primary(scope);
        while (Type.OPEN_PAREN == getCurrentType()) {
            expr= new InvokeExpr(expr, args(scope));
        }
        return expr;
    }

    /*#####################################################################################
    args ::= "(" ( sequence ( "," sequence )* )? ")" .
    #####################################################################################*/
    private List<Expr> args(Scope scope) throws SyntaxErrorException, IOException {
        ArrayList<Expr> args = new ArrayList<>();
        checkAndNext(Type.OPEN_PAREN, tokenizer.getCurrentToken());
        if(getCurrentType()!=Type.CLOSE_PAREN) {
            args.add(sequence(scope));
            while (Type.COMMA == getCurrentType()) {
                next();
                args.add(sequence(scope));
            }
        }
        checkAndNext(Type.CLOSE_PAREN, tokenizer.getCurrentToken());
        return args;
    }

    /*#####################################################################################
    primary ::= checkNum | bool | nil | string
	| getId
	| function
	| subsequence
	| cond
	| loop
	| print .
    #####################################################################################*/
    private Expr primary(Scope scope) throws IOException, SyntaxErrorException {
        switch (getCurrentType()) {
            case Num:
                return num();
            case True:
            case False:
                return bool();
            case String:
                return string();
            case Id:
                return getId(scope);
            case OPEN_BRACE:
                return functionExpression(scope);
            case OPEN_PAREN:
                return subsequence(scope);
            case If:
            case Ifnot:
                return cond(scope);
            case While:
            case Whilenot:
                return loop(scope);
            case Print:
            case Println:
                return print(scope);
            default:
                next();
                return NilVal.nil;
        }
    }

    private Expr print(Scope scope) throws IOException, SyntaxErrorException {
        Type type = getCurrentType();
        next();
        return new PrintExpr(args(scope), type);
    }

    /*#####################################################################################
    loop ::= ( "while" | "whilenot" ) sequence ( "do" sequence )? "od" .
    #####################################################################################*/
    private Expr loop(Scope scope) throws IOException, SyntaxErrorException {
        Type type = getCurrentType();
        next();
        Expr condExpr = sequence(scope);
        Expr doExpr = new NilVal();
        if (Type.Do == getCurrentType()) {
            next();
            doExpr = sequence(scope);
        }
        checkAndNext(Type.Od, tokenizer.getCurrentToken());
        return new WhileExpr(condExpr, doExpr, type);
    }

    /*#####################################################################################
    cond ::= ( "if" | "ifnot" ) sequence "then" sequence ( "else" sequence )? "fi" .
    #####################################################################################*/
    private Expr cond(Scope scope) throws SyntaxErrorException, IOException {
        Type type = getCurrentType();
        next();
        Expr condExpr = sequence(scope);
        checkAndNext(Type.Then, tokenizer.getCurrentToken());
        Expr thenExpr = sequence(scope);
        Expr elseExpr = new NilVal();
        if (Type.Else == getCurrentType()) {
            next();
            elseExpr = sequence(scope);
        }
        checkAndNext(Type.Fi, tokenizer.getCurrentToken());
        return new IfExpr(condExpr, thenExpr, elseExpr, type);
    }

    private Expr subsequence(Scope scope) throws SyntaxErrorException, IOException {
        checkAndNext(Type.OPEN_PAREN, tokenizer.getCurrentToken());
        Expr expr = sequence(scope);
        checkAndNext(Type.CLOSE_PAREN, tokenizer.getCurrentToken());
        return expr;
    }

    private GetVarExpr getId(Scope scope) {
        String id = tokenizer.getCurrentToken().getString();
        scope.checkInScope(id);
        next();
        return new GetVarExpr(id);
    }

    private StringVal string() {
        String string = tokenizer.getCurrentToken().getString();
        next();
        return new StringVal(string);
    }

    private BoolVal bool() {
        Type type = getCurrentType();
        next();
        return new BoolVal(type);
    }

    private NumVal num() {
        BigDecimal number = tokenizer.getCurrentToken().getNum();
        next();
        return new NumVal(number);
    }


    /////////////////////////////
    /// Utilities
    /////////////////////////////

    private void checkAndNext(Type type, Token token) throws SyntaxErrorException {
        if (type == token.getType()) {
            tokenizer.next();
        } else {
            throw new SyntaxErrorException("SyntaxException, expected: " + type + ", found: " + token.getType());
        }
    }

    private boolean check(Type type, Token token) throws SyntaxErrorException {
        if (type == token.getType()) {
            return true;
        } else {
            throw new SyntaxErrorException("expected: "+type+", found: "+token.getType());
        }
    }

    private Type getCurrentType() {
        return tokenizer.getCurrentToken().getType();
    }

    private boolean isBeginOfAssignment(){
        switch (getCurrentType()){
            case Id:
            case PLUS:case MINUS:case BANG:
            case Num: case True:case False:
            case Nil:case String:case OPEN_BRACE:
            case OPEN_PAREN:case If:case Ifnot:
            case While:case Whilenot:
            case Println:case Print:return true;
            default:return false;

        }
    }
}

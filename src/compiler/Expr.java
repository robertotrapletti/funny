package compiler;

import compiler.exceptions.InterpreterException;

abstract public class Expr {
    abstract Val eval(Env env) throws InterpreterException;
}

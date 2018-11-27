package compiler;

import compiler.exceptions.CompilerException;

import java.util.ArrayList;

public class Scope {
    private ArrayList<String> localsAndParams;
    private Scope parentScope;

    public Scope(ArrayList<String> locals, ArrayList<String> params, Scope parentScope) {
        this.localsAndParams=new ArrayList<>();
        this.localsAndParams.addAll(locals);
        this.localsAndParams.addAll(params);
        this.parentScope = parentScope;
    }

    public void checkInScope(String name){
        if(!isPresent(name))
            throw new CompilerException("The id "+name+" is not defined");
    }

    private boolean isPresent(String name){
        return localsAndParams.contains(name) || (parentScope != null && parentScope.isPresent(name));
    }
}

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.*;

public class RuntimeFunc extends RuntimeValue {
  String name;
  ArrayList<AspName> params;
  AspSuite suite;
  RuntimeScope outerScope;

  public RuntimeFunc(String name, ArrayList<AspName> params, AspSuite suite, RuntimeScope outerScope) {
    this.name = name;
    this.params = params;
    this.suite = suite;
    this.outerScope = outerScope;
  }

  public RuntimeFunc(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "function " + name;
  }

  @Override
  protected String typeName() {
    return "function";
  }

  @Override
  public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
    if (actualParams.size() != params.size()) {
      runtimeError("Expected " + params.size() + " arguments but found " + actualParams.size(), where);
      return null;
    }

    //Opprett et nytt RuntimeScope-objekt. Dette skopets outer skal være det skopet der funksjonen ble deklarert
    RuntimeScope scope = new RuntimeScope(outerScope);
    for (int i = 0; i < params.size(); i++) {
      String name = params.get(i).name;
      RuntimeValue value = actualParams.get(i);
      scope.assign(name, value);
    }

    try {
      suite.eval(scope);
    } catch (RuntimeReturnValue returnValue) {
      return returnValue.value;
    }
    return null; 
  }
}

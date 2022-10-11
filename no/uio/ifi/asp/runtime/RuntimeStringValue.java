package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue {
  String strValue;

  @Override
  public String showInfo() {
    if (strValue.indexOf('\'') >= 0)
      return '"' + strValue + '"';
    else
      return "'" + strValue + "'";
  }
  
  @Override
  String typeName() {
    return "string";
  }

  @Override
  public boolean getStringValue(String what, AspSyntax where) {
    return strValue;
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    return strValue == '' ? false : true;
  }

  @Override
  public long getIntValue(String what, AspSyntax where) {
    try {
      return Long.parseLong(strValue);
    } catch (NumberFormatException e) {
      runtimeError("Cannot convert '" + what + "' to integer " , where);
      return null;
    }
  }

  @Override
  public double getFloatValue(String what, AspSyntax where) {
    try {
      return Double.parseDouble(strValue);
    } catch (NumberFormatException e) {
      runtimeError("Cannot convert '" + what + "' to float", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeStringValue)
      return new RuntimeStringValue(strValue + v.getStringValue("+ operand", where));
    runtimeError("Type error for +", where);
    return null;
  }
  
  
  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeStringValue)
    return new RuntimeStringValue(strValue == v.getStringValue("== operand", where));
    runtimeError("Type error for ==", where);
    return null;
  }

  @Override
  public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeStringValue){
      String vStr = v.getStringValue("> operand", where);
      return new RuntimeBoolValue(strValue.compareTo(vStr)>0);
    }
    runtimeError("Type error for >", where);
    return null;
  }

  @Override
  public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeStringValue){
      String vStr = v.getStringValue(">= operand", where);
      return new RuntimeBoolValue(strValue.compareTo(vStr)>=0);
    }
    runtimeError("Type error for >=", where);
    return null;
  }

  @Override
  public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeStringValue) {
      String vStr = v.getStringValue("< operand", where);
      return new RuntimeBoolValue(strValue.compareTo(vStr) < 0);
    }
    runtimeError("Type error for <", where);
    return null;
  }
  
  @Override
  public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeStringValue){
      String vStr = v.getStringValue("<= operand", where);
      return new RuntimeBoolValue(strValue.compareTo(vStr)<=0);
    }
    runtimeError("Type error for <=", where);
    return null;
  }
  
  @Override
  public RuntimeValue evalLen(AspSyntax where) {
    return new RuntimeIntValue(strValue.length());
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeIntValue)
      String result = "";
      for (int i = 0; i < v.getIntValue("* operand", where); i++) 
        result+=strValue;
      return new RuntimeStringValue(result);
    runtimeError("Type error for *", where);
    return null;
  }
  

  @Override
  public RuntimeValue evalNot(AspSyntax where) {
    return new RuntimeBoolValue(strValue == "");
  }

  @Override
  public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeStringValue)
      return new RuntimeBoolValue(strValue != v.getStringValue("!= operand", where));
    runtimeError("Type error for !=", where);
    return null;
  }
  

  @Override
  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeIntValue) {
      long vInt = v.getIntValue("string subscription", where)
      if ( vInt>= strValue.length() || vInt<0) {
        runtimeError("Invalid index for the string!",where);
      else {
        return new RuntimeStringValue(Character.toString(strValue.charAt(vInt));
      }
    }
    runtimeError("Type error for *", where);
    return null;
  }
  
}

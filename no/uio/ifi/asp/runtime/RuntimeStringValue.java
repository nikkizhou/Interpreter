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
      runtimeError("Cannot convert '" + strValue + "' to " + what, where);
      return null;
    }
  }

  @Override
  public double getFloatValue(String what, AspSyntax where) {
    try {
      return Double.parseDouble(strValue);

    } catch (NumberFormatException e) {
      runtimeError("Cannot convert '" + strValue + "' to " + what, where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeStringValue)
      return new RuntimeStrValue(strValue + v.getStringValue("+ operand", where));
    runtimeError("Type error for +", where);
    return null;
  }
  
  @Override
  public RuntimeValue evalLen(AspSyntax where) {
    return new RuntimeIntValue(strValue.length());
  }

  @Override
  public RuntimeValue evalNot(AspSyntax where) {
    return new RuntimeBoolValue(strValue == "");
  }

  
}

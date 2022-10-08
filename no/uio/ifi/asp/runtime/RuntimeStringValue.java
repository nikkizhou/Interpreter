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
}

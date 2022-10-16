package no.uio.ifi.asp.runtime;

import java.util.HashMap;
import no.uio.ifi.asp.parser.AspSyntax;


public class RuntimeDictValue extends RuntimeValue {
  HashMap<String,RuntimeValue> dictValue = new HashMap<>();

  public RuntimeDictValue(HashMap<String,RuntimeValue> dict) {
    dictValue = dict;
  }

  @Override
  public String typeName() {
    return "Dictionary";
  }

  @Override
  public String showInfo() {
    return dictValue.toString().replace('=',':');
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    return dictValue.size() != 0;
  }

  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeNoneValue)
      return new RuntimeBoolValue(false);
    runtimeError("Type error for == on dict", where);
    return null;
  }

  @Override
  public RuntimeValue evalLen(AspSyntax where) {
    return new RuntimeIntValue(dictValue.size());
  }

  @Override
  public RuntimeValue evalNot(AspSyntax where) {
    return new RuntimeBoolValue(dictValue.size() == 0);
  }

  @Override
  public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeNoneValue) {
      return new RuntimeBoolValue(true);
    }
    runtimeError("Type error for != on dict", where);
    return null;
  }

  @Override
  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeStringValue) {
      String key = v.getStringValue("dict subscription", where);
      if (dictValue.containsKey(key)) 
        return dictValue.get(key);
      else
        runtimeError("Invalid key for dict subscription", where);
      return null;
    }
    runtimeError("Type error for dict subscription.", where);
    return null;
  }
}

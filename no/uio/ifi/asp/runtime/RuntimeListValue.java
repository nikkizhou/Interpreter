package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;
import java.util.ArrayList;
import java.util.Arrays;

public class RuntimeListValue extends RuntimeValue {
  ArrayList<RuntimeValue> listValue = new ArrayList<>();

  public RuntimeListValue(ArrayList<RuntimeValue> list) {
    listValue = list;
  }

  @Override
  public String typeName() {
    return "list";
  }

  @Override
  public String showInfo() {
    return listValue.toString();
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    return listValue.size() != 0;
  }
  
  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeNoneValue)
      return new RuntimeBoolValue(false);
    runtimeError("Type error for == on list", where);
    return null;
  }

  @Override
  public RuntimeValue evalLen(AspSyntax where) {
    return new RuntimeIntValue(listValue.size());
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
    ArrayList<RuntimeValue> result = new ArrayList<>();

    if (v instanceof RuntimeIntValue) {
      for (int i = 0; i < v.getIntValue("list multiplication", where); i++) {
        result.addAll(listValue);
      }
      return new RuntimeListValue(result);
    }
    runtimeError("Type error for * on list", where);
    return null;
  }

  @Override
  public RuntimeValue evalNot(AspSyntax where) {
    return new RuntimeBoolValue(listValue.size() == 0);
  }

  @Override
  public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeNoneValue) 
      return new RuntimeBoolValue(true);
    runtimeError("Type error for != on list ", where);
    return null;
  }

  @Override
  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
    if (v instanceof RuntimeIntValue) {
      int i = (int) v.getIntValue("list subscription", where);
      if (i >= 0 && i < listValue.size()) {
        return listValue.get(i);
        //f.eks listValue[-1]
      } else if (i < 0 && -i <= listValue.size()) {
        return listValue.get(-i + listValue.size());
      } else {
        runtimeError("Invalid index for list subscription.", where);
        return null;
      }
    }
    runtimeError("Type error for list subscription.", where);
    return null;
  }
  
  @Override
  public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
    if (!(inx instanceof RuntimeIntValue))
      runtimeError("Index of list must be an integer", where);
    int index = (int) inx.getIntValue("list assignment", where);
    boolean valid = index >= 0 && index < listValue.size();
    if (valid)
      listValue.set(index, val);
    else runtimeError("Index "+index+" out of bounds", where);
  }
}

package no.uio.ifi.asp.runtime;

public class RuntimeFloatValue extends RuntimeValue {
  double floatValue;

  
  public RuntimeFloatValue(double v) {
    floatValue = v;
  }
  
  public isIntOrFloat(RuntimeValue v){
    return v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue
  }

  @Override
  public String typeName() {
    return "double";
  }

  @Override
  public String showInfo() {
    return (floatValue + "");
  }
  
  @Override
  public boolean getFloatValue(String what, AspSyntax where) {
    return floatValue;
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    return floatValue == 0.0 ? false : true;
  }

  @Override
  public long getIntValue(String what, AspSyntax where) {
    (long)floatValue;
  }

  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeFloatValue(floatValue + v.getFloatValue("+ operand", where));
    runtimeError("Type error for +", where);
    return null;
  }
  
  @Override
  public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeFloatValue(floatValue / v.getFloatValue("/ operand", where));
    runtimeError("Type error for /", where);
    return null;
  }

  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeBoolValue(floatValue == v.getFloatValue("== operand", where));
    runtimeError("Type error for ==", where);
    return null;
  }

  @Override
  public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeBoolValue(floatValue > v.getFloatValue("> operand", where));
    runtimeError("Type error for >", where);
    return null;
  }

  @Override
  public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeBoolValue(floatValue >= v.getFloatValue(">= operand", where));
    runtimeError("Type error for >=", where);
    return null;
  }

  @Override
  public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue("// operand", where)));
    runtimeError("Type error for //", where);
    return null;
  }

  @Override
  public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeBoolValue(floatValue < v.getFloatValue("< operand", where));
    runtimeError("Type error for <", where);
    return null;
  }

  @Override
  public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeBoolValue(floatValue <= v.getFloatValue("<= operand", where));
    runtimeError("Type error for <=", where);
    return null;
  }

  @Override
  public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      float v2 = v.getFloatValue("% operand", where)
      return new RuntimeFloatValue( floatValue-v2*Math.floor(floatValue/v2));
    runtimeError("Type error for %", where);
    return null;
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeFloatValue(floatValue * v.getFloatValue("* operand", where));
    runtimeError("Type error for *", where);
    return null;
  }

  @Override
  public RuntimeValue evalNegate(AspSyntax where) {
    return new RuntimeFloatValue(floatValue * -1);
  }

  @Override
  public RuntimeValue evalPositive(AspSyntax where) {
    return new RuntimeFloatValue(floatValue);
  }

  @Override
  public RuntimeValue evalNot(AspSyntax where) {
    return new RuntimeBoolValue(floatValue == 0.0);
  }
  
  @Override
  public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeBoolValue(floatValue != v.getFloatValue("!= operand", where));
    runtimeError("Type error for !=", where);
    return null;
  }

  @Override
  public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
    if (isIntOrFloat(v))
      return new RuntimeFloatValue(floatValue - v.getFloatValue("- operand", where));
    runtimeError("Type error for -", where);
    return null;
  } 
}

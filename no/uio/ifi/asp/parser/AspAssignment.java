package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.main.*;


import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

public class AspAssignment extends AspSmallStmt {
  ArrayList<AspSubscription> subs = new ArrayList<>();
  AspName name;
  AspExpr expr;

  public AspAssignment(int i) {
    super(i);
  }

  static AspAssignment parse(Scanner s) {
    enterParser("assignment");
    AspAssignment aa = new AspAssignment(s.curLineNum());
    aa.name = AspName.parse(s);
    while (s.curToken().kind !=equalToken ) {
      aa.subs.add(AspSubscription.parse(s));
    }
    
    skip(s, equalToken);
    aa.expr = AspExpr.parse(s);
    leaveParser("assignment");
    return aa;
  }

  @Override
  void prettyPrint() {
    name.prettyPrint();
    for (AspSubscription as : subs) {
      as.prettyPrint();
    }
    prettyWrite(" = ");
    expr.prettyPrint();
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue exprVal = expr.eval(curScope);
    if (subs.size() == 0) {
      // f.eks global a = 23
      if (curScope.hasGlobalName(name.name)) 
        Main.globalScope.assign(name.name, exprVal);
      // f.eks a = 23
      else curScope.assign(name.name, exprVal);
      trace(name.name + " = " + exprVal.showInfo());
      return null;
    }
    // f.eks myList[2][3][1] = add(1,2)
    String s = "";
    RuntimeValue list = name.eval(curScope);
    RuntimeValue index = subs.get(0).eval(curScope);
    for (int i = 1; i < subs.size(); i++) {
      list = list.evalSubscription(index, this);
      index = subs.get(i).eval(curScope);
      s += "[" + index.showInfo() + "]";
    }
    
    trace(name.name + s + " = " + exprVal.showInfo());
    list.evalAssignElem(index, exprVal, this);
    return null;

    // RuntimeValue exprVal = expr.eval(curScope);
    // if (subs.size() != 0) {
    //     RuntimeValue list = name.eval(curScope);
    //     RuntimeValue index = subs.get(0).eval(curScope);;

    //     for (int i = 1; i < subs.size(); i++) {
    //         list = list.evalSubscription(index, this);
    //         index = subs.get(i).eval(curScope);
    //     }
    //     trace(name.name + "[" + index.showInfo() + "]" + " = " + exprVal.showInfo());
    //     list.evalAssignElem(index, exprVal, this);
    // } else {
    //     curScope.assign(name.name, exprVal);
    //     trace(name.name + " = " + exprVal.showInfo());
    // }
    // return exprVal;
    
  }

}

package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.scanner.Scanner;


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
    while (s.curToken().kind !=equalToken ) 
      aa.subs.add(AspSubscription.parse(s));
    
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
    prettyWrite("=");
    expr.prettyPrint();
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }

}

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;


import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

public class AspArguments extends AspPrimarySuffix {
  ArrayList<AspExpr> exprs = new ArrayList<>();

  public AspArguments(int n) {
    super(n);
  }

  public static AspArguments parse(Scanner s) {
    enterParser("arguments");
    AspArguments aa = new AspArguments(s.curLineNum());
    skip(s, leftParToken);
    while (s.curToken().kind != rightParToken) {
      aa.exprs.add(AspExpr.parse(s));
      if (s.curToken().kind != commaToken)
        break;
      skip(s, commaToken);
    }

    skip(s, rightParToken);
    leaveParser("arguments");
    return aa;
  }

  @Override
  void prettyPrint() {
    int nPrinted = 0;
    prettyWrite("(");
    for (AspExpr ae : exprs) {
      if (nPrinted++ > 0) 
        prettyWrite(", ");
      ae.prettyPrint();
    }
    prettyWrite(")");
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}

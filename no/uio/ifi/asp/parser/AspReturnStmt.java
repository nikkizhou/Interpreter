package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspReturnStmt extends AspSmallStmt {
  AspExpr expr;
    
  AspReturnStmt(int n) {
    super(n);
  }

  public static AspReturnStmt parse(Scanner s) {
    enterParser("return stmt");
    AspReturnStmt ar = new AspReturnStmt(s.curLineNum());
    skip(s, returnToken);
    ar.expr = AspExpr.parse(s);
    leaveParser("return stmt");
    return ar;
  }

  @Override
  public void prettyPrint() {
    prettyWrite("return ");
    expr.prettyPrint();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = expr.eval(curScope);
    trace("return " + v.showInfo());
    throw new RuntimeReturnValue(v, lineNum);
  }
}

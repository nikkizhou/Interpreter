package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

public class AspListDisplay extends AspAtom {
  ArrayList<AspExpr> exprs = new ArrayList<>();

  public AspListDisplay(int n) {
    super(n);
  }

  // brace: {}   bracket: []
  public static AspListDisplay parse(Scanner s) {
    enterParser("list display");
    AspListDisplay ald = new AspListDisplay(s.curLineNum());
    skip(s, leftBracketToken);
    ald.exprs.add(AspExpr.parse(s));
    while (s.curToken().kind != rightBracketToken) {
      if (s.curToken().kind != commaToken)
        break;
      skip(s, commaToken);
      ald.exprs.add(AspExpr.parse(s));
    }

    skip(s, rightBracketToken);
    leaveParser("list display");
    return ald;
  }

  @Override
  void prettyPrint() {
    int nPrinted = 0;
    prettyWrite("[");
    for (AspExpr ae : exprs) {
      if (nPrinted > 0)
        prettyWrite(", ");
      ae.prettyPrint();
      ++nPrinted;
    }
    prettyWrite("]");
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    ArrayList<RuntimeValue> aeList = new ArrayList<>();
    for (AspExpr ae : exprs) {
      aeList.add(ae.eval(curScope));
    }
    return new RuntimeListValue(aeList);
  }
}

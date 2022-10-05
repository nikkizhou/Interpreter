package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspGlobalStmt extends AspSmallStmt {
  ArrayList<AspName> names= new ArrayList<>();
    
  AspGlobalStmt(int n) {
    super(n);
  }

  public static AspGlobalStmt parse(Scanner s) {
    enterParser("global stmt");
    AspGlobalStmt ar = new AspGlobalStmt(s.curLineNum());
    skip(s, globalToken);
    
    while (true) {
      ar.names.add(AspName.parse(s));
      if (s.curToken().kind != commaToken)
        break;
      skip(s, commaToken);
    }
    leaveParser("global stmt");
    return ar;
  }

  @Override
  public void prettyPrint() {
    prettyWrite("global");
    int nPrinted = 0;
    for (AspName an : names) {
      if (nPrinted > 0)
        prettyWrite(", ");
      an.prettyPrint();
      ++nPrinted;
    }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    // -- Must be changed in part 3:
    return null;
  }
}

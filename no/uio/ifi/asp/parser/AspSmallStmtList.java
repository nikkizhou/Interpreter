package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt {
  ArrayList<AspSmallStmt> smallStmts = new ArrayList<>();
  Boolean lastSemiCol = false;

  public AspSmallStmtList(int n) {
    super(n);
  }

  public static AspSmallStmtList parse(Scanner s) {
    enterParser("small stmt list");

    AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());
    assl.smallStmts.add(AspSmallStmt.parse(s));
    while (s.curToken().kind != newLineToken) {
      skip(s, semicolonToken);
      if (s.curToken().kind == newLineToken) 
        break;
      assl.smallStmts.add(AspSmallStmt.parse(s));
    }
    skip(s, newLineToken);

    leaveParser("small stmt list");
    return assl;
  }

  @Override
  public void prettyPrint() {
    smallStmts.get(0).prettyPrint();
    
    for (int i=1; i < smallStmts.size(); i++) {
      prettyWrite("; ");
      smallStmts.get(i).prettyPrint();
    }
    prettyWriteLn();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}

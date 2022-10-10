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
<<<<<<< HEAD
    while (s.curToken().kind != newLineToken) {
      assl.smallStmts.add(AspSmallStmt.parse(s));
      if (s.curToken().kind != semicolonToken)
        break;
      skip(s, semicolonToken);
      if (s.curToken().kind == newLineToken) assl.lastSemiCol = true;
=======
    assl.smallStmts.add(AspSmallStmt.parse(s));
    while (s.curToken().kind != newLineToken) {
      skip(s, semicolonToken);
      if (s.curToken().kind == newLineToken) 
        break;
      assl.smallStmts.add(AspSmallStmt.parse(s));
>>>>>>> 7d74232ec4f312a23d423258c6a6bb1c2c2e6298
    }
    skip(s, newLineToken);

    leaveParser("small stmt list");
    return assl;
  }

  @Override
  public void prettyPrint() {
    smallStmts.get(0).prettyPrint();
<<<<<<< HEAD
    for (int i = 1; i < smallStmts.size(); i++) {
      prettyWrite("; ");
      smallStmts.get(i).prettyPrint();
    }
    if (lastSemiCol) prettyWrite("; ");
=======
    
    for (int i=1; i < smallStmts.size(); i++) {
      prettyWrite("; ");
      smallStmts.get(i).prettyPrint();
    }
>>>>>>> 7d74232ec4f312a23d423258c6a6bb1c2c2e6298
    prettyWriteLn();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}

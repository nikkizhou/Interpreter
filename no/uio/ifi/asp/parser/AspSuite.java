package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSuite extends AspSyntax {
  static ArrayList<AspStmt> stmts = new ArrayList<>();
  static AspSmallStmtList smallSL=null;

  AspSuite(int n) {
    super(n);
  }

  public static AspSuite parse(Scanner s) {
    enterParser("suite");
    AspSuite as = new AspSuite(s.curLineNum());
    
    if (s.curToken().kind !=newLineToken) {
      smallSL = AspSmallStmtList.parse(s);
    } else {
      skip(s, newLineToken);
      skip(s, indentToken);
      stmts.add(AspStmt.parse(s));
      while (s.curToken().kind != dedentToken) {
        stmts.add(AspStmt.parse(s));
      }
      skip(s, dedentToken);
    }

    leaveParser("suite");
    return as;
  }

  @Override
  public void prettyPrint() {
    if (smallSL != null) {
      smallSL.prettyPrint();
    } else {
      prettyWriteLn();
      prettyIndent();
      for (AspStmt as : stmts)  as.prettyPrint();
      prettyDedent();
    }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    // -- Must be changed in part 4:
    return null;
  }
}

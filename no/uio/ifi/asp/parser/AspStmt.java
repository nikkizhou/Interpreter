package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;
import java.util.Arrays;

abstract public class AspStmt extends AspSyntax {
  AspStmt(int i) {
    super(i);
  }

  static AspStmt parse(Scanner s) {
    enterParser("stmt");
    boolean isCompoundStmt = Arrays.asList("if", "while", "for", "def").contains(s.curToken().kind.image);
    AspStmt as = isCompoundStmt ? AspCompoundStmt.parse(s) : AspSmallStmtList.parse(s);
    leaveParser("stmt");
    return as;
  }
}

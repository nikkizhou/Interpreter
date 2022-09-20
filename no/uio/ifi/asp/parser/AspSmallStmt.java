package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;


abstract class AspSmallStmt extends AspSyntax {
  AspSmallStmt(int i) {
    super(i);
  }

  static AspSmallStmt parse(Scanner s) {
    enterParser("small stmt");
    AspSmallStmt ass = null;

    if (s.anyEqualToken()) {
        ass = AspAssignment.parse(s);
    } else if (s.curToken().kind == passToken) {
        ass = AspPassStmt.parse(s);
    } else if (s.curToken().kind == returnToken) {
      ass = AspReturnStmt.parse(s);
    } else if (s.curToken().kind == globalToken) {
      ass = AspGlobalStmt.parse(s);
    } else {
        ass = AspExprStmt.parse(s);
    }
    leaveParser("small stmt");
    return ass;
  }
}

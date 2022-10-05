package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;

abstract public class AspCompoundStmt extends AspStmt {
  AspCompoundStmt(int i) {
    super(i);
  }

  static AspCompoundStmt parse(Scanner s) {
    enterParser("compound stmt");
    AspCompoundStmt acs = null;
    switch (s.curToken().kind) {
      case ifToken:
        acs = AspIfStmt.parse(s);
        break;
      case forToken:
        acs = AspForStmt.parse(s);
        break;
      case defToken:
        acs = AspFuncDef.parse(s);
        break;
      case whileToken:
        acs = AspWhileStmt.parse(s);
        break;
      default:
        parserError("Expected an compound stmt but found a " + s.curToken().kind + "!", s.curLineNum());
    }

    leaveParser("compound stmt");
    return acs;
  }
  
}

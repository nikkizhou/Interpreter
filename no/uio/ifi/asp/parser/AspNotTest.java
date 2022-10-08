package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspNotTest extends AspSyntax {
  AspComparison comparison;
  boolean notTokenExists = false;

  AspNotTest(int n) {
    super(n);
  }

  static AspNotTest parse(Scanner s) {
    enterParser("not test");
    AspNotTest ant = new AspNotTest(s.curLineNum());
    if (s.curToken().kind == notToken){
      ant.notTokenExists = true;
      skip(s, notToken);
    }
    ant.comparison = AspComparison.parse(s);
    leaveParser("not test");
    return ant;
  }

  @Override
  void prettyPrint() {
    if (notTokenExists)
      prettyWrite(" not ");
    comparison.prettyPrint();
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    // -- Must be changed in part 3:
    return null;
  }
}

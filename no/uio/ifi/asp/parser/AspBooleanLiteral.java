package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspBooleanLiteral extends AspAtom {
  boolean boolVal;

  AspBooleanLiteral(int n) {
    super(n);
  }

  static AspBooleanLiteral parse(Scanner s) {
    enterParser("boolean literal");
    AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());

    //make sure it's true or falseToken first
    test(s, trueToken, falseToken);

    abl.boolVal = s.curToken().kind == trueToken;
    skip(s, s.curToken().kind);
    leaveParser("boolean literal");
    return abl;
  }

  @Override
  void prettyPrint() {
    prettyWrite(boolVal ? "True" : "False");
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}

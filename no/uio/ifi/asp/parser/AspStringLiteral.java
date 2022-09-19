package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspStringLiteral extends AspAtom {
  String str;

  AspStringLiteral(int n) {
    super(n);
  }

  static AspStringLiteral parse(Scanner s) {
    enterParser("string literal");
    AspStringLiteral asl = new AspStringLiteral(s.curLineNum());
    asl.str = s.curToken().stringLit;
    skip(s, stringToken);
    leaveParser("string literal");
    return asl;
  }

  @Override
  void prettyPrint() {
    prettyWrite("\"" + str + "\"");
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}

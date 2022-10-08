package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.TokenKind;

import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;

public class AspCompOpr extends AspSyntax{
  TokenKind opr;

  AspCompOpr(int n) {
    super(n);
  }

  TokenKind getOpr() {
    return opr;
  }

  public static AspCompOpr parse(Scanner s) {
    enterParser("comp opr");
    AspCompOpr aco = new AspCompOpr(s.curLineNum());
    aco.opr = s.curToken().kind;
    skip(s, aco.opr);
    leaveParser("comp opr");
    return aco;
  }

  @Override
  void prettyPrint() {
    prettyWrite(" " + opr.toString() + " ");
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
  

  
}

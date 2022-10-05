package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspName extends AspAtom {
  public String name;

  AspName(int n) {
    super(n);
  }

  public static AspName parse(Scanner s){
    enterParser("name");
    AspName an = new AspName(s.curLineNum());
    an.name = s.curToken().name;
    skip(s, TokenKind.nameToken);
    leaveParser("name");
    return an;
  }

  @Override
  void prettyPrint() {
    prettyWrite(name);
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}

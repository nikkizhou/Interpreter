package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspFactorPrefix extends AspSyntax{
  TokenKind prefix;

  AspFactorPrefix(int n) {
    super(n);
  }

  public static AspFactorPrefix parse(Scanner s) {
    enterParser("factor prefix");
    AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());
    afp.prefix = s.curToken().kind;
    if (s.isFactorPrefix())
      skip(s, afp.prefix);
    leaveParser("factor prefix");
    return afp;
  }
  
  @Override
  void prettyPrint() {
    prettyWrite(" " + prefix.toString() + " ");
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
  
}

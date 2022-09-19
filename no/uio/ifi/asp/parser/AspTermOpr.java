package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspTermOpr extends AspSyntax {
  TokenKind opr;

 AspTermOpr(int n){
   super(n);
  }

  public static AspTermOpr parse(Scanner s) {
    enterParser("term opr");
    AspTermOpr ato = new AspTermOpr(s.curLineNum());
    ato.opr = s.curToken().kind;
    if (s.isFactorOpr())  skip(s, ato.opr);
    leaveParser("term opr");
    return ato;
  }

  @Override
  void prettyPrint() {
    System.out.println(" " + opr.toString() + " ");
  }
  
  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }

  
}

package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspTermOpr extends AspSyntax {
  TokenKind opr;

 AspTermOpr(int n){
   super(n);
  }

  public static AspTermOpr parse(Scanner s) {
    enterParser("term opr");
    AspTermOpr ato = new AspTermOpr(s.curLineNum());
    ato.opr = s.curToken().kind;
    if (s.isTermOpr())  skip(s, ato.opr);
    leaveParser("term opr");
    return ato;
  }

  @Override
  void prettyPrint() {
    prettyWrite(opr.toString() + "");
  }
  
  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }

  
}

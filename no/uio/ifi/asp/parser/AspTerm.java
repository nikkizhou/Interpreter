package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.runtime.*;
import java.util.ArrayList;

class AspTerm extends AspSyntax {
  ArrayList<AspFactor> factors = new ArrayList<>();
  ArrayList<AspTermOpr> termOprs = new ArrayList<>();

  AspTerm(int n) {
    super(n);
  }

  static AspTerm parse(Scanner s) {
    enterParser("term");
    AspTerm at = new AspTerm(s.curLineNum());
    while (true) {
      at.factors.add(AspFactor.parse(s));
      if (!s.isTermOpr()) break;
      at.termOprs.add(AspTermOpr.parse(s));
    }

    leaveParser("term");
    return at;
  }

  @Override
  void prettyPrint() {
    for (int i = 0; i < termOprs.size(); i++) {
      factors.get(i).prettyPrint();
      termOprs.get(i).prettyPrint();
    }
    factors.get(factors.size() - 1).prettyPrint();
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}

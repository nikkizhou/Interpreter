package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;


class AspComparison extends AspSyntax {
  ArrayList<AspTerm> terms = new ArrayList<>();
  ArrayList<AspCompOpr> compOprs = new ArrayList<>();

  AspComparison(int n) {
    super(n);
  }

  static AspComparison parse(Scanner s) {
    enterParser("comparison");
    AspComparison comp = new AspComparison(s.curLineNum());
    while (true) {
      comp.terms.add(AspTerm.parse(s));
      if (!s.isCompOpr())
        break;
      comp.compOprs.add(AspCompOpr.parse(s));
    }

    leaveParser("comparison");
    return comp;
  }

  @Override
  void prettyPrint() {
    for (int i = 0; i < compOprs.size(); i++) {
      terms.get(i).prettyPrint();
      compOprs.get(i).prettyPrint();
    }
    terms.get(terms.size() - 1).prettyPrint();
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}

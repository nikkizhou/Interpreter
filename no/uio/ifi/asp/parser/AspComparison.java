package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.main.*;


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
    RuntimeValue v = terms.get(0).eval(curScope);
    //f.eks  the only term is 9+2 and without comopr
    if (terms.size() <= 1)
      return v;
    // for example 7<6==5>8, terms: [7,6,5,8], compOprs: [<,==,>]
    for (int i = 0; i < terms.size() - 1; ++i) {
      // step1: at first the runtime value for v is 7.  and v2 is 6
      v = terms.get(i).eval(curScope);
      RuntimeValue v2 = terms.get(i + 1).eval(curScope);
      // step2: the first k is <
      TokenKind k = compOprs.get(i).opr;
      switch (k) {
        case lessToken:
          // step3: the runtime value for v becomes 7<6
          v = v.evalLess(v2, this);
          break;
        case lessEqualToken:
          v = v.evalLessEqual(v2, this);
          break;
        case greaterToken:
          v = v.evalGreater(v2, this);
          break;
        case greaterEqualToken:
          v = v.evalGreaterEqual(v2, this);
          break;
        case equalToken:
          v = v.evalEqual(v2, this);
          break;
        case notEqualToken:
          v = v.evalNotEqual(v2, this);
          break;
        default:
          Main.panic("Illegal term operator: " + k + "!");
      }
      //7<6 && 6==5 && 5>8, returns false if any one is false 
      if (!v.getBoolValue("comparison", this))
        return new RuntimeBoolValue(false);
    }
    // if all previous terms are true, the final value depends on the last term's boolvalue
    return new RuntimeBoolValue(v.getBoolValue("comparison", this));
  }
}

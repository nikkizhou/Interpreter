package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.main.*;
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

  // code comes from uke-42-utdeling-1.pdf
  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    // for example 5+3-9
    //step1: at first the runtime value for v is 5
    RuntimeValue v = factors.get(0).eval(curScope);
    for (int i = 1; i < factors.size(); ++i) {
      // step2: the first k is +
      // step4: the second k is -
      TokenKind k = termOprs.get(i - 1).opr;
      switch (k) {
        case minusToken:
          //step5: after 8-9 the rumtime value for v becomes -1 and the loop finishes
          v = v.evalSubtract(factors.get(i).eval(curScope), this);
          break;
        case plusToken:
          // step 3: after 5+3 the runtime value for v becomes 8
          v = v.evalAdd(factors.get(i).eval(curScope), this);
          break;
        default:
          Main.panic("Illegal term operator: " + k + "!");
      }
    }
    //step 6: in the end the the rumtime value for v is -1
    return v;
  }

}

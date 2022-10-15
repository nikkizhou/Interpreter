package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspFactor extends AspSyntax {
  ArrayList<AspFactorOpr> factorOprs = new ArrayList<>();
  ArrayList<AspFactorPrefix> factorPrefixes = new ArrayList<>();
  ArrayList<AspPrimary> primarys = new ArrayList<>();

  AspFactor(int n) {
    super(n);
  }

  static AspFactor parse(Scanner s) {
    enterParser("factor");
    AspFactor af = new AspFactor(s.curLineNum());
    while (true) {
      // AspFactorPrefix is optional
      af.factorPrefixes.add(s.isFactorPrefix() ? AspFactorPrefix.parse(s) : null);
      af.primarys.add(AspPrimary.parse(s));
      if (!s.isFactorOpr())
        break;
      af.factorOprs.add(AspFactorOpr.parse(s));
    }

    leaveParser("factor");
    return af;
  }

  @Override
  void prettyPrint() {
    for (int i = 0; i < primarys.size(); i++) {
      if (i > 0)
        factorOprs.get(i - 1).prettyPrint();
      // factor prefix is optional
      if (factorPrefixes.get(i) != null)
        factorPrefixes.get(i).prettyPrint();
      // print one less factorOpr
      primarys.get(i).prettyPrint();

    }
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = primarys.get(0).eval(curScope);
    TokenKind pre1 = factorPrefixes.get(0).prefix;
    v = pre1 == minusToken ? v.evalNegate(this) : v.evalPositive(this);

    //f.eks -2
    if (primarys.size()==1)
     return v;
      
    //f.eks -2*8/3, so primarys:[2,8,3], factorOprs: [*,/], factorPrefixes: [-,null,null]
    for (int i = 1; i < primarys.size(); i++) {
      RuntimeValue v2 = primarys.get(i).eval(curScope);
      TokenKind pre2 = factorPrefixes.get(i).prefix;
      v2 = pre2 == minusToken ? v2.evalNegate(this) : v2.evalPositive(this);
      TokenKind opr = factorOprs.get(i-1).opr;
      
      switch (opr) {
        case astToken:
        v = v.evalMultiply(v2, this); break;
        case slashToken:
        v = v.evalDivide(v2, this); break;
        case doubleSlashToken:
        v = v.evalIntDivide(v2, this); break;
        case percentToken:
        v = v.evalModulo(v2, this); break;
        default:
        Main.panic("Illegal term operator: " + opr + "!");
        break;
      }
      
      v = primarys.get(i).eval(curScope);
      pre1 = factorPrefixes.get(i).prefix;
      v = pre1 == minusToken ? v.evalNegate(this) : v.evalPositive(this);
    }
    return v;
  }
}

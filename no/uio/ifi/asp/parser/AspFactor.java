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
    AspFactorPrefix prefix = factorPrefixes.get(0);
    if (prefix != null) v = handlePrefix(prefix, v);
    
    //f.eks -2
    if (primarys.size() == 1) return v;

    //f.eks -2*8/3, so primarys:[2,8,3], factorOprs: [*,/], factorPrefixes: [-,null,null]
    for (int i = 1; i < primarys.size(); i++) {
      RuntimeValue v2 = primarys.get(i).eval(curScope);
      TokenKind opr = factorOprs.get(i - 1).opr;
      prefix = factorPrefixes.get(i);
      if (prefix != null) v2 = handlePrefix(prefix, v2);

      switch (opr) {
        case astToken:
          v = v.evalMultiply(v2, this);
          break;
        case slashToken:
          v = v.evalDivide(v2, this);
          break;
        case doubleSlashToken:
          v = v.evalIntDivide(v2, this);
          break;
        case percentToken:
          v = v.evalModulo(v2, this);
          break;
        default:
          Main.panic("Illegal term operator: " + opr + "!");
          break;
      }
    }
    return v;
  }
  
  public RuntimeValue handlePrefix(AspFactorPrefix prefix, RuntimeValue v) { 
    TokenKind tk = prefix.prefix;
    if (tk == minusToken) return v.evalNegate(this);
    else if (tk == plusToken) return v.evalPositive(this);
    return v;
  }
}

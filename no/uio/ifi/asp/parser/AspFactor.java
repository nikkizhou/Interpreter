package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;


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
      //factor prefix is optional
      if (factorPrefixes.get(i) != null)
        factorPrefixes.get(i).prettyPrint();
      //print one less factorOpr
      primarys.get(i).prettyPrint();
      
    }
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}

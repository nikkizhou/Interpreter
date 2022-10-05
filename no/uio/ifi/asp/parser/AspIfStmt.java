package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspIfStmt extends AspCompoundStmt {
  ArrayList<AspExpr> exprs = new ArrayList<>();
  ArrayList<AspSuite> suites = new ArrayList<>();
  boolean elseExists = false;
   
  AspIfStmt(int n) {
    super(n);
  }

  static AspIfStmt parse(Scanner s) {
    enterParser("if stmt");
    AspIfStmt ais = new AspIfStmt(s.curLineNum());
    skip(s, ifToken);
    while (true) {
      ais.exprs.add(AspExpr.parse(s));
      skip(s, colonToken);
      ais.suites.add(AspSuite.parse(s));
      if (s.curToken().kind != elifToken)
        break;
      skip(s,elifToken);
    }
    
    if (s.curToken().kind == elseToken) {
      ais.elseExists = true;
      skip(s, elseToken);
      skip(s, colonToken);
      ais.suites.add(AspSuite.parse(s));
    }

    leaveParser("if stmt");
    return ais;
  }

  @Override
  public void prettyPrint() {
    int nPrinted = 0;
    prettyWrite("if");
    for (AspExpr ex : exprs) {
      if (nPrinted > 0)
        prettyWrite("elif ");
      ex.prettyPrint();
      prettyWrite(":");
      suites.get(nPrinted).prettyPrint();
      ++nPrinted;
    }
    
    if (elseExists) {
      prettyWrite("else: ");
      suites.get(suites.size() - 1).prettyPrint();
    }
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    // -- Must be changed in part 3:
    return null;
  }
}

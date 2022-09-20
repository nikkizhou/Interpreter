package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;

public class AspFuncDef extends AspCompoundStmt {
  ArrayList<AspName> names = new ArrayList<>();
  AspSuite suite;
  
  AspFuncDef(int n) {
    super(n);
  }

  static AspFuncDef parse(Scanner s) {
    enterParser("func def");
    AspFuncDef afd = new AspFuncDef(s.curLineNum());
    skip(s, defToken);
    afd.names.add(AspName.parse(s));
    skip(s, leftParToken);
    while (s.curToken().kind != rightParToken) {
      afd.names.add(AspName.parse(s));
      if (s.curToken().kind != commaToken)
        break;
      skip(s, commaToken);
    }
    skip(s, rightParToken);
    skip(s, colonToken);
    afd.suite = AspSuite.parse(s);
    leaveParser("func def");
    return afd;
  }

  @Override
  void prettyPrint() {
    prettyWrite("def ");
    names.get(0).prettyPrint();
    prettyWrite("( ");

    // the first name in names is the one which is there all the time
    if (names.size() == 1)
      return;
    for (int i = 1; i < names.size() - 1; i++) {
      names.get(i).prettyPrint();
      prettyWrite(", ");
    }
    names.get(names.size() - 1).prettyPrint(); 
    prettyWrite(" ): ");
    suite.prettyPrint();
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
  
}

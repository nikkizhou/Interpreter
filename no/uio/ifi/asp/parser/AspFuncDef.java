package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import java.util.ArrayList;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFuncDef extends AspCompoundStmt {
  AspName name;
  ArrayList<AspName> parameters = new ArrayList<>();
  AspSuite suite;

  AspFuncDef(int n) {
    super(n);
  }

  static AspFuncDef parse(Scanner s) {
    enterParser("func def");
    AspFuncDef afd = new AspFuncDef(s.curLineNum());
    skip(s, defToken);
    afd.name = AspName.parse(s);
    skip(s, leftParToken);
    while (s.curToken().kind != rightParToken) {
      afd.parameters.add(AspName.parse(s));
      if (s.curToken().kind != commaToken) {
        break;
      }
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
    int nPrinted = 0;
    prettyWrite("def ");
    this.name.prettyPrint();
    prettyWrite(" (");
    for (AspName an : this.parameters) {
      if (nPrinted++ > 0) {
        prettyWrite(", ");
      }
      an.prettyPrint();
    }
    prettyWrite("): ");
    this.suite.prettyPrint();
    prettyWriteLn();
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue value = new RuntimeFunc(name.name, parameters, suite, curScope);
    curScope.assign(name.name, value);
    trace("def " + name.name);
    return null;
  }
}

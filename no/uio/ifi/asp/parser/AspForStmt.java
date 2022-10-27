package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;

import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;

public class AspForStmt extends AspCompoundStmt {
  AspName name;
  AspExpr expr;
  AspSuite suite;
  
  AspForStmt(int n) {
    super(n);
  }

  static AspForStmt parse(Scanner s) {
    enterParser("for stmt");
    AspForStmt afs = new AspForStmt(s.curLineNum());
    skip(s, forToken);
    afs.name = AspName.parse(s);
    skip(s, inToken);
    afs.expr = AspExpr.parse(s);
    skip(s, colonToken);
    afs.suite = AspSuite.parse(s);
    leaveParser("for stmt");
    return afs;
  }

  @Override
  void prettyPrint() {
    prettyWrite("for ");
    name.prettyPrint();
    prettyWrite(" in ");
    expr.prettyPrint();
    prettyWrite(": ");
    suite.prettyPrint();
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue list = expr.eval(curScope);
    long size = list.evalLen(this).getIntValue("for loop", this);

    // f.eks for letter in myList (['a','b','c'])
    for (long i = 0; i < size; i++) {
      // value is myList[0], dvs 'a'
      RuntimeValue value = list.evalSubscription(new RuntimeIntValue(i), this);
      // assign: letter = 'a'
      curScope.assign(name.toString(), value);
      suite.eval(curScope);
      trace("for loop " + (i + 1) + " : " + name.toString() + " = " + value.showInfo());
    }
    
    return null;
  }
  
}

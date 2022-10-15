package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspAndTest extends AspSyntax {
  ArrayList<AspNotTest> notTests = new ArrayList<>();

  AspAndTest(int n) {
    super(n);
  }

  static AspAndTest parse(Scanner s) {
    enterParser("and test");
    AspAndTest aat = new AspAndTest(s.curLineNum());
    while (true) {
      aat.notTests.add(AspNotTest.parse(s));
      if (s.curToken().kind != andToken) break;
      skip(s, andToken);
    }

    leaveParser("and test");
    return aat;
  }

  @Override
  void prettyPrint() {
    int nPrinted = 0;
    for (AspNotTest ant : notTests) {
      if (nPrinted > 0)
        prettyWrite(" and ");
      ant.prettyPrint();
      ++nPrinted;
    }
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    // for example 5<3 and !6>=4, so notTests is [5<3, !6>=4]
    // step1: at first the value for v is 5<3, dvs false
    RuntimeValue v = notTests.get(0).eval(curScope);
    for (int i = 1; i < notTests.size(); ++i) {
      // since and would return the first false value, therefore should we always check if v.getBoolValue is false
      // stpe2: since 5<3 is false, so the runtimevalue of v is 5<3, and then v is returned.
      if (!v.getBoolValue("and operand", this))
        return v;
      v = notTests.get(i).eval(curScope);
    }
    // if all the <not test> has the boolvalue true, return the last <not test> value
    return v;
  }
}

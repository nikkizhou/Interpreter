package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;
import java.util.HashMap;

public class AspDictDisplay extends AspAtom {
  ArrayList<AspStringLiteral> stringlits = new ArrayList<>();
  ArrayList<AspExpr> exprs = new ArrayList<>();

  public AspDictDisplay(int n) {
    super(n);
  }

  public static AspDictDisplay parse(Scanner s) {
    enterParser("dict display");
    AspDictDisplay add = new AspDictDisplay(s.curLineNum());
    skip(s, leftBraceToken);
    while (s.curToken().kind != rightBraceToken) {
      add.stringlits.add(AspStringLiteral.parse(s));
      skip(s, colonToken);
      add.exprs.add(AspExpr.parse(s));
      if (s.curToken().kind != commaToken)
        break;
      skip(s, commaToken);
    }
    skip(s, rightBraceToken);
    leaveParser("dict display");
    return add;
  }

  @Override
  void prettyPrint() {
    prettyWrite("{");
    for (int i = 0; i < stringlits.size(); i++) {
      if (i > 0) 
        prettyWrite(", ");
      stringlits.get(i).prettyPrint();
      prettyWrite(": ");
      exprs.get(i).prettyPrint();
    }
    prettyWrite("}");
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    HashMap<String, RuntimeValue> dict = new HashMap<>();
    for (int i = 0; i < stringlits.size(); i++) {
      String key = stringlits.get(i).str;
      RuntimeValue value = exprs.get(i).eval(curScope);
      dict.put(key, value);
    }
    return new RuntimeDictValue(dict);
  }
}

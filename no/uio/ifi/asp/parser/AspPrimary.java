package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;


public class AspPrimary extends AspSyntax{
  AspAtom atom;
  ArrayList<AspPrimarySuffix> pSuffixes = new ArrayList<>();
  
  public AspPrimary(int i) {
    super(i);
  }
  
  static AspPrimary parse(Scanner s){
    enterParser("primary");
    AspPrimary ap = new AspPrimary(s.curLineNum());
    ap.atom = AspAtom.parse(s);
    while (s.curToken().kind == leftParToken || s.curToken().kind == leftBracketToken) {
      ap.pSuffixes.add(AspPrimarySuffix.parse(s));
    }
    leaveParser("primary");
    return ap;
  }

  @Override
  void prettyPrint() {
    atom.prettyPrint();
    for (AspPrimarySuffix aps : pSuffixes) {
      aps.prettyPrint();
    }
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = atom.eval(curScope);
    // f.ks {"Ja":17, "Nei":22}["Ja"]
    for (AspPrimarySuffix suffix : pSuffixes) {
      if (suffix instanceof AspSubscription)
        v = v.evalSubscription(suffix.eval(curScope), this);
      else {
        RuntimeValue arguments = suffix.eval(curScope);
        ArrayList<RuntimeValue> argumentsList = ((RuntimeListValue) arguments).listValue;
        String arguementsStr = "";
        for (RuntimeValue value : argumentsList) {
          arguementsStr += value.toString()+",";
        }
        // if no param, then don't show 'with params'
        String params = arguementsStr.length()!=0 ? " with params " + arguementsStr : "";
        Main.log.traceEval("Call " + v.toString() + params,this);
        v = v.evalFuncCall(argumentsList, this);
      }
    }
    return v;
  }
  
}

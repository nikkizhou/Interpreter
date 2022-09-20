package no.uio.ifi.asp.parser;

import java.util.ArrayList;
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
    return null;
  }
  
}

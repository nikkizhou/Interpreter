package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import no.uio.ifi.asp.runtime.*;
import java.util.ArrayList;


public class AspSmallStmtList extends AspStmt{
  static ArrayList<AspSmallStmt> smallStmts = new ArrayList<>();
  static boolean lastIsSemi = true;

  public AspSmallStmtList(int n) {
    super(n);
  }

  //??? er det riktig?
  public static AspSmallStmtList parse(Scanner s){
    enterParser("small stmt list");
    AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());

    while (s.curToken().kind!=newLineToken) {
      smallStmts.add(AspSmallStmt.parse(s));
      
      if (s.curToken().kind!=semicolonToken) {
        lastIsSemi =false;
        break;
      }
      skip(s, semicolonToken);
      
    }
    skip(s, newLineToken);
    leaveParser("small stmt list");
    return assl;
  }

  @Override
  void prettyPrint() {
    int nPrinted = 0;
    for (AspSmallStmt ass : smallStmts) {
      if (nPrinted > 0) prettyWrite("; ");
      ass.prettyPrint();
      ++nPrinted;
    }
    if(lastIsSemi) prettyWrite("; ");
    prettyWriteLn();
  }

  @Override
  RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
  
}

package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract public class AspPrimarySuffix extends AspSyntax{
  AspPrimarySuffix(int i) {
    super(i);
  }
  
  static AspPrimarySuffix parse(Scanner s) {
    enterParser("primary suffix");
    AspPrimarySuffix aps = null;
    switch (s.curToken().kind) {
      case leftParToken:
        aps = AspArguments.parse(s);  break;
      case leftBracketToken:
        aps = AspSubscription.parse(s); break;
      default:
        test(s, leftBracketToken, leftParToken);
    }
    leaveParser("primary suffix");
    return aps;
  }
}

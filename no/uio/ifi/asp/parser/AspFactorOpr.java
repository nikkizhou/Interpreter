package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;


public class AspFactorOpr extends AspSyntax {
  
    TokenKind opr;

    AspFactorOpr(int n) {
        super(n);
    }

    TokenKind getOpr() {
        return opr;
    }

    public static AspFactorOpr parse(Scanner s) {
        enterParser("factor opr");
        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());
        afo.opr = s.curToken().kind;
        if (s.isFactorOpr())
            skip(s, afo.opr);
            
        leaveParser("factor opr");
        return afo;
    }

    @Override
    void prettyPrint() {
        prettyWrite(" " + opr.toString() + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;   
    }
}
  

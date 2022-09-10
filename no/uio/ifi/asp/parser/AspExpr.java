// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExpr extends AspSyntax {
    ArrayList<AspAndTest> andTests = new ArrayList<>();
    
    AspExpr(int n) {
        super(n);
    }

    public static AspExpr parse(Scanner s) {
        enterParser("expr");

        AspExpr ae = new AspExpr(s.curLineNum());
        while (true) {
            ae.andTests.add(AspAndTest.parse(s));
            if (s.curToken().kind != orToken)
                break;
            skip(s, orToken);
        }

        leaveParser("expr");
        return ae;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        for (AspAndTest ant : andTests) {
            if (nPrinted > 0)
                prettyWrite(" or ");
            ant.prettyPrint();
            ++nPrinted;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- Must be changed in part 3:
        return null;
    }
}

// ---------------------------------------------------------------------

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
        RuntimeValue v = notTests.get(0).eval(curScope);
        for (int i = 1; i < notTests.size(); ++i) {
            if (!v.getBoolValue("and operand", this))
                return v;
            v = notTests.get(i).eval(curScope);
        }
        return v;
    }
}



class AspNotTest extends AspSyntax {
    AspComparison comparison;
    static boolean notTokenExists = false;

    AspNotTest(int n) {
        super(n);
    }

    static AspNotTest parse(Scanner s) {
        enterParser("not test");
        AspNotTest ant = new AspNotTest(s.curLineNum());
        if (s.curToken().kind == notToken)
            notTokenExists = true;
        ant.comparison = AspComparison.parse(s);

        leaveParser("and test");
        return ant;
    }

    @Override
    void prettyPrint() {
        if (notTokenExists)
            prettyWrite(" not ");
        comparison.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- Must be changed in part 3:
        return null;
    }
}



// hvordan endre det til abstract?
// hva er meningen med skip? if (!s.isCompOpr()) break; sørker for at det må være comOpr allerede
class AspComparison extends AspSyntax {
    ArrayList<AspTerm> terms = new ArrayList<>();
    ArrayList<AspCompOpr> compOprs = new ArrayList<>();

    AspComparison(int n) {
        super(n);
    }

    static AspComparison parse(Scanner s) {
        enterParser("comparison");
        AspComparison comp = new AspComparison(s.curLineNum());
        while (true) {
            comp.terms.add(AspTerm.parse(s));
            if (!s.isCompOpr())
                break;
            comp.compOprs.add(AspCompOpr.parse(s));
            String[] compOprImages = new String[] { "<", "<=", ">", ">=", "!=", "==" };
            skip(s, compOprImages);
        }

        leaveParser("comparison");
        return comp;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < compOprs.size(); i++) {
            terms.get(i).prettyPrint();
            compOprs.get(i).prettyPrint();
        }
        terms.get(terms.size() - 1).prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}


class AspTerm extends AspSyntax {
    ArrayList<AspFactor> factors = new ArrayList<>();
    ArrayList<AspTermOpr> termOprs = new ArrayList<>();

    AspTerm(int n) {
        super(n);
    }

    static AspTerm parse(Scanner s) {
        enterParser("term");
        AspTerm at = new AspTerm(s.curLineNum());
        while (true) {
            at.factors.add(AspFactor.parse(s));
            if (!s.isTermOpr())
                break;
            at.termOprs.add(AspTermOpr.parse(s));
            skip(s, new String[] { "+", "-" });
        }

        leaveParser("term");
        return at;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < termOprs.size(); i++) {
            factors.get(i).prettyPrint();
            termOprs.get(i).prettyPrint();
        }
        factors.get(factors.size() - 1).prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}


// er det riktig???
class AspCompOpr extends AspSyntax {

    AspCompOpr(int n) {
        super(n);
    }

    static AspCompOpr parse(Scanner s) {
        enterParser("com opr");
        AspCompOpr aco = new AspCompOpr(s.curLineNum());
        leaveParser("com opr");
        return aco;
    }

    @Override
    void prettyPrint() {
        //????????
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}



class AspTermOpr extends AspSyntax {

    AspTermOpr(int n) {
        super(n);
    }

    static AspTermOpr parse(Scanner s) {
        enterParser("term opr");
        AspTermOpr ato = new AspTermOpr(s.curLineNum());
        leaveParser("term opr");
        return ato;
    }

    @Override
    void prettyPrint() {
        // ????????
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}



class AspFactor extends AspSyntax {
    ArrayList<AspFactorOpr> factorOprs = new ArrayList<>();
    ArrayList<AspFactorPrefix> factorePrefixes = new ArrayList<>();
    ArrayList<AspPrimary> primarys = new ArrayList<>();

    AspFactor(int n) {super(n);}

    static AspFactor parse(Scanner s) {
        enterParser("factor");
        AspFactor af = new AspFactor(s.curLineNum());
        while (true) {
            af.factorOprs.add(AspFactor.parse(s));
            if (!s.isTermOpr())
                break;
            af.factorePrefixes.add(AspTermOpr.parse(s));
            skip(s, new String[] { "+", "-" });
        }

        leaveParser("factor");
        return af;
    }

    @Override
    void prettyPrint() {
        int nPrinted = 0;
        for (int i = 0; i < termOprs.length; i++) {
            factors.get(i).prettyPrint();
            termOprs.get(i).prettyPrint();
        }
        factors.get(terms.size() - 1).prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}













//     abstract class AspAtom extends AspSyntax {
//         static AspAtom parse(Scanner s) {
//             AspAtom aa = null;
//             switch (s.curToken().kind) {
//                 case falseToken:
//                 case trueToken:
//                     aa = AspBooleanLiteral.parse(s);
//                     break;
//                 case floatToken:
//                     aa = AspFloatLiteral.parse(s);
//                     break;
//                 case integerToken:
//                     aa = AspIntegerLiteral.parse(s);
//                     break;
//                 case leftBraceToken:
//                     aa = AspDictDisplay.parse(s);
//                     break;
//                 case leftBracketToken:
//                     aa = AspListDisplay.parse(s);
//                     break;
//                 case leftParToken:
//                     aa = AspInnerExpr.parse(s);
//                     break;
//                 case nameToken:
//                     aa = AspName.parse(s);
//                     break;
//                 case noneToken:
//                     aa = AspNoneLiteral.parse(s);
//                     break;
//                 case stringToken:
//                     aa = AspStringLiteral.parse(s);
//                     break;
//                 default:
//                     parserError("Expected an expression atom but found a " +
//                             s.curToken().kind + "!", s.curLineNum());
//             }
//             return aa;
//         }

// static AspWhileStmt parse(Scanner s) {
//     enterParser("while stmt");
//     AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
//     skip(s, whileToken);
//     aws.test = AspExpr.parse(s);
//     skip(s, colonToken);
//     aws.body = AspSuite.parse(s);
//     leaveParser("while stmt");
//     return aws;
// }

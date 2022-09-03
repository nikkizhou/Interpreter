// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Token {
	public TokenKind kind;
	// public String name, stringLit;
	// public long integerLit;
	// public double floatLit;
	public Object literal;
	public int lineNum;

	Token(TokenKind k) {
		this(k, 0,null);
	}

	Token(TokenKind k, int lNum, Object lit) {
		kind = k;
		lineNum = lNum;
		literal = lit;
	}

	void checkResWords() {
		if (kind != nameToken)
			return;

		for (TokenKind tk : EnumSet.range(andToken, yieldToken)) {
			if (literal.equals(tk.image)) {
				kind = tk;
				break;
			}
		}
	}

	public String showInfo() {
		String t = kind + " token";
		if (lineNum > 0) {
			t += " on line " + lineNum;
		}

		switch (kind) {
			case floatToken:
				t += ": " + literal;
				break;
			case integerToken:
				t += ": " + literal;
				break;
			case nameToken:
				t += ": " + literal;
				break;
			case stringToken:
				t += ": '" + literal + "'";


				// if (literal.indexOf('"') >= 0)
				// 	t += ": '" + literal + "'";
				// else
				// 	t += ": " + '"' + literal + '"';
				break;
		}
		return t;
	}

	@Override
	public String toString() {
		return kind.toString();
	}
}

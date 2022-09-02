// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
	private LineNumberReader sourceFile = null;
	private String curFileName;
	private ArrayList<Token> curLineTokens = new ArrayList<>();
	private Stack<Integer> indents = new Stack<>();
	private final int TABDIST = 4;

	public Scanner(String fileName) {
		curFileName = fileName;
		indents.push(0);
		
		try {
			sourceFile = new LineNumberReader(
					new InputStreamReader(
							new FileInputStream(fileName),
							"UTF-8"));
		} catch (IOException e) {
			scannerError("Cannot read " + fileName + "!");
		}
	}

	private void scannerError(String message) {
		String m = "Asp scanner error";
		if (curLineNum() > 0)
			m += " on line " + curLineNum();
		m += ": " + message;

		Main.error(m);
	}

	//return the first token in curLineTokens
	public Token curToken() {
		// if current line is empty, jump to next line
		while (curLineTokens.isEmpty()) {
			readNextLine();
		}
		return curLineTokens.get(0);
	}

	// remove the first token in curLineTokens
	public void readNextToken() {
		if (!curLineTokens.isEmpty())
			curLineTokens.remove(0);
	}

	// devide next line into tokens and add dem to curLineTokens
  //Denne metoden er privat og kalles bare fra curToken.
	private void readNextLine() {
		curLineTokens.clear();

		String line = null;
		try {
			if (sourceFile != null) {
				line = sourceFile.readLine();
			}
			
			if (line == null) {
				sourceFile.close();
				sourceFile = null;
				// if the line is not empty, log it out
			} else {
				Main.log.noteSourceLine(curLineNum(), line);
			}
		} catch (IOException e) {
			sourceFile = null;
			scannerError("Unspecified I/O error!");
		}

		// -- Must be changed in part 1:
		if (line != null) {
			line = expandLeadingTabs(line);
			int n = findIndent(line);

			if (line.length() == n || line.charAt(0) == '#')
				return;

			if (n > indents.peek()) {
				indents.push(n);
				curLineTokens.add(new Token(indentToken, curLineNum()));
			}

			while (n < indents.peek()) {
				indents.pop();
				curLineTokens.add(new Token(dedentToken, curLineNum()));
			}

			if (n != indents.peek())
				scannerError("Expected indents number: " + indents.peek() + ", but got: " + n);

			for (int value : indents) {
				if (value > 0)
					curLineTokens.add(new Token(dedentToken, curLineNum()));
			}

			// Terminate line:
			curLineTokens.add(new Token(newLineToken, curLineNum()));

			for (Token t : curLineTokens)
				Main.log.noteToken(t);
		}
		

		//??? Om nødvendig, kaller curToken på readNextLine for å få lest inn flere linjer.
	}
	

	public int curLineNum() {
		return sourceFile != null ? sourceFile.getLineNumber() : 0;
	}

	private int findIndent(String s) {
		int indent = 0;
		while (indent < s.length() && s.charAt(indent) == ' ') indent++;
		return indent;
	}

	// Denne metoden er privat og kalles bare fra readNextLine.
	private String expandLeadingTabs(String s) {
		// -- Must be changed in part 1:
		int n = 0;
		StringBuilder linjeBuf = new StringBuilder(s);
		List<Character> tegner = Arrays.asList(' ', '\t');

		while (linjeBuf.length()!=0 && n<linjeBuf.length()&&tegner.contains(linjeBuf.charAt(n))) {
			if (linjeBuf.charAt(0) == ' ') {
				n++;
			} else {
				linjeBuf.replace(n,n+1, " ".repeat(TABDIST - n % TABDIST));
				n += TABDIST - n % TABDIST;
			}
		}
		return linjeBuf.toString();
	}

	private boolean isLetterAZ(char c) {
		return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || (c == '_');
	}

	private boolean isDigit(char c) {
		return '0' <= c && c <= '9';
	}

	public boolean isCompOpr(String s) {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		//return k == doubleEqualToken;
		return false;
	}

	public boolean isFactorPrefix() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		return false;
	}

	public boolean isFactorOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		return false;
	}

	public boolean isTermOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2:
		return false;
	}

	public boolean anyEqualToken() {
		for (Token t : curLineTokens) {
			if (t.kind == equalToken)
				return true;
			if (t.kind == semicolonToken)
				return false;
		}
		return false;
	}
}

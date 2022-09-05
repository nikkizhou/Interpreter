//Must be changed in part 2: er det for del2?
// Token.java, er det greit å kombinere alle literal til en??
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

		//try opening the file
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

	// return the first token in curLineTokens
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
	// Denne metoden er privat og kalles bare fra curToken.
	private void readNextLine() {
		curLineTokens.clear();

		String line = null;
		try {
			if (sourceFile != null) {
				line = sourceFile.readLine();
				//after last line, add corresponding dedentToken to curLineTokens based on indents
				if (line == null) {
					// System.out.println(indents + " indents");
					for (int value : indents) {
						if (value > 0)
							curLineTokens.add(new Token(dedentToken));
					}
					// System.out.println(curLineTokens+ "curLineTokens");
					curLineTokens.add(new Token(eofToken));
					sourceFile.close();
					sourceFile = null;

				} else {
					Main.log.noteSourceLine(curLineNum(), line);

					//if the line is blank
					boolean erBlank = line.trim().isEmpty() || line.charAt(0) == '#';
					if (erBlank)
						return;

					//if the line includes indent or dedent
					handelIndentToken(line);

					// handle other tokens
					int current = 0;
					while (current < line.trim().length()) {
						handleOprTokens(line.trim(), current);
						current = handleNameLitTokens(line.trim(), current);
						//System.out.println("current in while loop 96: " + current);
						current++;
					}

					addToken(newLineToken, null);
				}

				for (Token t : curLineTokens)
					Main.log.noteToken(t);
			}

		} catch (IOException e) {
			sourceFile = null;
			scannerError("Unspecified I/O error!");
		}

		// -- Must be changed in part 1:
		// ??? Om nødvendig, kaller curToken på readNextLine for å få lest inn flere
		// linjer.
	}

	public void handelIndentToken(String line) {
		line = expandLeadingTabs(line);
		int n = findIndent(line);
		if (n > indents.peek()) {
			indents.push(n);
			addToken(indentToken, null);
		}
		while (n < indents.peek()) {
			indents.pop();
			addToken(dedentToken, null);
		}
		if (n != indents.peek())
			scannerError("Expected indents number: " + indents.peek() + ", but got: " + n);
	}

	public void handleOprTokens(String line, int i) {
		String curChar = "" + line.charAt(i);
		String nextChar = i < line.length() - 1 ? "" + line.charAt(i + 1) : "";
		String lastChar = i > 0 ? "" + line.charAt(i - 1) : "";

		for (TokenKind tk : EnumSet.range(astToken, semicolonToken)) {
			if (curChar.equals(tk.image)) {
				switch (curChar) {
					case "=":
						boolean secondSymbol = Arrays.asList("=", "!", "<", ">").contains(lastChar);
						if (!secondSymbol) {
							addToken(nextChar.equals("=") ? doubleEqualToken : equalToken, null);
						}
						break;
					case "/":
						if (!lastChar.equals("/")) {
							addToken(nextChar.equals("/") ? doubleSlashToken : slashToken, null);
						}
						break;
					case ">":
						addToken(nextChar.equals("=") ? greaterEqualToken : greaterToken, null);
						break;
					case "<":
						addToken(nextChar.equals("=") ? lessEqualToken : lessToken, null);
						break;
					case "!":
						addToken(nextChar.equals("=") ? notEqualToken : null, null);
						break;
					default:
						addToken(tk, null);
						break;
				}
			}
		}
	}

	// isLastIndex(i) return i line.length()
	public int handleNameLitTokens(String line, int current) {
		//char nextChar = i < line.length() - 1 ? line.charAt(i + 1) : ' ';
		int start = current;
		TokenKind kind = null;
		String value = null;
		current++;
		
		//handle name literal
		if (isLetterAZ(line.charAt(start))) {
			while (current < line.length() && (isLetterAZ(line.charAt(current))||isDigit(line.charAt(current)))) {
				current++;
			}

			value = line.substring(start, current);
			// if it's sd45sd$dd, how to fix???
			for (TokenKind tk : EnumSet.range(andToken, yieldToken)) {
				boolean isKeyWord = value != null && value.equals(tk.image);
				if (!isKeyWord) {
					kind = nameToken;
				} else {
					addToken(tk, null);
					return current - 1;
					// kind = tk;
					// value = null;
				}
			}

			// handle integer and float literal
		} else if (isDigit(line.charAt(start))) {
			while (current < line.length() && (isDigit(line.charAt(current)) || line.charAt(current) == '.')) {
				current++;
			}
			value = line.substring(start, current);
			// !!! if . is the last digit in value, throw error
			// if its 34kdsad, throw error
			kind = value.contains(".") ? floatToken : integerToken;


			//handle string literal
		} else if (Arrays.asList('"', '\'').contains(line.charAt(start))) {
			
			while (line.charAt(start)!= line.charAt(current-1) && current < line.length()-1) {
				current++;
			}
			value = line.substring(start + 1, current);
			// !!! if value contains \n and it's not in the end, throw error
			kind = stringToken;
		}
		
		addToken(kind, value);
		return current-1;

		// String element = line.substring(start, i);
		// TokenKind kind = keywords.get(element);
		// if (kind == null)
		// kind = nameToken;
		// addToken(kind);
	}


	public void addToken(TokenKind kind, String value) {
		if (kind != null)
			curLineTokens.add(new Token(kind, curLineNum(), value));
	}

	public int curLineNum() {
		return sourceFile != null ? sourceFile.getLineNumber() : 0;
	}

	private int findIndent(String s) {
		int indent = 0;
		while (indent < s.length() && s.charAt(indent) == ' ')
			indent++;
		return indent;
	}

	// Denne metoden er privat og kalles bare fra readNextLine.
	private String expandLeadingTabs(String line) {
		// -- Must be changed in part 1:
		int n = 0;
		StringBuilder linjeBuf = new StringBuilder(line);
		List<Character> tegner = Arrays.asList(' ', '\t');

		while (n < linjeBuf.length() && tegner.contains(linjeBuf.charAt(n))) {
			if (linjeBuf.charAt(0) == ' ') {
				n++;
			} else {
				linjeBuf.replace(n, n + 1, " ".repeat(TABDIST - n % TABDIST));
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

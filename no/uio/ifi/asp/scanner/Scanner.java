package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.Arrays;

public class Scanner {
	private LineNumberReader sourceFile = null;
	private String curFileName;
	private ArrayList<Token> curLineTokens = new ArrayList<>();
	private Stack<Integer> indents = new Stack<>();
	private final int TABDIST = 4;

	public Scanner(String fileName) {
		curFileName = fileName;
		indents.push(0);

		// try opening the file
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

	public String removeComment(String line) {
		int offset = line.indexOf("#");

		if (offset != -1) {
			line = line.substring(0, offset);
		}
		return line;
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
			scannerError("\nExpected indents number: " + indents.peek() + ", but got: " + n);
	}

	private void readNextLine() {
		curLineTokens.clear();

		// Read the next line:
		String line = null;
		try {
			line = sourceFile.readLine();
			if (line == null) {
				sourceFile.close();
				sourceFile = null;
			} else {
				Main.log.noteSourceLine(curLineNum(), line);
			}
		} catch (IOException e) {
			sourceFile = null;
			scannerError("Unspecified I/O error!");
		}

		// case 1: finished reading all lines
		if (line == null) {
			for (int value : indents) {
				if (value > 0)
					curLineTokens.add(new Token(dedentToken));
			}
			curLineTokens.add(new Token(eofToken));

			// case 2: the line is empty or comment
		} else if (line.trim().isEmpty() || line.charAt(0) == '#') {
			return;

			// case 3: the line is valid
		}else {
			// 3-1: check indent and dedent tokens
			handelIndentToken(line);
			// 3-2: handle other tokens
			int current = 0;
			while (current < removeComment(line).trim().length()) {
				handleOprTokens(line.trim(), current);
				current = handleNameLitTokens(line.trim(), current);
				current++;
			}
			addToken(newLineToken, null);
		}

		for (Token t : curLineTokens)
			Main.log.noteToken(t);
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

	public int handleNameLitTokens(String line, int current) {
		int start = current;
		TokenKind kind = null;
		String value = null;
		current++;

		// case1: handle name literal
		if (isLetterAZ(line.charAt(start))) {
			while (current < line.length() && (isLetterAZ(line.charAt(current)) || isDigit(line.charAt(current)))) {
				current++;
			}

			value = line.substring(start, current);
			for (TokenKind tk : EnumSet.range(andToken, yieldToken)) {
				boolean isKeyWord = value != null && value.equals(tk.image);
				if (!isKeyWord) {
					kind = nameToken;
				} else {
					addToken(tk, null);
					return current - 1;
				}
			}

			//case2: handle integer and float literal
		} else if (isDigit(line.charAt(start))) {
			while (current < line.length() && (isDigit(line.charAt(current)) || line.charAt(current) == '.')) {
				current++;
			}
			value = line.substring(start, current);

			boolean startWithZero = value.startsWith("0") && value.length() > 1;
			if (value.endsWith(".") || startWithZero)
				scannerError("\nInvalid number: " + value);
			kind = value.contains(".") ? floatToken : integerToken;


			//case3: handle string literal
		} else if (Arrays.asList('"', '\'').contains(line.charAt(start))) {
			while (current < line.length() - 1 && line.charAt(start) != line.charAt(current)) {
				current++;
			}
			if (line.charAt(current) != line.charAt(start)) {
				scannerError("\nLeft quote is provided, but right quote is missing in the same line");
			}
			value = line.substring(start + 1, current);
			kind = stringToken;
			current++;

		} else if (Arrays.asList('.', '$', '£', '@').contains(line.charAt(start))) {
			scannerError("\nThe symbol " + line.charAt(start) + " here is not valid");
		}

		addToken(kind, value);
		return current - 1;
	}

	public void addToken(TokenKind kind, String value) {
		if (kind != null) {
			Token t = new Token(kind, curLineNum());
			switch (kind) {
				case nameToken:
					t.name = value;
					break;
				case integerToken:
					t.integerLit = Integer.parseInt(value);
					break;
				case floatToken:
					t.floatLit = Float.parseFloat(value);
					break;
				case stringToken:
					t.stringLit = value;
					break;
				default:
					break;
			}
			curLineTokens.add(t);
		}
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

	private String expandLeadingTabs(String line) {
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

	public boolean isCompOpr() {
		TokenKind k = curToken().kind;
		return Arrays.asList("<", "<=",">",">=","!=","==").contains(k.image);
	}

	public boolean isFactorPrefix() {
		TokenKind k = curToken().kind;
		return Arrays.asList("+", "-").contains(k.image);
	}

	public boolean isFactorOpr() {
		TokenKind k = curToken().kind;
		return Arrays.asList("*", "/", "//", "%").contains(k.image);
	}

	public boolean isTermOpr() {
		TokenKind k = curToken().kind;
		return Arrays.asList("+", "-").contains(k.image);
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

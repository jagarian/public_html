package neo.util.comm;

import neo.util.comm.StringHelper;
import neo.util.log.Log;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 	@Class Name	: 	Formatter.java
 * 	@파일설명		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	작업일 		버젼	구분	작업자		내용
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기)
 *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스)
 *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티)
 *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅)
 **********************************************************************************************             
 */
public class Formatter {
	private static final Set BEGIN_CLAUSES = new HashSet();
	private static final Set END_CLAUSES = new HashSet();
	private static final Set LOGICAL = new HashSet();
	private static final Set QUANTIFIERS = new HashSet();
	private static final Set DML = new HashSet();
	private static final Set MISC = new HashSet();

	static {
		BEGIN_CLAUSES.add("left");
		BEGIN_CLAUSES.add("right");
		BEGIN_CLAUSES.add("inner");
		BEGIN_CLAUSES.add("outer");
		BEGIN_CLAUSES.add("group");
		BEGIN_CLAUSES.add("order");
		END_CLAUSES.add("where");
		END_CLAUSES.add("set");
		END_CLAUSES.add("having");
		END_CLAUSES.add("join");
		END_CLAUSES.add("from");
		END_CLAUSES.add("by");
		END_CLAUSES.add("join");
		END_CLAUSES.add("into");
		END_CLAUSES.add("union");
		LOGICAL.add("and");
		LOGICAL.add("or");
		LOGICAL.add("when");
		LOGICAL.add("else");
		LOGICAL.add("end");
		QUANTIFIERS.add("in");
		QUANTIFIERS.add("all");
		QUANTIFIERS.add("exists");
		QUANTIFIERS.add("some");
		QUANTIFIERS.add("any");
		DML.add("insert");
		DML.add("update");
		DML.add("delete");
		MISC.add("select");
		MISC.add("on");
		//MISC.add("values");
	}

	String indentString = "    ";
	String initial = "\n    ";
	boolean beginLine = true;
	boolean afterBeginBeforeEnd = false;
	boolean afterByOrSetOrFromOrSelect = false;
	boolean afterValues = false;
	boolean afterOn = false;
	boolean afterBetween = false;
	boolean afterInsert = false;
	int inFunction = 0;
	int parensSinceSelect = 0;
	private LinkedList parenCounts = new LinkedList();
	private LinkedList afterByOrFromOrSelects = new LinkedList();
	int indent = 1;
	StringBuffer result = new StringBuffer();
	StringTokenizer tokens;
	String lastToken;
	String token;
	String lcToken;
	
	public Formatter() {	
	}
	
	public Formatter(String sql) {
		tokens =
			new StringTokenizer( sql,
								new StringBuffer().append("()+*/-=<>'`\"[],").append(StringHelper.WHITESPACE).toString(),
								true);
	}

	public Formatter setInitialString(String initial) {
		this.initial = initial;
		return this;
	}

	public Formatter setIndentString(String indent) {
		this.indentString = indent;
		return this;
	}

	public String format() {
		result.append(initial);
		while (tokens.hasMoreTokens()) {
			token = tokens.nextToken();
			lcToken = token.toLowerCase();
			if ("'".equals(token)) {
				String t;
				do {
					t = tokens.nextToken();
					token += t;
				} while (!"'".equals(t) && tokens.hasMoreTokens());
				// cannot handle single quotes
			} else if ("\"".equals(token)) {
				String t;
				do {
					t = tokens.nextToken();
					token += t;
				} while (!"\"".equals(t));
			}
			if (afterByOrSetOrFromOrSelect && ",".equals(token)) {
				commaAfterByOrFromOrSelect();
			} else if (afterOn && ",".equals(token)) {
				commaAfterOn();
			} else if ("(".equals(token)) {
				openParen();
			} else if (")".equals(token)) {
				closeParen();
			} else if (BEGIN_CLAUSES.contains(lcToken)) {
				beginNewClause();
			} else if (END_CLAUSES.contains(lcToken)) {
				endNewClause();
			} else if ("select".equals(lcToken)) {
				select();
			} else if (DML.contains(lcToken)) {
				updateOrInsertOrDelete();
			} else if ("values".equals(lcToken)) {
				values();
			} else if ("on".equals(lcToken)) {
				on();
			} else if (afterBetween && lcToken.equals("and")) {
				misc();
				afterBetween = false;
			} else if (LOGICAL.contains(lcToken)) {
				logical();
			} else if (isWhitespace(token)) {
				white();
			} else {
				misc();
			}
			if (!isWhitespace(token))
				lastToken = lcToken;
		}
		return  "\n"+result.toString();
	}

	private void commaAfterOn() {
		out();
		indent--;
		newline();
		afterOn = false;
		afterByOrSetOrFromOrSelect = true;
	}

	private void commaAfterByOrFromOrSelect() {
		out();
		newline();
	}

	private void logical() {
		if ("end".equals(lcToken))
			indent--;
		newline();
		out();
		beginLine = false;
	}

	private void on() {
		indent++;
		afterOn = true;
		newline();
		out();
		beginLine = false;
	}

	private void misc() {
		out();
		if ("between".equals(lcToken)) {
			afterBetween = true;
		}
		if (afterInsert) {
			newline();
			afterInsert = false;
		} else {
			beginLine = false;
			if ("case".equals(lcToken)) {
				indent++;
			}
		}
	}

	private void white() {
		if (!beginLine) {
			result.append(" ");
		}
	}

	private void updateOrInsertOrDelete() {
		out();
		indent++;
		beginLine = false;
		if ("update".equals(lcToken))
			newline();
		if ("insert".equals(lcToken))
			afterInsert = true;
	}

	private void select() {
		out();
		indent++;
		newline();
		parenCounts.addLast(new Integer(parensSinceSelect));
		afterByOrFromOrSelects.addLast(new Boolean(afterByOrSetOrFromOrSelect));
		parensSinceSelect = 0;
		afterByOrSetOrFromOrSelect = true;
	}

	private void out() {
		result.append(token);
	}

	private void endNewClause() {
		if (!afterBeginBeforeEnd) {
			indent--;
			if (afterOn) {
				indent--;
				afterOn = false;
			}
			newline();
		}
		out();
		if (!"union".equals(lcToken))
			indent++;
		newline();
		afterBeginBeforeEnd = false;
		afterByOrSetOrFromOrSelect = "by".equals(lcToken) || "set".equals(lcToken) || "from".equals(lcToken);
	}

	private void beginNewClause() {
		if (!afterBeginBeforeEnd) {
			if (afterOn) {
				indent--;
				afterOn = false;
			}
			indent--;
			newline();
		}
		out();
		beginLine = false;
		afterBeginBeforeEnd = true;
	}

	private void values() {
		indent--;
		newline();
		out();
		indent++;
		newline();
		afterValues = true;
	}

	private void closeParen() {
		parensSinceSelect--;
		if (parensSinceSelect < 0) {
			indent--;
			parensSinceSelect = ((Integer) parenCounts.removeLast()).intValue();
			afterByOrSetOrFromOrSelect = ((Boolean) afterByOrFromOrSelects.removeLast()).booleanValue();
		}
		if (inFunction > 0) {
			inFunction--;
			out();
		} else {
			if (!afterByOrSetOrFromOrSelect) {
				indent--;
				newline();
			}
			out();
		}
		beginLine = false;
	}

	private void openParen() {
		if (isFunctionName(lastToken) || inFunction > 0) {
			inFunction++;
		}
		beginLine = false;
		if (inFunction > 0) {
			out();
		} else {
			out();
			if (!afterByOrSetOrFromOrSelect) {
				indent++;
				newline();
				beginLine = true;
			}
		}
		parensSinceSelect++;
	}

	private static boolean isFunctionName(String tok) {
		final char begin = tok.charAt(0);
		final boolean isIdentifier =
			Character.isJavaIdentifierStart(begin) || '"' == begin;
		return isIdentifier 				&& 
				!LOGICAL.contains(tok)		&& 
				!END_CLAUSES.contains(tok)	&& 
				!QUANTIFIERS.contains(tok)	&& 
				!DML.contains(tok)			&& 
				!MISC.contains(tok);
	}

	private static boolean isWhitespace(String token) {
		return StringHelper.WHITESPACE.indexOf(token) >= 0;
	}

	private void newline() {
		result.append("\n");
		for (int i = 0; i < indent; i++) {
			result.append(indentString);
		}
		beginLine = true;
	}

	public static void test() {
		String query = " SELECT * FROM DUAL ";
		Log.info(new Formatter(query).format(), "neo.util.comm.Formatter");
	}
}

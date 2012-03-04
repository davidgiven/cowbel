/* © 2012 David Given
 * This file is made available under the terms of the two-clause BSD
 * license. See the file COPYING.BSD in the distribution directory for the
 * full license text.
 */

package com.cowlark.cowbel.parser.parsers;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.cowlark.cowbel.parser.core.Location;
import com.cowlark.cowbel.parser.core.MutableLocation;
import com.cowlark.cowbel.parser.core.ParseResult;

public abstract class Parser
{
	public static AtomParser AtomParser = new AtomParser();
	public static NumericConstantParser NumericConstantParser = new NumericConstantParser();
	public static StringConstantParser StringConstantParser = new StringConstantParser();
	public static ExpressionLowParser ExpressionLowParser = new ExpressionLowParser();
	public static ExpressionMediumParser ExpressionMediumParser = new ExpressionMediumParser();
	public static ExpressionHighParser ExpressionHighParser = new ExpressionHighParser();
	public static ExpressionLeafParser ExpressionLeafParser = new ExpressionLeafParser();
	public static ExpressionStatementParser ExpressionStatementParser = new ExpressionStatementParser();
	public static IdentifierParser IdentifierParser = new IdentifierParser();
	public static ParenthesisedExpressionParser ParenthesisedExpressionParser = new ParenthesisedExpressionParser();
	public static ProgramParser ProgramParser = new ProgramParser();
	public static StatementParser StatementParser = new StatementParser();
	public static VarDeclParser VarDeclParser = new VarDeclParser();
	public static TypeParser TypeParser = new TypeParser();
	public static TypeVariableParser TypeVariableParser = new TypeVariableParser();
	public static FunctionHeaderParser FunctionHeaderParser = new FunctionHeaderParser();
	public static ParameterDeclarationListParser ParameterDeclarationListParser = new ParameterDeclarationListParser();
	public static ParameterDeclarationParser ParameterDeclarationParser = new ParameterDeclarationParser();
	public static FunctionDefinitionParser FunctionDefinitionParser = new FunctionDefinitionParser();
	public static VarAssignmentParser VarAssignmentParser = new VarAssignmentParser();
	public static PrefixOperatorParser PrefixOperatorParser = new PrefixOperatorParser();
	public static OperatorParser OperatorParser = new OperatorParser();
	public static MethodNameParser MethodNameParser = new MethodNameParser();
	public static ScopeConstructorParser ScopeConstructorParser = new ScopeConstructorParser();
	public static StatementScopeConstructorParser StatementScopeConstructorParser = new StatementScopeConstructorParser();
	public static ReturnStatementParser ReturnStatementParser = new ReturnStatementParser();
	public static IfStatementParser IfStatementParser = new IfStatementParser();
	public static LabelStatementParser LabelStatementParser = new LabelStatementParser();
	public static GotoStatementParser GotoStatementParser = new GotoStatementParser();
	public static WhileStatementParser WhileStatementParser = new WhileStatementParser();
	public static DoWhileStatementParser DoWhileStatementParser = new DoWhileStatementParser();
	public static BreakStatementParser BreakStatementParser = new BreakStatementParser();
	public static ContinueStatementParser ContinueStatementParser = new ContinueStatementParser();
	public static ForStatementParser ForStatementParser = new ForStatementParser();
	public static IdentifierListParser IdentifierListParser = new IdentifierListParser();
	public static ArgumentListParser ArgumentListParser = new ArgumentListParser();
	public static DirectFunctionCallStatementParser DirectFunctionCallStatementParser = new DirectFunctionCallStatementParser();
	public static MethodCallStatementParser MethodCallStatementParser = new MethodCallStatementParser();
	public static VariableDeclarationListParser VariableDeclarationListParser = new VariableDeclarationListParser();
	public static ExpressionListParser ExpressionListParser = new ExpressionListParser();
	public static TypeVariableListParser TypeVariableListParser = new TypeVariableListParser();
	public static TypeListParser TypeListParser = new TypeListParser();
	public static TypeAssignmentParser TypeAssignmentParser = new TypeAssignmentParser();
	public static InterfaceDeclarationParser InterfaceDeclarationParser = new InterfaceDeclarationParser();
	public static InterfaceEntryParser InterfaceEntryParser = new InterfaceEntryParser();
	public static ImplementsStatementParser ImplementsStatementParser = new ImplementsStatementParser();
	public static ExternStatementParser ExternStatementParser = new ExternStatementParser();
	public static TypeExternStatementParser TypeExternStatementParser = new TypeExternStatementParser();
	public static ExternExpressionParser ExternExpressionParser = new ExternExpressionParser();
	
	public static Parser EOFParser = new EOFParser();
	public static Parser DotParser = new TrivialParser(".");
	public static Parser CommaParser = new TrivialParser(",");
	public static Parser SemicolonParser = new TrivialParser(";");
	public static Parser ColonParser = new TrivialParser(":");
	public static Parser OpenParenthesisParser = new TrivialParser("(");
	public static Parser CloseParenthesisParser = new TrivialParser(")");
	public static Parser OpenAngleBracketParser = new TrivialParser("<");
	public static Parser CloseAngleBracketParser = new TrivialParser(">");
	public static Parser OpenBraceParser = new TrivialParser("{");
	public static Parser CloseBraceParser = new TrivialParser("}");
	public static Parser OpenSquareParser = new TrivialParser("[");
	public static Parser CloseSquareParser = new TrivialParser("]");
	public static Parser EqualsParser = new TrivialParser("=");
	public static Parser OpenCloseParenthesisParser = new TrivialParser("()");

	/* When adding new keywords, remember to adjust the table in S. */
	public static Parser TrueTokenParser = new TrivialParser("true");
	public static Parser FalseTokenParser = new TrivialParser("false");
	public static Parser VarTokenParser = new TrivialParser("var");
	public static Parser FunctionTokenParser = new TrivialParser("function");
	public static Parser IfTokenParser = new TrivialParser("if");
	public static Parser ElseTokenParser = new TrivialParser("else");
	public static Parser GotoTokenParser = new TrivialParser("goto");
	public static Parser ReturnTokenParser = new TrivialParser("return");
	public static Parser BreakTokenParser = new TrivialParser("break");
	public static Parser ContinueTokenParser = new TrivialParser("continue");
	public static Parser WhileTokenParser = new TrivialParser("while");
	public static Parser DoTokenParser = new TrivialParser("do");
	public static Parser ForTokenParser = new TrivialParser("for");
	public static Parser TypeTokenParser = new TrivialParser("type");
	public static Parser ImplementsTokenParser = new TrivialParser("implements");
	public static Parser ExternTokenParser = new TrivialParser("extern");

	private static HashMap<Location, HashMap<Parser, ParseResult>> _cache =
		new HashMap<Location, HashMap<Parser, ParseResult>>(); 
		
	private static ParseResult get(Location loc, Parser type)
	{
		HashMap<Parser, ParseResult> m = _cache.get(loc);
		if (m == null)
			return null;
		return m.get(type);
	}
	
	private static void put(Location loc, Parser type, ParseResult result)
	{
		HashMap<Parser, ParseResult> m = _cache.get(loc);
		if (m == null)
		{
			m = new HashMap<Parser, ParseResult>();
			_cache.put(loc, m);
		}
		
		m.put(type, result);
	}

	private void linecomment(MutableLocation loc)
	{
		loc.advance(2); /* Skip the // */
		
		for (;;)
		{
			int c = loc.codepointAtOffset(0);
			if ((c == '\n') || (c == -1))
				break;
			
			loc.advance();
		}
	}
	
	private void blockcomment(MutableLocation loc)
	{
		loc.advance(2); /* Skip the /* */
		
		for (;;)
		{
			int c1 = loc.codepointAtOffset(0);
			int c2 = loc.codepointAtOffset(1);
			
			if ((c1 == '*') && (c2 == '/'))
			{
				loc.advance(2);
				break;
			}
			
			if (c1 == -1)
				break;
			
			loc.advance();
		}
	}
	
	private static Pattern _line_directive_pattern =
		Pattern.compile(" ([0-9]+) \"([^\"]*)\".*");
	
	private void line_directive(MutableLocation loc)
	{
		StringBuilder sb = new StringBuilder();
		
		for (;;)
		{
			int c = loc.codepointAtOffset(0);
			loc.advance();
			
			if (c == '\n')
				break;
			sb.appendCodePoint(c);
		}
		
		Matcher match = _line_directive_pattern.matcher(sb);
		if (!match.find())
			return;
		
		loc.setLineNumber(Integer.valueOf(match.group(1)));
		loc.setFilename(match.group(2));
	}
	
	private void whitespace(MutableLocation loc)
	{
		for (;;)
		{
			int c = loc.codepointAtOffset(0);
			
			if ((c == '#') && (loc.getColumn() == 0))
			{
				line_directive(loc);
				continue;
			}
			
			if (!Character.isWhitespace(c))
			{
				if (c == '/')
				{
					c = loc.codepointAtOffset(1);
					if (c == '*')
						blockcomment(loc);
					else if (c == '/')
						linecomment(loc);
					else
						break;
				}
				else
					break;
			}
			else
				loc.advance();
		}
	}
	
	protected abstract ParseResult parseImpl(Location location);
	
	public ParseResult parse(Location location)
	{
		ParseResult pr = get(location, this);
		if (pr != null)
			return pr;

		int c = location.codepointAtOffset(0);
		if (Character.isWhitespace(c) || (c == '/') || (c == '#'))
		{
			MutableLocation ml = new MutableLocation(location);
			whitespace(ml);
			pr = parseImpl(ml);
		}
		else
			pr = parseImpl(location);
		
		put(location, this, pr);
		return pr;
	}
	
	protected static ParseResult combineParseErrors(ParseResult... results)
	{
		ParseResult max = null;
		for (ParseResult pr : results)
		{
			if (pr.failed())
			{
				if ((max == null) || (max.end().compareTo(pr.end()) < 0))
					max = pr;
			}
		}
		
		return max;
	}
}

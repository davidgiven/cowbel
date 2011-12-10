package com.cowlark.sake.parser.nodes;

import java.util.HashMap;
import com.cowlark.sake.parser.core.Location;
import com.cowlark.sake.parser.core.MutableLocation;
import com.cowlark.sake.parser.core.ParseResult;

public abstract class Parser
{
	public static AtomParser AtomParser = new AtomParser();
	public static StringConstantParser StringConstantParser = new StringConstantParser();
	public static ListConstructorParser ListConstructorParser = new ListConstructorParser();
	public static ExpressionLowParser ExpressionLowParser = new ExpressionLowParser();
	public static ExpressionMediumParser ExpressionMediumParser = new ExpressionMediumParser();
	public static ExpressionHighParser ExpressionHighParser = new ExpressionHighParser();
	public static ExpressionLeafParser ExpressionLeafParser = new ExpressionLeafParser();
	public static ExpressionStatementParser ExpressionStatementParser = new ExpressionStatementParser();
	public static IdentifierParser IdentifierParser = new IdentifierParser();
	public static ParenthesisedExpressionParser ParenthesisedExpressionParser = new ParenthesisedExpressionParser();
	public static ProgramParser ProgramParser = new ProgramParser();
	public static FunctionStatementParser FunctionStatementParser = new FunctionStatementParser();
	public static TopLevelStatementParser TopLevelStatementParser = new TopLevelStatementParser();
	public static VarDeclParser VarDeclParser = new VarDeclParser();
	public static TypeParser TypeParser = new TypeParser();
	public static FunctionHeaderParser FunctionHeaderParser = new FunctionHeaderParser();
	public static ParameterDeclarationListParser ParameterDeclarationListParser = new ParameterDeclarationListParser();
	public static ParameterDeclarationParser ParameterDeclarationParser = new ParameterDeclarationParser();
	public static FunctionDefinitionParser FunctionDefinitionParser = new FunctionDefinitionParser();
	public static BlockParser BlockParser = new BlockParser();
	public static VarAssignmentParser VarAssignmentParser = new VarAssignmentParser();
	public static PrefixOperatorParser PrefixOperatorParser = new PrefixOperatorParser();
	public static OperatorParser OperatorParser = new OperatorParser();
	public static MethodNameParser MethodNameParser = new MethodNameParser();
	
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
	public static Parser PlusParser = new TrivialParser("+");

	public static Parser VarTokenParser = new TrivialParser("var");
	public static Parser FunctionTokenParser = new TrivialParser("function");
	public static Parser StringTokenParser = new TrivialParser("string");
	public static Parser IntegerTokenParser = new TrivialParser("integer");
	public static Parser BooleanTokenParser = new TrivialParser("boolean");

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
	
	private void whitespace(MutableLocation loc)
	{
		for (;;)
		{
			int c = loc.codepointAtOffset(0);
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
		if (Character.isWhitespace(c) || (c == '/'))
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

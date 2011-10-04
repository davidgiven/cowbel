package com.cowlang2.parser.nodes;

import java.util.HashMap;
import com.cowlang2.parser.core.Location;
import com.cowlang2.parser.core.MutableLocation;
import com.cowlang2.parser.core.ParseResult;

public abstract class Parser
{
	public static AtomParser AtomParser = new AtomParser();
	public static Expression1Parser Expression1Parser = new Expression1Parser();
	public static Expression2Parser Expression2Parser = new Expression2Parser();
	public static Expression3Parser Expression3Parser = new Expression3Parser();
	public static ExpressionStatementParser ExpressionStatementParser = new ExpressionStatementParser();
	public static IdentifierParser IdentifierParser = new IdentifierParser();
	public static InfixOperatorParser InfixOperatorParser = new InfixOperatorParser();
	public static InterfaceDeclarationParser InterfaceDeclarationParser = new InterfaceDeclarationParser();
	public static MethodCallParser MethodCallParser = new MethodCallParser();
	public static MethodNameParser MethodNameParser = new MethodNameParser();
	public static OperatorParser OperatorParser = new OperatorParser();
	public static ParenthesisedExpressionParser ParenthesisedExpressionParser = new ParenthesisedExpressionParser();
	public static PrefixOperatorParser PrefixOperatorParser = new PrefixOperatorParser();
	public static ProgramParser ProgramParser = new ProgramParser();
	public static StatementParser StatementParser = new StatementParser();
	public static StatementsParser StatementsParser = new StatementsParser();
	public static VarDeclParser VarDeclParser = new VarDeclParser();
	public static TypexDeclarationParser TypexDeclarationParser = new TypexDeclarationParser();
	public static TypeReferenceParser TypeReferenceParser = new TypeReferenceParser();
	public static MethodDeclarationParser MethodDeclarationParser = new MethodDeclarationParser();
	public static ParameterDeclarationListParser ParameterDeclarationListParser = new ParameterDeclarationListParser();
	public static ParameterDeclarationParser ParameterDeclarationParser = new ParameterDeclarationParser();
	
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

	public static Parser VarTokenParser = new TrivialParser("var");
	public static Parser InterfaceTokenParser = new TrivialParser("interface");
	public static Parser MethodTokenParser = new TrivialParser("method");

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

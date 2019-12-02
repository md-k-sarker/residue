// Generated from /Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/main/antlr4/org/dase/parser/antlr/simple_parser_v2.g4 by ANTLR 4.7.2

package org.dase.parser.antlr;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class simple_parser_v2Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, SUBCLASSOF=2, EQUIVALENTTO=3, NEQ=4, COMPOSE=5, DOT=6, INVERSE=7, 
		OPENPAR=8, CLOSEPAR=9, OPENSQPAR=10, CLOSESQPAR=11, OPENBRACE=12, CLOSEBRACE=13, 
		COLON=14, AND=15, OR=16, NOT=17, SOME=18, ALL=19, MIN=20, MAX=21, EXACT=22, 
		IN=23, TRANSITIVEROLES=24, INT=25, DOUBLE=26, IDD=27;
	public static final int
		RULE_parsecandidatesolution = 0, RULE_groupedcandidateclasses = 1, RULE_parsecandidateclasswithprop = 2, 
		RULE_parsecandidateclass = 3, RULE_parseconjunctivehornclause = 4, RULE_parseposclasses = 5, 
		RULE_parsenegclasses = 6, RULE_parseobjectpropertyid = 7, RULE_parseclassid = 8, 
		RULE_parseid = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"parsecandidatesolution", "groupedcandidateclasses", "parsecandidateclasswithprop", 
			"parsecandidateclass", "parseconjunctivehornclause", "parseposclasses", 
			"parsenegclasses", "parseobjectpropertyid", "parseclassid", "parseid"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, "'.'", null, "'('", "')'", "'['", 
			"']'", "'{'", "'}'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "SUBCLASSOF", "EQUIVALENTTO", "NEQ", "COMPOSE", "DOT", "INVERSE", 
			"OPENPAR", "CLOSEPAR", "OPENSQPAR", "CLOSESQPAR", "OPENBRACE", "CLOSEBRACE", 
			"COLON", "AND", "OR", "NOT", "SOME", "ALL", "MIN", "MAX", "EXACT", "IN", 
			"TRANSITIVEROLES", "INT", "DOUBLE", "IDD"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "simple_parser_v2.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public simple_parser_v2Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ParsecandidatesolutionContext extends ParserRuleContext {
		public GroupedcandidateclassesContext g;
		public List<GroupedcandidateclassesContext> groupedcandidateclasses() {
			return getRuleContexts(GroupedcandidateclassesContext.class);
		}
		public GroupedcandidateclassesContext groupedcandidateclasses(int i) {
			return getRuleContext(GroupedcandidateclassesContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(simple_parser_v2Parser.AND); }
		public TerminalNode AND(int i) {
			return getToken(simple_parser_v2Parser.AND, i);
		}
		public ParsecandidatesolutionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parsecandidatesolution; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).enterParsecandidatesolution(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).exitParsecandidatesolution(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof simple_parser_v2Visitor ) return ((simple_parser_v2Visitor<? extends T>)visitor).visitParsecandidatesolution(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParsecandidatesolutionContext parsecandidatesolution() throws RecognitionException {
		ParsecandidatesolutionContext _localctx = new ParsecandidatesolutionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parsecandidatesolution);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(20);
			((ParsecandidatesolutionContext)_localctx).g = groupedcandidateclasses();
			setState(25);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(21);
				match(AND);
				setState(22);
				groupedcandidateclasses();
				}
				}
				setState(27);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupedcandidateclassesContext extends ParserRuleContext {
		public TerminalNode SOME() { return getToken(simple_parser_v2Parser.SOME, 0); }
		public ParseobjectpropertyidContext parseobjectpropertyid() {
			return getRuleContext(ParseobjectpropertyidContext.class,0);
		}
		public TerminalNode DOT() { return getToken(simple_parser_v2Parser.DOT, 0); }
		public List<ParsecandidateclasswithpropContext> parsecandidateclasswithprop() {
			return getRuleContexts(ParsecandidateclasswithpropContext.class);
		}
		public ParsecandidateclasswithpropContext parsecandidateclasswithprop(int i) {
			return getRuleContext(ParsecandidateclasswithpropContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(simple_parser_v2Parser.OR); }
		public TerminalNode OR(int i) {
			return getToken(simple_parser_v2Parser.OR, i);
		}
		public List<ParsecandidateclassContext> parsecandidateclass() {
			return getRuleContexts(ParsecandidateclassContext.class);
		}
		public ParsecandidateclassContext parsecandidateclass(int i) {
			return getRuleContext(ParsecandidateclassContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(simple_parser_v2Parser.AND); }
		public TerminalNode AND(int i) {
			return getToken(simple_parser_v2Parser.AND, i);
		}
		public GroupedcandidateclassesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupedcandidateclasses; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).enterGroupedcandidateclasses(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).exitGroupedcandidateclasses(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof simple_parser_v2Visitor ) return ((simple_parser_v2Visitor<? extends T>)visitor).visitGroupedcandidateclasses(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupedcandidateclassesContext groupedcandidateclasses() throws RecognitionException {
		GroupedcandidateclassesContext _localctx = new GroupedcandidateclassesContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_groupedcandidateclasses);
		int _la;
		try {
			int _alt;
			setState(47);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SOME:
				enterOuterAlt(_localctx, 1);
				{
				setState(28);
				match(SOME);
				setState(29);
				parseobjectpropertyid();
				setState(30);
				match(DOT);
				setState(31);
				parsecandidateclasswithprop();
				setState(36);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==OR) {
					{
					{
					setState(32);
					match(OR);
					setState(33);
					parsecandidateclasswithprop();
					}
					}
					setState(38);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case OPENPAR:
			case IDD:
				enterOuterAlt(_localctx, 2);
				{
				setState(39);
				parsecandidateclass();
				setState(44);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(40);
						match(AND);
						setState(41);
						parsecandidateclass();
						}
						} 
					}
					setState(46);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParsecandidateclasswithpropContext extends ParserRuleContext {
		public List<ParseconjunctivehornclauseContext> parseconjunctivehornclause() {
			return getRuleContexts(ParseconjunctivehornclauseContext.class);
		}
		public ParseconjunctivehornclauseContext parseconjunctivehornclause(int i) {
			return getRuleContext(ParseconjunctivehornclauseContext.class,i);
		}
		public TerminalNode OPENPAR() { return getToken(simple_parser_v2Parser.OPENPAR, 0); }
		public List<TerminalNode> OR() { return getTokens(simple_parser_v2Parser.OR); }
		public TerminalNode OR(int i) {
			return getToken(simple_parser_v2Parser.OR, i);
		}
		public TerminalNode CLOSEPAR() { return getToken(simple_parser_v2Parser.CLOSEPAR, 0); }
		public ParsecandidateclasswithpropContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parsecandidateclasswithprop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).enterParsecandidateclasswithprop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).exitParsecandidateclasswithprop(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof simple_parser_v2Visitor ) return ((simple_parser_v2Visitor<? extends T>)visitor).visitParsecandidateclasswithprop(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParsecandidateclasswithpropContext parsecandidateclasswithprop() throws RecognitionException {
		ParsecandidateclasswithpropContext _localctx = new ParsecandidateclasswithpropContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_parsecandidateclasswithprop);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(49);
				match(OPENPAR);
				}
				break;
			}
			setState(52);
			parseconjunctivehornclause();
			setState(57);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(53);
					match(OR);
					setState(54);
					parseconjunctivehornclause();
					}
					} 
				}
				setState(59);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CLOSEPAR) {
				{
				setState(60);
				match(CLOSEPAR);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParsecandidateclassContext extends ParserRuleContext {
		public List<ParseconjunctivehornclauseContext> parseconjunctivehornclause() {
			return getRuleContexts(ParseconjunctivehornclauseContext.class);
		}
		public ParseconjunctivehornclauseContext parseconjunctivehornclause(int i) {
			return getRuleContext(ParseconjunctivehornclauseContext.class,i);
		}
		public TerminalNode OPENPAR() { return getToken(simple_parser_v2Parser.OPENPAR, 0); }
		public List<TerminalNode> AND() { return getTokens(simple_parser_v2Parser.AND); }
		public TerminalNode AND(int i) {
			return getToken(simple_parser_v2Parser.AND, i);
		}
		public TerminalNode CLOSEPAR() { return getToken(simple_parser_v2Parser.CLOSEPAR, 0); }
		public ParsecandidateclassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parsecandidateclass; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).enterParsecandidateclass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).exitParsecandidateclass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof simple_parser_v2Visitor ) return ((simple_parser_v2Visitor<? extends T>)visitor).visitParsecandidateclass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParsecandidateclassContext parsecandidateclass() throws RecognitionException {
		ParsecandidateclassContext _localctx = new ParsecandidateclassContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_parsecandidateclass);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(63);
				match(OPENPAR);
				}
				break;
			}
			setState(66);
			parseconjunctivehornclause();
			setState(71);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(67);
					match(AND);
					setState(68);
					parseconjunctivehornclause();
					}
					} 
				}
				setState(73);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			setState(75);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CLOSEPAR) {
				{
				setState(74);
				match(CLOSEPAR);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParseconjunctivehornclauseContext extends ParserRuleContext {
		public ParseposclassesContext parseposclasses() {
			return getRuleContext(ParseposclassesContext.class,0);
		}
		public List<TerminalNode> OPENPAR() { return getTokens(simple_parser_v2Parser.OPENPAR); }
		public TerminalNode OPENPAR(int i) {
			return getToken(simple_parser_v2Parser.OPENPAR, i);
		}
		public List<TerminalNode> CLOSEPAR() { return getTokens(simple_parser_v2Parser.CLOSEPAR); }
		public TerminalNode CLOSEPAR(int i) {
			return getToken(simple_parser_v2Parser.CLOSEPAR, i);
		}
		public TerminalNode AND() { return getToken(simple_parser_v2Parser.AND, 0); }
		public TerminalNode NOT() { return getToken(simple_parser_v2Parser.NOT, 0); }
		public ParsenegclassesContext parsenegclasses() {
			return getRuleContext(ParsenegclassesContext.class,0);
		}
		public ParseconjunctivehornclauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parseconjunctivehornclause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).enterParseconjunctivehornclause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).exitParseconjunctivehornclause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof simple_parser_v2Visitor ) return ((simple_parser_v2Visitor<? extends T>)visitor).visitParseconjunctivehornclause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParseconjunctivehornclauseContext parseconjunctivehornclause() throws RecognitionException {
		ParseconjunctivehornclauseContext _localctx = new ParseconjunctivehornclauseContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_parseconjunctivehornclause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(77);
				match(OPENPAR);
				}
				break;
			}
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OPENPAR) {
				{
				setState(80);
				match(OPENPAR);
				}
			}

			setState(83);
			parseposclasses();
			setState(85);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(84);
				match(CLOSEPAR);
				}
				break;
			}
			setState(93);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(87);
				match(AND);
				setState(88);
				match(NOT);
				setState(89);
				match(OPENPAR);
				setState(90);
				parsenegclasses();
				setState(91);
				match(CLOSEPAR);
				}
				break;
			}
			setState(96);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(95);
				match(CLOSEPAR);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParseposclassesContext extends ParserRuleContext {
		public List<ParseclassidContext> parseclassid() {
			return getRuleContexts(ParseclassidContext.class);
		}
		public ParseclassidContext parseclassid(int i) {
			return getRuleContext(ParseclassidContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(simple_parser_v2Parser.AND); }
		public TerminalNode AND(int i) {
			return getToken(simple_parser_v2Parser.AND, i);
		}
		public ParseposclassesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parseposclasses; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).enterParseposclasses(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).exitParseposclasses(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof simple_parser_v2Visitor ) return ((simple_parser_v2Visitor<? extends T>)visitor).visitParseposclasses(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParseposclassesContext parseposclasses() throws RecognitionException {
		ParseposclassesContext _localctx = new ParseposclassesContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_parseposclasses);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			parseclassid();
			setState(103);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(99);
					match(AND);
					setState(100);
					parseclassid();
					}
					} 
				}
				setState(105);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParsenegclassesContext extends ParserRuleContext {
		public List<ParseclassidContext> parseclassid() {
			return getRuleContexts(ParseclassidContext.class);
		}
		public ParseclassidContext parseclassid(int i) {
			return getRuleContext(ParseclassidContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(simple_parser_v2Parser.OR); }
		public TerminalNode OR(int i) {
			return getToken(simple_parser_v2Parser.OR, i);
		}
		public ParsenegclassesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parsenegclasses; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).enterParsenegclasses(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).exitParsenegclasses(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof simple_parser_v2Visitor ) return ((simple_parser_v2Visitor<? extends T>)visitor).visitParsenegclasses(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParsenegclassesContext parsenegclasses() throws RecognitionException {
		ParsenegclassesContext _localctx = new ParsenegclassesContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_parsenegclasses);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			parseclassid();
			setState(111);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(107);
				match(OR);
				setState(108);
				parseclassid();
				}
				}
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParseobjectpropertyidContext extends ParserRuleContext {
		public ParseidContext parseid() {
			return getRuleContext(ParseidContext.class,0);
		}
		public ParseobjectpropertyidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parseobjectpropertyid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).enterParseobjectpropertyid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).exitParseobjectpropertyid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof simple_parser_v2Visitor ) return ((simple_parser_v2Visitor<? extends T>)visitor).visitParseobjectpropertyid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParseobjectpropertyidContext parseobjectpropertyid() throws RecognitionException {
		ParseobjectpropertyidContext _localctx = new ParseobjectpropertyidContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_parseobjectpropertyid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			parseid();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParseclassidContext extends ParserRuleContext {
		public ParseidContext parseid() {
			return getRuleContext(ParseidContext.class,0);
		}
		public ParseclassidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parseclassid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).enterParseclassid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).exitParseclassid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof simple_parser_v2Visitor ) return ((simple_parser_v2Visitor<? extends T>)visitor).visitParseclassid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParseclassidContext parseclassid() throws RecognitionException {
		ParseclassidContext _localctx = new ParseclassidContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_parseclassid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			parseid();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParseidContext extends ParserRuleContext {
		public TerminalNode IDD() { return getToken(simple_parser_v2Parser.IDD, 0); }
		public ParseidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parseid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).enterParseid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof simple_parser_v2Listener ) ((simple_parser_v2Listener)listener).exitParseid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof simple_parser_v2Visitor ) return ((simple_parser_v2Visitor<? extends T>)visitor).visitParseid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParseidContext parseid() throws RecognitionException {
		ParseidContext _localctx = new ParseidContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_parseid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			match(IDD);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\35{\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\3"+
		"\2\3\2\3\2\7\2\32\n\2\f\2\16\2\35\13\2\3\3\3\3\3\3\3\3\3\3\3\3\7\3%\n"+
		"\3\f\3\16\3(\13\3\3\3\3\3\3\3\7\3-\n\3\f\3\16\3\60\13\3\5\3\62\n\3\3\4"+
		"\5\4\65\n\4\3\4\3\4\3\4\7\4:\n\4\f\4\16\4=\13\4\3\4\5\4@\n\4\3\5\5\5C"+
		"\n\5\3\5\3\5\3\5\7\5H\n\5\f\5\16\5K\13\5\3\5\5\5N\n\5\3\6\5\6Q\n\6\3\6"+
		"\5\6T\n\6\3\6\3\6\5\6X\n\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6`\n\6\3\6\5\6c\n"+
		"\6\3\7\3\7\3\7\7\7h\n\7\f\7\16\7k\13\7\3\b\3\b\3\b\7\bp\n\b\f\b\16\bs"+
		"\13\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\2\2\f\2\4\6\b\n\f\16\20\22\24\2\2"+
		"\2\u0081\2\26\3\2\2\2\4\61\3\2\2\2\6\64\3\2\2\2\bB\3\2\2\2\nP\3\2\2\2"+
		"\fd\3\2\2\2\16l\3\2\2\2\20t\3\2\2\2\22v\3\2\2\2\24x\3\2\2\2\26\33\5\4"+
		"\3\2\27\30\7\21\2\2\30\32\5\4\3\2\31\27\3\2\2\2\32\35\3\2\2\2\33\31\3"+
		"\2\2\2\33\34\3\2\2\2\34\3\3\2\2\2\35\33\3\2\2\2\36\37\7\24\2\2\37 \5\20"+
		"\t\2 !\7\b\2\2!&\5\6\4\2\"#\7\22\2\2#%\5\6\4\2$\"\3\2\2\2%(\3\2\2\2&$"+
		"\3\2\2\2&\'\3\2\2\2\'\62\3\2\2\2(&\3\2\2\2).\5\b\5\2*+\7\21\2\2+-\5\b"+
		"\5\2,*\3\2\2\2-\60\3\2\2\2.,\3\2\2\2./\3\2\2\2/\62\3\2\2\2\60.\3\2\2\2"+
		"\61\36\3\2\2\2\61)\3\2\2\2\62\5\3\2\2\2\63\65\7\n\2\2\64\63\3\2\2\2\64"+
		"\65\3\2\2\2\65\66\3\2\2\2\66;\5\n\6\2\678\7\22\2\28:\5\n\6\29\67\3\2\2"+
		"\2:=\3\2\2\2;9\3\2\2\2;<\3\2\2\2<?\3\2\2\2=;\3\2\2\2>@\7\13\2\2?>\3\2"+
		"\2\2?@\3\2\2\2@\7\3\2\2\2AC\7\n\2\2BA\3\2\2\2BC\3\2\2\2CD\3\2\2\2DI\5"+
		"\n\6\2EF\7\21\2\2FH\5\n\6\2GE\3\2\2\2HK\3\2\2\2IG\3\2\2\2IJ\3\2\2\2JM"+
		"\3\2\2\2KI\3\2\2\2LN\7\13\2\2ML\3\2\2\2MN\3\2\2\2N\t\3\2\2\2OQ\7\n\2\2"+
		"PO\3\2\2\2PQ\3\2\2\2QS\3\2\2\2RT\7\n\2\2SR\3\2\2\2ST\3\2\2\2TU\3\2\2\2"+
		"UW\5\f\7\2VX\7\13\2\2WV\3\2\2\2WX\3\2\2\2X_\3\2\2\2YZ\7\21\2\2Z[\7\23"+
		"\2\2[\\\7\n\2\2\\]\5\16\b\2]^\7\13\2\2^`\3\2\2\2_Y\3\2\2\2_`\3\2\2\2`"+
		"b\3\2\2\2ac\7\13\2\2ba\3\2\2\2bc\3\2\2\2c\13\3\2\2\2di\5\22\n\2ef\7\21"+
		"\2\2fh\5\22\n\2ge\3\2\2\2hk\3\2\2\2ig\3\2\2\2ij\3\2\2\2j\r\3\2\2\2ki\3"+
		"\2\2\2lq\5\22\n\2mn\7\22\2\2np\5\22\n\2om\3\2\2\2ps\3\2\2\2qo\3\2\2\2"+
		"qr\3\2\2\2r\17\3\2\2\2sq\3\2\2\2tu\5\24\13\2u\21\3\2\2\2vw\5\24\13\2w"+
		"\23\3\2\2\2xy\7\35\2\2y\25\3\2\2\2\23\33&.\61\64;?BIMPSW_biq";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
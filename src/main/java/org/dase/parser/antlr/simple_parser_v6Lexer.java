// Generated from /Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/main/antlr4/org/dase/parser/antlr/simple_parser_v6.g4 by ANTLR 4.7.2

package org.dase.parser.antlr;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class simple_parser_v6Lexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, SUBCLASSOF=2, EQUIVALENTTO=3, NEQ=4, COMPOSE=5, DOT=6, INVERSE=7, 
		OPENPAR=8, CLOSEPAR=9, OPENSQPAR=10, CLOSESQPAR=11, OPENBRACE=12, CLOSEBRACE=13, 
		COLON=14, AND=15, OR=16, NOT=17, SOME=18, ALL=19, MIN=20, MAX=21, EXACT=22, 
		IN=23, TRANSITIVEROLES=24, INT=25, DOUBLE=26, IDD=27;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "SUBCLASSOF", "EQUIVALENTTO", "NEQ", "COMPOSE", "DOT", "INVERSE", 
			"OPENPAR", "CLOSEPAR", "OPENSQPAR", "CLOSESQPAR", "OPENBRACE", "CLOSEBRACE", 
			"COLON", "AND", "OR", "NOT", "SOME", "ALL", "MIN", "MAX", "EXACT", "IN", 
			"TRANSITIVEROLES", "INT", "DOUBLE", "IDD"
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


	public simple_parser_v6Lexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "simple_parser_v6.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

//	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\35\u0121\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\3\2\6\2;\n\2\r\2\16\2<\3\2\3\2\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3R"+
		"\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4]\n\4\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\5\5g\n\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\5\bp\n\b\3\t\3\t\3\n"+
		"\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u008d\n\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u009a\n\21\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u00a8\n\22\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23"+
		"\u00c6\n\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u00dc\n\24\3\25\3\25\3\25"+
		"\3\25\3\25\5\25\u00e3\n\25\3\26\3\26\3\26\3\26\3\26\5\26\u00ea\n\26\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\5\27\u00f2\n\27\3\30\3\30\3\30\5\30\u00f7"+
		"\n\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\5\31\u010a\n\31\3\32\6\32\u010d\n\32\r\32\16\32\u010e"+
		"\3\33\6\33\u0112\n\33\r\33\16\33\u0113\3\33\3\33\7\33\u0118\n\33\f\33"+
		"\16\33\u011b\13\33\3\34\6\34\u011e\n\34\r\34\16\34\u011f\2\2\35\3\3\5"+
		"\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21"+
		"!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\35\3\2\b\5\2\13"+
		"\f\17\17\"\"\4\2qq\u221a\u221a\4\2@@\u2267\u2267\4\2>>\u2266\u2266\3\2"+
		"\62;\f\2\13\f\"\"*+..\60\60>@]]_`}}\177\177\2\u0144\2\3\3\2\2\2\2\5\3"+
		"\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2"+
		"\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3"+
		"\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'"+
		"\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63"+
		"\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\3:\3\2\2\2\5Q\3\2\2\2\7\\\3\2\2\2\t"+
		"f\3\2\2\2\13h\3\2\2\2\rj\3\2\2\2\17o\3\2\2\2\21q\3\2\2\2\23s\3\2\2\2\25"+
		"u\3\2\2\2\27w\3\2\2\2\31y\3\2\2\2\33{\3\2\2\2\35}\3\2\2\2\37\u008c\3\2"+
		"\2\2!\u0099\3\2\2\2#\u00a7\3\2\2\2%\u00c5\3\2\2\2\'\u00db\3\2\2\2)\u00e2"+
		"\3\2\2\2+\u00e9\3\2\2\2-\u00f1\3\2\2\2/\u00f6\3\2\2\2\61\u0109\3\2\2\2"+
		"\63\u010c\3\2\2\2\65\u0111\3\2\2\2\67\u011d\3\2\2\29;\t\2\2\2:9\3\2\2"+
		"\2;<\3\2\2\2<:\3\2\2\2<=\3\2\2\2=>\3\2\2\2>?\b\2\2\2?\4\3\2\2\2@R\7\u2293"+
		"\2\2AB\7/\2\2BR\7@\2\2CD\7u\2\2DE\7w\2\2ER\7d\2\2FG\7^\2\2GH\7u\2\2HI"+
		"\7s\2\2IJ\7u\2\2JK\7w\2\2KL\7d\2\2LM\7u\2\2MN\7g\2\2NO\7v\2\2OP\7g\2\2"+
		"PR\7s\2\2Q@\3\2\2\2QA\3\2\2\2QC\3\2\2\2QF\3\2\2\2R\6\3\2\2\2S]\7\u2263"+
		"\2\2TU\7?\2\2U]\7?\2\2VW\7^\2\2WX\7g\2\2XY\7s\2\2YZ\7w\2\2Z[\7k\2\2[]"+
		"\7x\2\2\\S\3\2\2\2\\T\3\2\2\2\\V\3\2\2\2]\b\3\2\2\2^g\7\u2262\2\2_`\7"+
		"#\2\2`g\7?\2\2ab\7^\2\2bc\7p\2\2cd\7q\2\2de\7v\2\2eg\7?\2\2f^\3\2\2\2"+
		"f_\3\2\2\2fa\3\2\2\2g\n\3\2\2\2hi\t\3\2\2i\f\3\2\2\2jk\7\60\2\2k\16\3"+
		"\2\2\2lp\7\u207d\2\2mn\7`\2\2np\7/\2\2ol\3\2\2\2om\3\2\2\2p\20\3\2\2\2"+
		"qr\7*\2\2r\22\3\2\2\2st\7+\2\2t\24\3\2\2\2uv\7]\2\2v\26\3\2\2\2wx\7_\2"+
		"\2x\30\3\2\2\2yz\7}\2\2z\32\3\2\2\2{|\7\177\2\2|\34\3\2\2\2}~\7<\2\2~"+
		"\36\3\2\2\2\177\u008d\7\u2295\2\2\u0080\u0081\7c\2\2\u0081\u0082\7p\2"+
		"\2\u0082\u008d\7f\2\2\u0083\u0084\7C\2\2\u0084\u0085\7P\2\2\u0085\u008d"+
		"\7F\2\2\u0086\u0087\7^\2\2\u0087\u0088\7u\2\2\u0088\u0089\7s\2\2\u0089"+
		"\u008a\7e\2\2\u008a\u008b\7c\2\2\u008b\u008d\7r\2\2\u008c\177\3\2\2\2"+
		"\u008c\u0080\3\2\2\2\u008c\u0083\3\2\2\2\u008c\u0086\3\2\2\2\u008d \3"+
		"\2\2\2\u008e\u009a\7\u2296\2\2\u008f\u0090\7q\2\2\u0090\u009a\7t\2\2\u0091"+
		"\u0092\7Q\2\2\u0092\u009a\7T\2\2\u0093\u0094\7^\2\2\u0094\u0095\7u\2\2"+
		"\u0095\u0096\7s\2\2\u0096\u0097\7e\2\2\u0097\u0098\7w\2\2\u0098\u009a"+
		"\7r\2\2\u0099\u008e\3\2\2\2\u0099\u008f\3\2\2\2\u0099\u0091\3\2\2\2\u0099"+
		"\u0093\3\2\2\2\u009a\"\3\2\2\2\u009b\u00a8\7\u00ae\2\2\u009c\u009d\7p"+
		"\2\2\u009d\u009e\7q\2\2\u009e\u00a8\7v\2\2\u009f\u00a0\7P\2\2\u00a0\u00a1"+
		"\7Q\2\2\u00a1\u00a8\7V\2\2\u00a2\u00a3\7^\2\2\u00a3\u00a4\7n\2\2\u00a4"+
		"\u00a5\7p\2\2\u00a5\u00a6\7q\2\2\u00a6\u00a8\7v\2\2\u00a7\u009b\3\2\2"+
		"\2\u00a7\u009c\3\2\2\2\u00a7\u009f\3\2\2\2\u00a7\u00a2\3\2\2\2\u00a8$"+
		"\3\2\2\2\u00a9\u00c6\7\u2205\2\2\u00aa\u00ab\7g\2\2\u00ab\u00ac\7z\2\2"+
		"\u00ac\u00ad\7k\2\2\u00ad\u00ae\7u\2\2\u00ae\u00af\7v\2\2\u00af\u00c6"+
		"\7u\2\2\u00b0\u00b1\7G\2\2\u00b1\u00b2\7Z\2\2\u00b2\u00b3\7K\2\2\u00b3"+
		"\u00b4\7U\2\2\u00b4\u00b5\7V\2\2\u00b5\u00c6\7U\2\2\u00b6\u00b7\7u\2\2"+
		"\u00b7\u00b8\7q\2\2\u00b8\u00b9\7o\2\2\u00b9\u00c6\7g\2\2\u00ba\u00bb"+
		"\7U\2\2\u00bb\u00bc\7Q\2\2\u00bc\u00bd\7O\2\2\u00bd\u00c6\7G\2\2\u00be"+
		"\u00bf\7^\2\2\u00bf\u00c0\7g\2\2\u00c0\u00c1\7z\2\2\u00c1\u00c2\7k\2\2"+
		"\u00c2\u00c3\7u\2\2\u00c3\u00c4\7v\2\2\u00c4\u00c6\7u\2\2\u00c5\u00a9"+
		"\3\2\2\2\u00c5\u00aa\3\2\2\2\u00c5\u00b0\3\2\2\2\u00c5\u00b6\3\2\2\2\u00c5"+
		"\u00ba\3\2\2\2\u00c5\u00be\3\2\2\2\u00c6&\3\2\2\2\u00c7\u00dc\7\u2202"+
		"\2\2\u00c8\u00c9\7h\2\2\u00c9\u00ca\7q\2\2\u00ca\u00cb\7t\2\2\u00cb\u00cc"+
		"\7c\2\2\u00cc\u00cd\7n\2\2\u00cd\u00dc\7n\2\2\u00ce\u00cf\7H\2\2\u00cf"+
		"\u00d0\7Q\2\2\u00d0\u00d1\7T\2\2\u00d1\u00d2\7C\2\2\u00d2\u00d3\7N\2\2"+
		"\u00d3\u00dc\7N\2\2\u00d4\u00d5\7^\2\2\u00d5\u00d6\7h\2\2\u00d6\u00d7"+
		"\7q\2\2\u00d7\u00d8\7t\2\2\u00d8\u00d9\7c\2\2\u00d9\u00da\7n\2\2\u00da"+
		"\u00dc\7n\2\2\u00db\u00c7\3\2\2\2\u00db\u00c8\3\2\2\2\u00db\u00ce\3\2"+
		"\2\2\u00db\u00d4\3\2\2\2\u00dc(\3\2\2\2\u00dd\u00e3\t\4\2\2\u00de\u00df"+
		"\7^\2\2\u00df\u00e0\7i\2\2\u00e0\u00e1\7g\2\2\u00e1\u00e3\7s\2\2\u00e2"+
		"\u00dd\3\2\2\2\u00e2\u00de\3\2\2\2\u00e3*\3\2\2\2\u00e4\u00ea\t\5\2\2"+
		"\u00e5\u00e6\7^\2\2\u00e6\u00e7\7n\2\2\u00e7\u00e8\7g\2\2\u00e8\u00ea"+
		"\7s\2\2\u00e9\u00e4\3\2\2\2\u00e9\u00e5\3\2\2\2\u00ea,\3\2\2\2\u00eb\u00f2"+
		"\7?\2\2\u00ec\u00ed\7g\2\2\u00ed\u00ee\7s\2\2\u00ee\u00ef\7w\2\2\u00ef"+
		"\u00f0\7c\2\2\u00f0\u00f2\7n\2\2\u00f1\u00eb\3\2\2\2\u00f1\u00ec\3\2\2"+
		"\2\u00f2.\3\2\2\2\u00f3\u00f4\7k\2\2\u00f4\u00f7\7p\2\2\u00f5\u00f7\7"+
		"\u220a\2\2\u00f6\u00f3\3\2\2\2\u00f6\u00f5\3\2\2\2\u00f7\60\3\2\2\2\u00f8"+
		"\u00f9\7v\2\2\u00f9\u00fa\7t\2\2\u00fa\u00fb\7c\2\2\u00fb\u00fc\7p\2\2"+
		"\u00fc\u010a\7u\2\2\u00fd\u00fe\7v\2\2\u00fe\u00ff\7t\2\2\u00ff\u0100"+
		"\7c\2\2\u0100\u0101\7p\2\2\u0101\u0102\7u\2\2\u0102\u0103\7k\2\2\u0103"+
		"\u0104\7v\2\2\u0104\u0105\7k\2\2\u0105\u0106\7x\2\2\u0106\u010a\7g\2\2"+
		"\u0107\u0108\7T\2\2\u0108\u010a\7\u207c\2\2\u0109\u00f8\3\2\2\2\u0109"+
		"\u00fd\3\2\2\2\u0109\u0107\3\2\2\2\u010a\62\3\2\2\2\u010b\u010d\t\6\2"+
		"\2\u010c\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e\u010c\3\2\2\2\u010e\u010f"+
		"\3\2\2\2\u010f\64\3\2\2\2\u0110\u0112\5\63\32\2\u0111\u0110\3\2\2\2\u0112"+
		"\u0113\3\2\2\2\u0113\u0111\3\2\2\2\u0113\u0114\3\2\2\2\u0114\u0115\3\2"+
		"\2\2\u0115\u0119\5\r\7\2\u0116\u0118\5\63\32\2\u0117\u0116\3\2\2\2\u0118"+
		"\u011b\3\2\2\2\u0119\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a\66\3\2\2"+
		"\2\u011b\u0119\3\2\2\2\u011c\u011e\n\7\2\2\u011d\u011c\3\2\2\2\u011e\u011f"+
		"\3\2\2\2\u011f\u011d\3\2\2\2\u011f\u0120\3\2\2\2\u01208\3\2\2\2\26\2<"+
		"Q\\fo\u008c\u0099\u00a7\u00c5\u00db\u00e2\u00e9\u00f1\u00f6\u0109\u010e"+
		"\u0113\u0119\u011f\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
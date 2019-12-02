// Generated from /Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/main/antlr4/org/dase/parser/antlr/simple_parser_v6.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link simple_parser_v6Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface simple_parser_v6Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code parseCandidateSolution}
	 * labeled alternative in {@link simple_parser_v6Parser#parsecandidatesolution}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseCandidateSolution(simple_parser_v6Parser.ParseCandidateSolutionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseGroupedCandidateClassesWithProp}
	 * labeled alternative in {@link simple_parser_v6Parser#groupedcandidateclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseGroupedCandidateClassesWithProp(simple_parser_v6Parser.ParseGroupedCandidateClassesWithPropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseGroupedCandidateClasses}
	 * labeled alternative in {@link simple_parser_v6Parser#groupedcandidateclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseGroupedCandidateClasses(simple_parser_v6Parser.ParseGroupedCandidateClassesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseCandidateClassesWithProp}
	 * labeled alternative in {@link simple_parser_v6Parser#parsecandidateclasswithprop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseCandidateClassesWithProp(simple_parser_v6Parser.ParseCandidateClassesWithPropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseCandidateClasses}
	 * labeled alternative in {@link simple_parser_v6Parser#parsecandidateclass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseCandidateClasses(simple_parser_v6Parser.ParseCandidateClassesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseHornClauses}
	 * labeled alternative in {@link simple_parser_v6Parser#parseconjunctivehornclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseHornClauses(simple_parser_v6Parser.ParseHornClausesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parsePosClauses}
	 * labeled alternative in {@link simple_parser_v6Parser#parseposclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParsePosClauses(simple_parser_v6Parser.ParsePosClausesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseNegClauses}
	 * labeled alternative in {@link simple_parser_v6Parser#parsenegclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseNegClauses(simple_parser_v6Parser.ParseNegClausesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseObjProp}
	 * labeled alternative in {@link simple_parser_v6Parser#parseobjectpropertyid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseObjProp(simple_parser_v6Parser.ParseObjPropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseClassId}
	 * labeled alternative in {@link simple_parser_v6Parser#parseclassid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseClassId(simple_parser_v6Parser.ParseClassIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseID}
	 * labeled alternative in {@link simple_parser_v6Parser#parseid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseID(simple_parser_v6Parser.ParseIDContext ctx);
}
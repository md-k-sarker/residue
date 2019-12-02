// Generated from /Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/main/antlr4/org/dase/parser/antlr/simple_parser_v7.g4 by ANTLR 4.7.2

package org.dase.parser.antlr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link simple_parser_v7Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface simple_parser_v7Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code parseCandidateSolution}
	 * labeled alternative in {@link simple_parser_v7Parser#parsecandidatesolution}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseCandidateSolution(simple_parser_v7Parser.ParseCandidateSolutionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseGroupedCandidateClassesWithProp}
	 * labeled alternative in {@link simple_parser_v7Parser#groupedcandidateclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseGroupedCandidateClassesWithProp(simple_parser_v7Parser.ParseGroupedCandidateClassesWithPropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseGroupedCandidateClassesWithoutProp}
	 * labeled alternative in {@link simple_parser_v7Parser#groupedcandidateclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseGroupedCandidateClassesWithoutProp(simple_parser_v7Parser.ParseGroupedCandidateClassesWithoutPropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseCandidateClassWithProp}
	 * labeled alternative in {@link simple_parser_v7Parser#parsecandidateclasswithprop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseCandidateClassWithProp(simple_parser_v7Parser.ParseCandidateClassWithPropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseCandidateClassWithoutProp}
	 * labeled alternative in {@link simple_parser_v7Parser#parsecandidateclass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseCandidateClassWithoutProp(simple_parser_v7Parser.ParseCandidateClassWithoutPropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseConjunctivehornclause}
	 * labeled alternative in {@link simple_parser_v7Parser#parseconjunctivehornclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseConjunctivehornclause(simple_parser_v7Parser.ParseConjunctivehornclauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parsePosClasses}
	 * labeled alternative in {@link simple_parser_v7Parser#parseposclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParsePosClasses(simple_parser_v7Parser.ParsePosClassesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseNegClasses}
	 * labeled alternative in {@link simple_parser_v7Parser#parsenegclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseNegClasses(simple_parser_v7Parser.ParseNegClassesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseObjProp}
	 * labeled alternative in {@link simple_parser_v7Parser#parseobjectpropertyid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseObjProp(simple_parser_v7Parser.ParseObjPropContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseClass}
	 * labeled alternative in {@link simple_parser_v7Parser#parseclassid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseClass(simple_parser_v7Parser.ParseClassContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parseID}
	 * labeled alternative in {@link simple_parser_v7Parser#parseid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseID(simple_parser_v7Parser.ParseIDContext ctx);
}
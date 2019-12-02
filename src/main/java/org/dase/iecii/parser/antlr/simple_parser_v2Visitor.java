// Generated from /Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/main/antlr4/org/dase/parser/antlr/simple_parser_v2.g4 by ANTLR 4.7.2

package org.dase.iecii.parser.antlr;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link simple_parser_v2Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface simple_parser_v2Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link simple_parser_v2Parser#parsecandidatesolution}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParsecandidatesolution(simple_parser_v2Parser.ParsecandidatesolutionContext ctx);
	/**
	 * Visit a parse tree produced by {@link simple_parser_v2Parser#groupedcandidateclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupedcandidateclasses(simple_parser_v2Parser.GroupedcandidateclassesContext ctx);
	/**
	 * Visit a parse tree produced by {@link simple_parser_v2Parser#parsecandidateclasswithprop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParsecandidateclasswithprop(simple_parser_v2Parser.ParsecandidateclasswithpropContext ctx);
	/**
	 * Visit a parse tree produced by {@link simple_parser_v2Parser#parsecandidateclass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParsecandidateclass(simple_parser_v2Parser.ParsecandidateclassContext ctx);
	/**
	 * Visit a parse tree produced by {@link simple_parser_v2Parser#parseconjunctivehornclause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseconjunctivehornclause(simple_parser_v2Parser.ParseconjunctivehornclauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link simple_parser_v2Parser#parseposclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseposclasses(simple_parser_v2Parser.ParseposclassesContext ctx);
	/**
	 * Visit a parse tree produced by {@link simple_parser_v2Parser#parsenegclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParsenegclasses(simple_parser_v2Parser.ParsenegclassesContext ctx);
	/**
	 * Visit a parse tree produced by {@link simple_parser_v2Parser#parseobjectpropertyid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseobjectpropertyid(simple_parser_v2Parser.ParseobjectpropertyidContext ctx);
	/**
	 * Visit a parse tree produced by {@link simple_parser_v2Parser#parseclassid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseclassid(simple_parser_v2Parser.ParseclassidContext ctx);
	/**
	 * Visit a parse tree produced by {@link simple_parser_v2Parser#parseid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParseid(simple_parser_v2Parser.ParseidContext ctx);
}
// Generated from /Users/sarker/Workspaces/Jetbrains/residue/java/residue_java_v1/src/main/antlr4/org/dase/parser/antlr/simple_parser_v7.g4 by ANTLR 4.7.2

package org.dase.parser.antlr;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link simple_parser_v7Visitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class simple_parser_v7BaseVisitor<T> extends AbstractParseTreeVisitor<T> implements simple_parser_v7Visitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParseCandidateSolution(simple_parser_v7Parser.ParseCandidateSolutionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParseGroupedCandidateClassesWithProp(simple_parser_v7Parser.ParseGroupedCandidateClassesWithPropContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParseGroupedCandidateClassesWithoutProp(simple_parser_v7Parser.ParseGroupedCandidateClassesWithoutPropContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParseCandidateClassWithProp(simple_parser_v7Parser.ParseCandidateClassWithPropContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParseCandidateClassWithoutProp(simple_parser_v7Parser.ParseCandidateClassWithoutPropContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParseConjunctivehornclause(simple_parser_v7Parser.ParseConjunctivehornclauseContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParsePosClasses(simple_parser_v7Parser.ParsePosClassesContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParseNegClasses(simple_parser_v7Parser.ParseNegClassesContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParseObjProp(simple_parser_v7Parser.ParseObjPropContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParseClass(simple_parser_v7Parser.ParseClassContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParseID(simple_parser_v7Parser.ParseIDContext ctx) { return visitChildren(ctx); }
}
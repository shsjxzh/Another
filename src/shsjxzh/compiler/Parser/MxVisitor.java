// Generated from .\Mx.g4 by ANTLR 4.7.1

package shsjxzh.compiler.Parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MxParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(MxParser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat(MxParser.StatContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#blockStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStat(MxParser.BlockStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#exprStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStat(MxParser.ExprStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#selectStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectStat(MxParser.SelectStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#elseIfBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfBody(MxParser.ElseIfBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#elseBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseBody(MxParser.ElseBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while}
	 * labeled alternative in {@link MxParser#iterStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(MxParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code for}
	 * labeled alternative in {@link MxParser#iterStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor(MxParser.ForContext ctx);
	/**
	 * Visit a parse tree produced by the {@code break}
	 * labeled alternative in {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak(MxParser.BreakContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continue}
	 * labeled alternative in {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue(MxParser.ContinueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code return}
	 * labeled alternative in {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn(MxParser.ReturnContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#varDeclStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclStat(MxParser.VarDeclStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#emptyStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStat(MxParser.EmptyStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#judgeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJudgeExpr(MxParser.JudgeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#funcDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDecl(MxParser.FuncDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#formalParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameters(MxParser.FormalParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDecl(MxParser.ClassDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassStat(MxParser.ClassStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#constractDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstractDecl(MxParser.ConstractDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#fieldVarDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldVarDecl(MxParser.FieldVarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(MxParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#varInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarInitializer(MxParser.VarInitializerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Call}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall(MxParser.CallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code New}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew(MxParser.NewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Bracket}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracket(MxParser.BracketContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Constant}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(MxParser.ConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code This}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThis(MxParser.ThisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Index}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex(MxParser.IndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Binary}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary(MxParser.BinaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FieldAccess}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldAccess(MxParser.FieldAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IDLeaf}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIDLeaf(MxParser.IDLeafContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unary}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary(MxParser.UnaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PostfixSelfAddSub}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixSelfAddSub(MxParser.PostfixSelfAddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ClassFuncAccess}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassFuncAccess(MxParser.ClassFuncAccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#exprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprList(MxParser.ExprListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(MxParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreator(MxParser.CreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#nonArrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonArrayType(MxParser.NonArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(MxParser.TypeContext ctx);
}
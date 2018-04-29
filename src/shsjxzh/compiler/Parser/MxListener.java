// Generated from .\Mx.g4 by ANTLR 4.7.1

package shsjxzh.compiler.Parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxParser}.
 */
public interface MxListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MxParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MxParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(MxParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(MxParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(MxParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(MxParser.StatContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#blockStat}.
	 * @param ctx the parse tree
	 */
	void enterBlockStat(MxParser.BlockStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#blockStat}.
	 * @param ctx the parse tree
	 */
	void exitBlockStat(MxParser.BlockStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#exprStat}.
	 * @param ctx the parse tree
	 */
	void enterExprStat(MxParser.ExprStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#exprStat}.
	 * @param ctx the parse tree
	 */
	void exitExprStat(MxParser.ExprStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#selectStat}.
	 * @param ctx the parse tree
	 */
	void enterSelectStat(MxParser.SelectStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#selectStat}.
	 * @param ctx the parse tree
	 */
	void exitSelectStat(MxParser.SelectStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#elseIfBody}.
	 * @param ctx the parse tree
	 */
	void enterElseIfBody(MxParser.ElseIfBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#elseIfBody}.
	 * @param ctx the parse tree
	 */
	void exitElseIfBody(MxParser.ElseIfBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#elseBody}.
	 * @param ctx the parse tree
	 */
	void enterElseBody(MxParser.ElseBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#elseBody}.
	 * @param ctx the parse tree
	 */
	void exitElseBody(MxParser.ElseBodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code while}
	 * labeled alternative in {@link MxParser#iterStat}.
	 * @param ctx the parse tree
	 */
	void enterWhile(MxParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code while}
	 * labeled alternative in {@link MxParser#iterStat}.
	 * @param ctx the parse tree
	 */
	void exitWhile(MxParser.WhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code for}
	 * labeled alternative in {@link MxParser#iterStat}.
	 * @param ctx the parse tree
	 */
	void enterFor(MxParser.ForContext ctx);
	/**
	 * Exit a parse tree produced by the {@code for}
	 * labeled alternative in {@link MxParser#iterStat}.
	 * @param ctx the parse tree
	 */
	void exitFor(MxParser.ForContext ctx);
	/**
	 * Enter a parse tree produced by the {@code break}
	 * labeled alternative in {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 */
	void enterBreak(MxParser.BreakContext ctx);
	/**
	 * Exit a parse tree produced by the {@code break}
	 * labeled alternative in {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 */
	void exitBreak(MxParser.BreakContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continue}
	 * labeled alternative in {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 */
	void enterContinue(MxParser.ContinueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continue}
	 * labeled alternative in {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 */
	void exitContinue(MxParser.ContinueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code return}
	 * labeled alternative in {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 */
	void enterReturn(MxParser.ReturnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code return}
	 * labeled alternative in {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 */
	void exitReturn(MxParser.ReturnContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#varDeclStat}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclStat(MxParser.VarDeclStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#varDeclStat}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclStat(MxParser.VarDeclStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#emptyStat}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStat(MxParser.EmptyStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#emptyStat}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStat(MxParser.EmptyStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#judgeExpr}.
	 * @param ctx the parse tree
	 */
	void enterJudgeExpr(MxParser.JudgeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#judgeExpr}.
	 * @param ctx the parse tree
	 */
	void exitJudgeExpr(MxParser.JudgeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#funcDecl}.
	 * @param ctx the parse tree
	 */
	void enterFuncDecl(MxParser.FuncDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#funcDecl}.
	 * @param ctx the parse tree
	 */
	void exitFuncDecl(MxParser.FuncDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameters(MxParser.FormalParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameters(MxParser.FormalParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void enterClassDecl(MxParser.ClassDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void exitClassDecl(MxParser.ClassDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classStat}.
	 * @param ctx the parse tree
	 */
	void enterClassStat(MxParser.ClassStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classStat}.
	 * @param ctx the parse tree
	 */
	void exitClassStat(MxParser.ClassStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#constractDecl}.
	 * @param ctx the parse tree
	 */
	void enterConstractDecl(MxParser.ConstractDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#constractDecl}.
	 * @param ctx the parse tree
	 */
	void exitConstractDecl(MxParser.ConstractDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#fieldVarDecl}.
	 * @param ctx the parse tree
	 */
	void enterFieldVarDecl(MxParser.FieldVarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#fieldVarDecl}.
	 * @param ctx the parse tree
	 */
	void exitFieldVarDecl(MxParser.FieldVarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(MxParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(MxParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#varInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVarInitializer(MxParser.VarInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#varInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVarInitializer(MxParser.VarInitializerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Call}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCall(MxParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Call}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCall(MxParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code New}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNew(MxParser.NewContext ctx);
	/**
	 * Exit a parse tree produced by the {@code New}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNew(MxParser.NewContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Bracket}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBracket(MxParser.BracketContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Bracket}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBracket(MxParser.BracketContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Constant}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConstant(MxParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Constant}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConstant(MxParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code This}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterThis(MxParser.ThisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code This}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitThis(MxParser.ThisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Index}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIndex(MxParser.IndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Index}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIndex(MxParser.IndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Binary}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinary(MxParser.BinaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Binary}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinary(MxParser.BinaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FieldAccess}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFieldAccess(MxParser.FieldAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FieldAccess}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFieldAccess(MxParser.FieldAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IDLeaf}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIDLeaf(MxParser.IDLeafContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IDLeaf}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIDLeaf(MxParser.IDLeafContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unary}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnary(MxParser.UnaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unary}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnary(MxParser.UnaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PostfixSelfAddSub}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPostfixSelfAddSub(MxParser.PostfixSelfAddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PostfixSelfAddSub}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPostfixSelfAddSub(MxParser.PostfixSelfAddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ClassFuncAccess}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterClassFuncAccess(MxParser.ClassFuncAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ClassFuncAccess}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitClassFuncAccess(MxParser.ClassFuncAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#exprList}.
	 * @param ctx the parse tree
	 */
	void enterExprList(MxParser.ExprListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#exprList}.
	 * @param ctx the parse tree
	 */
	void exitExprList(MxParser.ExprListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(MxParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(MxParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(MxParser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(MxParser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#nonArrayType}.
	 * @param ctx the parse tree
	 */
	void enterNonArrayType(MxParser.NonArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#nonArrayType}.
	 * @param ctx the parse tree
	 */
	void exitNonArrayType(MxParser.NonArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MxParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MxParser.TypeContext ctx);
}
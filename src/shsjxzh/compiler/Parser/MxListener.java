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
	 * Enter a parse tree produced by the {@code block}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterBlock(MxParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code block}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitBlock(MxParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterExpression(MxParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitExpression(MxParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code select}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterSelect(MxParser.SelectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code select}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitSelect(MxParser.SelectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code iter}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterIter(MxParser.IterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code iter}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitIter(MxParser.IterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jump}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterJump(MxParser.JumpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jump}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitJump(MxParser.JumpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code vardecl}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterVardecl(MxParser.VardeclContext ctx);
	/**
	 * Exit a parse tree produced by the {@code vardecl}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitVardecl(MxParser.VardeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code empty}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterEmpty(MxParser.EmptyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code empty}
	 * labeled alternative in {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitEmpty(MxParser.EmptyContext ctx);
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
	 * Enter a parse tree produced by {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 */
	void enterJumpStat(MxParser.JumpStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#jumpStat}.
	 * @param ctx the parse tree
	 */
	void exitJumpStat(MxParser.JumpStatContext ctx);
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
	 * Enter a parse tree produced by the {@code var}
	 * labeled alternative in {@link MxParser#classStat}.
	 * @param ctx the parse tree
	 */
	void enterVar(MxParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code var}
	 * labeled alternative in {@link MxParser#classStat}.
	 * @param ctx the parse tree
	 */
	void exitVar(MxParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code func}
	 * labeled alternative in {@link MxParser#classStat}.
	 * @param ctx the parse tree
	 */
	void enterFunc(MxParser.FuncContext ctx);
	/**
	 * Exit a parse tree produced by the {@code func}
	 * labeled alternative in {@link MxParser#classStat}.
	 * @param ctx the parse tree
	 */
	void exitFunc(MxParser.FuncContext ctx);
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
	 * Enter a parse tree produced by the {@code EqualCompare}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEqualCompare(MxParser.EqualCompareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualCompare}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEqualCompare(MxParser.EqualCompareContext ctx);
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
	 * Enter a parse tree produced by the {@code AddMinus}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddMinus(MxParser.AddMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddMinus}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddMinus(MxParser.AddMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitOr}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBitOr(MxParser.BitOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitOr}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBitOr(MxParser.BitOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogOr}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLogOr(MxParser.LogOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogOr}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLogOr(MxParser.LogOrContext ctx);
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
	 * Enter a parse tree produced by the {@code MultiDivModu}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMultiDivModu(MxParser.MultiDivModuContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiDivModu}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMultiDivModu(MxParser.MultiDivModuContext ctx);
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
	 * Enter a parse tree produced by the {@code LogAnd}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLogAnd(MxParser.LogAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogAnd}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLogAnd(MxParser.LogAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralLeaf}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLiteralLeaf(MxParser.LiteralLeafContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralLeaf}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLiteralLeaf(MxParser.LiteralLeafContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitXor}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBitXor(MxParser.BitXorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitXor}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBitXor(MxParser.BitXorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitAnd}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBitAnd(MxParser.BitAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitAnd}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBitAnd(MxParser.BitAndContext ctx);
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
	 * Enter a parse tree produced by the {@code Compare}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCompare(MxParser.CompareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Compare}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCompare(MxParser.CompareContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAssign(MxParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAssign(MxParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code L_R_Shift}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterL_R_Shift(MxParser.L_R_ShiftContext ctx);
	/**
	 * Exit a parse tree produced by the {@code L_R_Shift}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitL_R_Shift(MxParser.L_R_ShiftContext ctx);
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
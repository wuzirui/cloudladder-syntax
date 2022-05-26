package syntax;

import antlr.CL;
import ast.*;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CLAstGenenrator implements antlr.CLListener {
    @Getter
    private Stack<ASTNode> ast = new Stack<>();
    @Getter
    private CLVars vars = new CLVars();
    @Getter
    private List<CLError> errors = new ArrayList<>();
    @Getter
    private CLBlock global;
    private CLBlock currentBlock;
    @Getter
    private CLFunctionDefs funcs = new CLFunctionDefs();

    private CLBlock findNameBlock(String varName) {
        CLBlock current = currentBlock;
        while (current != null) {
            if (vars.contains(varName, current.getName())) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    @Override public void enterExpArrayAccess(CL.ExpArrayAccessContext ctx) { }
    @Override public void exitExpArrayAccess(CL.ExpArrayAccessContext ctx) { }

    @Override public void enterExpFunctionCall(CL.ExpFunctionCallContext ctx) {
        ast.push(new CLFunctionCall());
    }
    @Override public void exitExpFunctionCall(CL.ExpFunctionCallContext ctx) {
        exitExpression();
    }

    @Override public void enterExpPrimaryExpression(CL.ExpPrimaryExpressionContext ctx) { }
    @Override public void exitExpPrimaryExpression(CL.ExpPrimaryExpressionContext ctx) {
        ast.push(new CLPrimaryExpression(ctx.primaryExpression()));
        exitExpression();
    }

    public void exitExpression() {
        ASTNode item = ast.pop();
        if (!(item instanceof CLExpression)) return;
        ASTNode parent = ast.peek();
        if (parent instanceof CLDataDefinition) {
            ((CLDataDefinition) parent).addInitExpression(vars, (CLExpression) item);
        }
        else if (parent instanceof CLAssignmentStatement) {
            ((CLAssignmentStatement) parent).expression = (CLExpression) item;
        }
        else if (parent instanceof CLSelection) {
            ((CLSelection) parent).condition = (CLExpression) item;
        }
        else if (parent instanceof CLReturnStatement) {
            ((CLReturnStatement) parent).value = (CLExpression) item;
        }
        else if (parent instanceof CLLeftAssociativeExpression) {
            if (item instanceof CLLeftAssociativeExpression
                    && ((CLLeftAssociativeExpression) item).op == ((CLLeftAssociativeExpression) parent).op) {
                ((CLLeftAssociativeExpression) parent).operands.addAll(((CLLeftAssociativeExpression) item).operands);
            }
            else {
                ((CLLeftAssociativeExpression) parent).operands.add((CLExpression) item);
            }
        }
        else if (parent instanceof CLFunctionCall) {
            if (((CLFunctionCall) parent).func == null) {
                ((CLFunctionCall) parent).func = (CLExpression) item;
            }
            else {
                ((CLFunctionCall) parent).args.add((CLExpression) item);
            }
        }
        else if (parent instanceof CLUnaryExpression) {
            ((CLUnaryExpression) parent).operand = (CLExpression) item;
        }
        else if (parent instanceof CLExpressionStatement) {
            ((CLExpressionStatement) parent).expression = (CLExpression) item;
        }
        else {
            throw new RuntimeException("unexpected ast structure\n" + parent);
        }
    }

    @Override public void enterExpUnaryExpression(CL.ExpUnaryExpressionContext ctx) {
        ast.push(new CLUnaryExpression(ctx.op.getText()));
    }
    @Override public void exitExpUnaryExpression(CL.ExpUnaryExpressionContext ctx) {
        exitExpression();
    }

    @Override public void enterExpFieldAccess(CL.ExpFieldAccessContext ctx) { }
    @Override public void exitExpFieldAccess(CL.ExpFieldAccessContext ctx) { }

    @Override public void enterExpPipeAndArrow(CL.ExpPipeAndArrowContext ctx) { }
    @Override public void exitExpPipeAndArrow(CL.ExpPipeAndArrowContext ctx) { }

    @Override public void enterExpFunctionExpression(CL.ExpFunctionExpressionContext ctx) {
    }
    @Override public void exitExpFunctionExpression(CL.ExpFunctionExpressionContext ctx) { }

    @Override public void enterExpBinaryExpression(CL.ExpBinaryExpressionContext ctx) {
        ast.push(new CLLeftAssociativeExpression(ctx.op.getText()));
    }
    @Override public void exitExpBinaryExpression(CL.ExpBinaryExpressionContext ctx) {
        exitExpression();
    }

    @Override public void enterArgumentList(CL.ArgumentListContext ctx) { }
    @Override public void exitArgumentList(CL.ArgumentListContext ctx) { }

    @Override public void enterPeLiteral(CL.PeLiteralContext ctx) { }
    @Override public void exitPeLiteral(CL.PeLiteralContext ctx) { }

    @Override public void enterPeArrayLiteral(CL.PeArrayLiteralContext ctx) { }
    @Override public void exitPeArrayLiteral(CL.PeArrayLiteralContext ctx) { }

    @Override public void enterPeObjLiteral(CL.PeObjLiteralContext ctx) { }
    @Override public void exitPeObjLiteral(CL.PeObjLiteralContext ctx) { }

    @Override public void enterPeParenExpression(CL.PeParenExpressionContext ctx) { }
    @Override public void exitPeParenExpression(CL.PeParenExpressionContext ctx) { }

    @Override public void enterPeIdentifier(CL.PeIdentifierContext ctx) { }
    @Override public void exitPeIdentifier(CL.PeIdentifierContext ctx) { }

    @Override public void enterPeNumberLiteral(CL.PeNumberLiteralContext ctx) { }
    @Override public void exitPeNumberLiteral(CL.PeNumberLiteralContext ctx) { }

    @Override public void enterPeBoolLiteral(CL.PeBoolLiteralContext ctx) { }
    @Override public void exitPeBoolLiteral(CL.PeBoolLiteralContext ctx) { }

    @Override public void enterPeStringLiteral(CL.PeStringLiteralContext ctx) { }
    @Override public void exitPeStringLiteral(CL.PeStringLiteralContext ctx) { }

    @Override public void enterArrayLiteral(CL.ArrayLiteralContext ctx) { }
    @Override public void exitArrayLiteral(CL.ArrayLiteralContext ctx) { }

    @Override public void enterObjLiteral(CL.ObjLiteralContext ctx) { }
    @Override public void exitObjLiteral(CL.ObjLiteralContext ctx) { }

    @Override public void enterStaticObjItem(CL.StaticObjItemContext ctx) { }
    @Override public void exitStaticObjItem(CL.StaticObjItemContext ctx) { }

    @Override public void enterStringObjItem(CL.StringObjItemContext ctx) { }
    @Override public void exitStringObjItem(CL.StringObjItemContext ctx) { }

    @Override public void enterDynamicObjItem(CL.DynamicObjItemContext ctx) { }
    @Override public void exitDynamicObjItem(CL.DynamicObjItemContext ctx) { }

    @Override public void enterStatement(CL.StatementContext ctx) { }
    @Override public void exitStatement(CL.StatementContext ctx) { }

    @Override public void enterExpressionStatement(CL.ExpressionStatementContext ctx) {
        ast.push(new CLExpressionStatement());
    }
    @Override public void exitExpressionStatement(CL.ExpressionStatementContext ctx) {
        CLExpressionStatement item = (CLExpressionStatement) ast.pop();
        ASTNode parent = ast.peek();
        if (parent instanceof CLProgram) {
            ((CLProgram) parent).addItem(item);
        }
        else if (parent instanceof CLCompoundStatment) {
            ((CLCompoundStatment) parent).getChildren().add(item);
        }
        else {
            throw new RuntimeException("unknown ast structure, " + parent);
        }
    }

    @Override public void enterReturnStatement(CL.ReturnStatementContext ctx) {
        ast.push(new CLReturnStatement());
    }
    @Override public void exitReturnStatement(CL.ReturnStatementContext ctx) {
        CLReturnStatement item = (CLReturnStatement) ast.pop();
        assert ast.peek() instanceof CLCompoundStatment;
        CLCompoundStatment parent = (CLCompoundStatment) ast.peek();
        parent.getChildren().add(item);
    }

    @Override public void enterBreakStatement(CL.BreakStatementContext ctx) { }
    @Override public void exitBreakStatement(CL.BreakStatementContext ctx) { }

    @Override public void enterContinueStatement(CL.ContinueStatementContext ctx) { }
    @Override public void exitContinueStatement(CL.ContinueStatementContext ctx) { }

    @Override public void enterDataStatement(CL.DataStatementContext ctx) { }
    @Override public void exitDataStatement(CL.DataStatementContext ctx) { }

    @Override public void enterDataStatementItemList(CL.DataStatementItemListContext ctx) { }
    @Override public void exitDataStatementItemList(CL.DataStatementItemListContext ctx) { }

    @Override public void enterDataStatementItem(CL.DataStatementItemContext ctx) {
        String name = ctx.Identifier().getText();

        if (findNameBlock(name) != null) {
            errors.add(new CLError("ES02", "var " + name +" has multiple definitions"));
        }
        // 默认将初始化设为假
        // TODO: 完善初始化
        vars.insert(name, currentBlock.getName(), false);

        ast.push(new CLDataDefinition(name, false, currentBlock));

        System.out.println("recognized name = " + name);
    }
    @Override public void exitDataStatementItem(CL.DataStatementItemContext ctx) {
        CLDataDefinition item = (CLDataDefinition) ast.pop();
        var parent = ast.peek();
        if (parent instanceof CLProgram) {
            ((CLProgram) ast.peek()).addItem(item);
        }
        else {
            throw new RuntimeException("unexpected ast structure");
        }
    }

    @Override public void enterCompoundStatement(CL.CompoundStatementContext ctx) {
        CLBlock namespace = null;
        ASTNode parent = ast.peek();
        if (parent instanceof CLFunctionDef) {
            // 在函数构造函数中时已经创建了新的名字空间
            namespace = ((CLFunctionDef) parent).getBody();
            ast.push(new CLCompoundStatment(namespace));
            currentBlock = namespace;
            assert currentBlock.getParent() == ((CLFunctionDef) parent).parent;
        }
        else if (parent instanceof CLSelection) {
            namespace = new CLBlock();
            namespace.setParent(currentBlock);
            ast.push(new CLCompoundStatment(namespace));
            currentBlock = namespace;
        }
        else {
            throw new RuntimeException("unexpected ast structure\n" + ast);
        }

    }
    @Override public void exitCompoundStatement(CL.CompoundStatementContext ctx) {
        CLCompoundStatment item = (CLCompoundStatment) ast.pop();
        if (currentBlock != null)
            currentBlock = currentBlock.getParent();
        var parent = ast.peek();
        if (parent instanceof CLFunctionDef) {
            ((CLFunctionDef) ast.peek()).children = item;
        }
        else if (parent instanceof CLSelection) {
            if (((CLSelection) parent).ThenClause == null) {
                ((CLSelection) parent).ThenClause = item;
            }
            else if (((CLSelection) parent).ElseClause == null){
                ((CLSelection) parent).ElseClause = item;
            }
            else {
                throw new RuntimeException("unexpected else clause" + ast);
            }
        }
        else {
            throw new RuntimeException("unexpected ast structure\n" + ast);
        }
    }

    @Override public void enterSelectionStatement(CL.SelectionStatementContext ctx) {
        ast.push(new CLSelection());
    }

    @Override public void exitSelectionStatement(CL.SelectionStatementContext ctx) {
        CLSelection item = (CLSelection) ast.pop();
        var parent = ast.peek();
        if (parent instanceof CLProgram) {
            ((CLProgram) parent).addItem(item);
        }
        else if (parent instanceof CLCompoundStatment) {
            ((CLCompoundStatment) parent).getChildren().add(item);
        }
        else {
            throw new RuntimeException("unexpected ast structure\n" + ast);
        }
    }
    @Override public void enterAssignmentStatement(CL.AssignmentStatementContext ctx) {
        ast.push(new CLAssignmentStatement());
    }
    @Override public void exitAssignmentStatement(CL.AssignmentStatementContext ctx) {
        CLAssignmentStatement item = (CLAssignmentStatement) ast.pop();
        var parent = ast.peek();
        if (parent instanceof CLProgram) {
            ((CLProgram) ast.peek()).addItem(item);
        }
        else {
            throw new RuntimeException("unexpected ast structure");
        }
    }
    @Override public void enterAssignArray2(CL.AssignArray2Context ctx) { }
    @Override public void exitAssignArray2(CL.AssignArray2Context ctx) { }
    @Override public void enterAssIdentifier(CL.AssIdentifierContext ctx) { }
    @Override public void exitAssIdentifier(CL.AssIdentifierContext ctx) {
        CLAssignmentStatement parent = (CLAssignmentStatement) ast.peek();
        String name = ctx.Identifier().getText();
        CLBlock block = findNameBlock(name);
        if (block == null) {
            errors.add(new CLError("ES01", "var " + name + " not defined"));
        }
        parent.dest = new CLIdentifier(name);
    }
    @Override public void enterAssignField1(CL.AssignField1Context ctx) { }
    @Override public void exitAssignField1(CL.AssignField1Context ctx) { }
    @Override public void enterAssignField2(CL.AssignField2Context ctx) { }
    @Override public void exitAssignField2(CL.AssignField2Context ctx) { }
    @Override public void enterAssignArray1(CL.AssignArray1Context ctx) { }
    @Override public void exitAssignArray1(CL.AssignArray1Context ctx) { }
    @Override public void enterAssignmentOperator(CL.AssignmentOperatorContext ctx) { }
    @Override public void exitAssignmentOperator(CL.AssignmentOperatorContext ctx) { }
    @Override public void enterWhileStatement(CL.WhileStatementContext ctx) { }
    @Override public void exitWhileStatement(CL.WhileStatementContext ctx) { }
    @Override public void enterForStatement(CL.ForStatementContext ctx) { }
    @Override public void exitForStatement(CL.ForStatementContext ctx) { }
    @Override public void enterForDeclStatement(CL.ForDeclStatementContext ctx) { }
    @Override public void exitForDeclStatement(CL.ForDeclStatementContext ctx) { }
    @Override public void enterFunctionDefinition(CL.FunctionDefinitionContext ctx) {
        String name = ctx.Identifier().getText();
        if (funcs.contains(name, currentBlock.getName())) {
            errors.add(new CLError("ES01", "function multiple def"));
        }
        ast.push(new CLFunctionDef(name, currentBlock));
    }

    @Override public void exitFunctionDefinition(CL.FunctionDefinitionContext ctx) {
        CLFunctionDef item = (CLFunctionDef) ast.pop();
        funcs.add(item);
        currentBlock.getChildren().add(item.getBody());
        currentBlock = item.getBody();

        if (ast.peek() instanceof CLProgram) {
            ((CLProgram) ast.peek()).addItem(item);
        }
    }
    @Override public void enterParamList(CL.ParamListContext ctx) {
        var parent = ast.peek();
        for (var item : ctx.Identifier()) {
            if (parent instanceof CLFunctionDef) {
                ((CLFunctionDef) parent).addArg(vars, item.getText());
            }
        }
    }
    @Override public void exitParamList(CL.ParamListContext ctx) { }
    @Override public void enterImportStatement(CL.ImportStatementContext ctx) { }
    @Override public void exitImportStatement(CL.ImportStatementContext ctx) { }
    @Override public void enterExportStatement(CL.ExportStatementContext ctx) { }
    @Override public void exitExportStatement(CL.ExportStatementContext ctx) { }

    @Override public void enterProgram(CL.ProgramContext ctx) {
        ast.push(new CLProgram());
        global = new CLBlock();
        currentBlock = global;
    }

    @Override public void exitProgram(CL.ProgramContext ctx) { }
    @Override public void enterProgramItem(CL.ProgramItemContext ctx) { }
    @Override public void exitProgramItem(CL.ProgramItemContext ctx) { }

    @Override public void enterEveryRule(ParserRuleContext ctx) { }
    @Override public void exitEveryRule(ParserRuleContext ctx) { }
    @Override public void visitTerminal(TerminalNode node) { }
    @Override public void visitErrorNode(ErrorNode node) { }
}

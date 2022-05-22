/**
 * @ author Markus Bowie
 * @ author Noël Hennings
 */
package prop.assignment0;

import java.io.IOException;

class Parser implements IParser {

    private Tokenizer t = null;

    public void open(String fileName) throws IOException, TokenizerException {
        t = new Tokenizer();
        t.open(fileName);
        t.moveNext();
    }

    public INode parse() throws IOException, TokenizerException, ParserException {
        if (t == null)
            throw new IOException("No open file.");
        return new AssignmentNode(t);
    }

    public void close() throws IOException {
        if (t != null)
            t.close();
    }

    class AssignmentNode implements INode {
        IdNode id;
        Lexeme idLex = null;
        Lexeme assign = null;
        ExpressionNode exp;
        Lexeme semi = null;

        StringBuilder strBuilder = new StringBuilder();

        AssignmentNode(Tokenizer t) throws IOException, TokenizerException, ParserException {
            idLex = t.current();
            id = new IdNode(t);
            if (t.current().token() != Token.ASSIGN_OP) {
                throw new ParserException("missing assign");
            }
            assign = t.current();
            t.moveNext();
            exp = new ExpressionNode(t);
            if (t.current().token() != Token.SEMICOLON) {
                throw new ParserException("missing semi: " + t.current());
            }
            semi = t.current();
            t.moveNext();
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            builder.append("AssignmentNode\n");
            id.buildString(builder, 8);
            builder.append("\n");
            addTabs(builder, 8);
            builder.append(assign);
            builder.append("\n");
            addTabs(builder, 8);
            builder.append("ExpressionNode");
            exp.buildString(builder, tabs);
            builder.append("\n");
            builder.append(semi);
        }
    }

    // nån expression behöver + och -
    class ExpressionNode implements INode {
        TermNode term = null;
        ArithmeticExpressionNode aExp = null;

        ExpressionNode(Tokenizer t) throws IOException, TokenizerException, ParserException {
            term = new TermNode(t);
            if (t.current().token() == Token.ADD_OP || t.current().token() == Token.SUB_OP) {
                aExp = new ArithmeticExpressionNode(t);
            }
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            builder.append("\n");
            addTabs(builder, 16);
            builder.append("TermNode");
            addTabs(builder, tabs);
            term.buildString(builder, tabs + 8);
            if (aExp != null) {
                aExp.buildString(builder, tabs + 8);
            }
        }
    }

    class ArithmeticExpressionNode implements INode {
        Lexeme operator = null;
        ExpressionNode expN = null;

        public ArithmeticExpressionNode(Tokenizer t) throws IOException, TokenizerException, ParserException {
            if (t.current().token() == Token.ADD_OP || t.current().token() == Token.SUB_OP) {
                operator = t.current();
                t.moveNext();
                expN = new ExpressionNode(t);
            } else {
                throw new ParserException(t.current().value() + " not valid arithmetic expression");
            }
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            builder.append("\n");
            addTabs(builder, 8);
            builder.append(operator);
            builder.append("\n");
            addTabs(builder, 8);
            builder.append("ExpressionNode");
            addTabs(builder, tabs);
            expN.buildString(builder, tabs );
        }
    }

    class TermNode implements INode {
        FactorNode factor = null;
        OperatorTermNode opTerm = null;


        public TermNode(Tokenizer t) throws IOException, TokenizerException, ParserException {
            factor = new FactorNode(t);
            if (t.current().token() == Token.MULT_OP || t.current().token() == Token.DIV_OP) {
                opTerm = new OperatorTermNode(t);
            }
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            builder.append("\n");
            addTabs(builder, tabs + 16);
            builder.append("FactorNode");
            addTabs(builder, tabs);
            factor.buildString(builder, tabs);
            if (opTerm != null) {
                opTerm.buildString(builder, tabs + 8);
            }
        }
    }

    class OperatorTermNode implements INode {
        Lexeme termOp = null;
        TermNode term = null;

        public OperatorTermNode(Tokenizer t) throws IOException, TokenizerException, ParserException {
            if (t.current().token() != Token.DIV_OP && t.current().token() != Token.MULT_OP) {
                throw new ParserException(t.current().value() + " not term operator");
            }
            termOp = t.current();
            t.moveNext();
            term = new TermNode(t);
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            builder.append("\n");
            addTabs(builder, tabs + 8);
            builder.append(termOp);
            builder.append("\n");
            //builder.append("        ");
            addTabs(builder, tabs + 8);
            builder.append("TermNode");

            term.buildString(builder, tabs);
        }
    }

    class FactorNode implements INode {
        IntNode intNode = null;
        ExpressionNode expN = null;
        Lexeme p1 = null;
        Lexeme p2 = null;

        public FactorNode(Tokenizer t) throws IOException, TokenizerException, ParserException {
            if (t.current().token() == Token.INT_LIT) {
                intNode = new IntNode(t);
            } else {
                if (t.current().token() == Token.LEFT_PAREN) {
                    p1 = t.current();
                    t.moveNext();
                    expN = new ExpressionNode(t);
                    p2 = t.current();
                    t.moveNext();
                } else {
                    expN = new ExpressionNode(t);
                }
            }
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            if (intNode != null) {
                addTabs(builder, tabs + 8);
                intNode.buildString(builder, tabs);
            } else {
                if (p1 != null) {
                    builder.append("\n");
                    addTabs(builder, tabs + 8);
                    builder.append(p1);
                }
                expN.buildString(builder, tabs + 8);
                if (p2 != null) {
                    builder.append("\n");
                    addTabs(builder, tabs + 8);
                    builder.append(p2);
                }
            }
        }
    }

    class IdNode implements INode {
        String token = t.current().toString();
        Lexeme l = null;

        public IdNode(Tokenizer t) throws IOException, TokenizerException {
            l = t.current();
            t.moveNext();
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            addTabs(builder, 8);
            builder.append(l);
        }
    }

    class IntNode implements INode {
        Lexeme l = null;

        public IntNode(Tokenizer t) throws IOException, TokenizerException, ParserException {
            if (t.current().token() != Token.INT_LIT) {
                throw new ParserException("not int literal " + t.current().value());
            }
            l = t.current();
            t.moveNext();
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            builder.append("\n");
            builder.append("        ");
            addTabs(builder, tabs);
            builder.append(l);
        }
    }


//    public static void output(Lexeme lexeme, StringBuilder builder, int tabs) {
//        output(lexeme.value(), builder, tabs);
//    }

//    public static void output(Object value, StringBuilder builder, int tabs) {
//        builder.append("\n");
//        addTabs(builder, tabs);
//        builder.append(value);
//    }

    public static void addTabs(StringBuilder builder, int tabs) {
        for (int i = 0; i < tabs; i++) {
            builder.append(" ");
        }
    }
}

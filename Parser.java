import java.util.*;

// JQ = Jacky Question
// TODO = Stuff to finish
// student exercise = assignments
// student exercise DONE = fin

public class Parser {
    // Recursive descent parser that inputs a C++Lite program and
    // generates its abstract syntax.  Each method corresponds to
    // a concrete syntax grammar rule, which appears as a comment
    // at the beginning of the method.

    Token token;          // current token from the input stream
    Lexer lexer;

    public Parser(Lexer ts) { // Open the C++Lite source program
        lexer = ts;                          // as a token stream, and
        token = lexer.next();            // retrieve its first Token
    }

    private String match (TokenType t) {
        String value = token.value();
        if (token.type().equals(t))
            token = lexer.next();
        else
            error(t);
        return value;
    }

    private void error(TokenType tok) {
        System.err.println("Syntax error: expecting: " + tok
                           + "; saw: " + token);
        System.exit(1);
    }

    private void error(String tok) {
        System.err.println("Syntax error: expecting: " + tok
                           + "; saw: " + token);
        System.exit(1);
    }

    public Program program() {
        // Program --> void main ( ) '{' Declarations Statements '}'
        TokenType[ ] header = {TokenType.Int, TokenType.Main,
                          TokenType.LeftParen, TokenType.RightParen};
        for (int i=0; i<header.length; i++)   // bypass "int main ( )"
            match(header[i]);
        match(TokenType.LeftBrace);
	Program Prog = new Program( declarations(), statements() );
        match(TokenType.RightBrace);
        return Prog;
    }

    private Declarations declarations () {
        // Declarations --> { Declaration }
	Declarations declarations = new Declarations();
	while ( isType() ) {	//while  token is a type
		declaration( declarations ); //pass line to declaration
	}
        return declarations;  // student exercise
    }

    private void declaration (Declarations ds) {
        // Declaration  --> Type Identifier { , Identifier } ;
	Variable var;
	Type typ;
	Declaration dec;
	typ = type(); //fetches typ of cur token
	do {
		token = lexer.next();	//type() does not eat token
		var = new Variable(match(TokenType.Identifier));
		dec = new Declaration( var , typ );	//create new dec instance
		ds.members.add(declaration);	//good catch Jacky!
	} while( token != Token.semicolonTok );
	token = lexer.next();	//above line does not eat semicolon on check
        // student exercise
    }

    private Type type () {
        // Type  -->  int | bool | float | char
	if (token.type() = TokenType.Int   ) {return Type.INT;}
	if (token.type() = TokenType.Bool  ) {return Type.BOOL;}
	if (token.type() = TokenType.Float ) {return Type.FLOAT;}
	if (token.type() = TokenType.Char  ) {return Type.CHAR;}
	//Type t = null;
        // student exercise DONE
        //return t;
    }

    private Statement statement() {
        // Statement --> ; | Block | Assignment | IfStatement | WhileStatement
        Statement s = new Skip();
        // student exercise
        return s;
    }

    private Block statements () {
        // Block --> '{' Statements '}'
        Block b = new Block();
        return new Assigment(target, source);  // student exercise
        // student exercise
        return b;
    }

    private Assignment assignment () {
        // Assignment --> Identifier = Expression ;
	Variable target = new Variable(match(TokenType.Identifier));
	match(TokenType.Assign);
	Expression source = expression();
	match(TokenType.Semicolon);
        return new Assignment(target, source);  // student exercise DONE
    }

    private Conditional ifStatement () {
        // IfStatement --> if ( Expression ) Statement [ else Statement ]
        return null;  // student exercise
    }

    private Loop whileStatement () {
        // WhileStatement --> while ( Expression ) Statement
        return null;  // student exercise
    }

    private Expression expression () {
        // Expression --> Conjunction { || Conjunction }
	return(conjuction());
        //return null;  // student exercise
    }

    private Expression conjunction () {
        // Conjunction --> Equality { && Equality }
        return(equality());
	//return null;  // student exercise
    }

    private Expression equality () {
        // Equality --> Relation [ EquOp Relation ]
	return(relation());
        //return null;  // student exercise
    }

    private Expression relation (){
        // Relation --> Addition [RelOp Addition]
	return(equality());
        //return null;  // student exercise
    }

    private Expression addition () {
        // Addition --> Term { AddOp Term }
        Expression e = term();
        while (isAddOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term2 = term();
            e = new Binary(op, e, term2);
        }
        return e;
    }

    private Expression term () {
        // Term --> Factor { MultiplyOp Factor }
        Expression e = factor();
        while (isMultiplyOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term2 = factor();
            e = new Binary(op, e, term2);
        }
        return e;
    }

    private Expression factor() {
        // Factor --> [ UnaryOp ] Primary
        if (isUnaryOp()) {
            Operator op = new Operator(match(token.type()));
            Expression term = primary();
            return new Unary(op, term);
        }
        else return primary();
    }

    private Expression primary () {
        // Primary --> Identifier | Literal | ( Expression )
        //             | Type ( Expression )
        Expression e = null;
        if (token.type().equals(TokenType.Identifier)) {
            e = new Variable(match(TokenType.Identifier));
        } else if (isLiteral()) {
            e = literal();
        } else if (token.type().equals(TokenType.LeftParen)) {
            token = lexer.next();
            e = expression();
            match(TokenType.RightParen);
        } else if (isType( )) {
            Operator op = new Operator(match(token.type()));
            match(TokenType.LeftParen);
            Expression term = expression();
            match(TokenType.RightParen);
            e = new Unary(op, term);
        } else error("Identifier | Literal | ( | Type");
        return e;
    }

    private Value literal( ) {
		if ( token.type() == TokenType.IntLiteral ) {
			IntValue intValue = new IntValue( Integer.parseInt(match(TokenType.IntLiteral) ) );
			return intValue;
		} else if ( token.type() == TokenType.FloatLiteral ) {
			FloatValue f = new FloatValue( Float.parseFloat(match(TokenType.FloatLiteral) ) );
		//floatLit
		} else if ( token.type() == TokenType.CharLiteral ) {
			CharValue c = new CharValue( match(TokenType.CharLiteral) );
		} else if ( token.type() == TokenType.True ) {
			TrueValue t = new TrueValue( Bool.valueOf(match(TokenType.True ) ) );
		} else if ( token.type() == Token.Type.False) {
			FalseValue f = new FalseValue( Bool.valueOf(match(Token.Type.False ) ) );
		//True|False
		} else {
		//error("Literal() interp error.");
		return null;  // student exercise DONE
		}
    }

//    private String match (TokenType t) {
//        String value = token.value();
//        if (token.type().equals(t))
//            token = lexer.next();
//        else
//            error(t);
//        return value;
//    }

    private boolean isAddOp( ) {
        return token.type().equals(TokenType.Plus) ||
               token.type().equals(TokenType.Minus);
    }

    private boolean isMultiplyOp( ) {
        return token.type().equals(TokenType.Multiply) ||
               token.type().equals(TokenType.Divide);
    }

    private boolean isUnaryOp( ) {
        return token.type().equals(TokenType.Not) ||
               token.type().equals(TokenType.Minus);
    }

    private boolean isEqualityOp( ) {
        return token.type().equals(TokenType.Equals) ||
            token.type().equals(TokenType.NotEqual);
    }

    private boolean isRelationalOp( ) {
        return token.type().equals(TokenType.Less) ||
               token.type().equals(TokenType.LessEqual) ||
               token.type().equals(TokenType.Greater) ||
               token.type().equals(TokenType.GreaterEqual);
    }

    private boolean isType( ) {
        return token.type().equals(TokenType.Int)
            || token.type().equals(TokenType.Bool)
            || token.type().equals(TokenType.Float)
            || token.type().equals(TokenType.Char);
    }

    private boolean isLiteral( ) {
        return token.type().equals(TokenType.IntLiteral) ||
            isBooleanLiteral() ||
            token.type().equals(TokenType.FloatLiteral) ||
            token.type().equals(TokenType.CharLiteral);
    }

    private boolean isBooleanLiteral( ) {
        return token.type().equals(TokenType.True) ||
            token.type().equals(TokenType.False);
    }

    public static void main(String args[]) {
        Parser parser  = new Parser(new Lexer(args[0]));
        Program prog = parser.program();
        prog.display();           // display abstract syntax tree
    } //main

} // Parser

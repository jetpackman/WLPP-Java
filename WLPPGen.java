import java.util.*;

public class WLPPGen {
    Scanner in = new Scanner(System.in);
    int currentVariableCount = 1;
    int loopNumber = 1;
    int ifNumber = 1;
    // The set of terminal symbols in the WLPP grammar.
    Set<String> terminals = new HashSet<String>(Arrays.asList("BOF", "BECOMES", 
         "COMMA", "ELSE", "EOF", "EQ", "GE", "GT", "ID", "IF", "INT", "LBRACE", 
         "LE", "LPAREN", "LT", "MINUS", "NE", "NUM", "PCT", "PLUS", "PRINTLN",
         "RBRACE", "RETURN", "RPAREN", "SEMI", "SLASH", "STAR", "WAIN", "WHILE",
         "AMP", "LBRACK", "RBRACK", "NEW", "DELETE", "NULL"));
    List<String> symbols;
    HashMap<String,Pair> symbolTable = new HashMap<String,Pair>();

    String intString = "int";
    String intStarString = "int*";
    // Data structure for storing the parse tree.
    public class Tree {
        List<String> rule;
        String type="tester";
        ArrayList<Tree> children = new ArrayList<Tree>();

        // Does this node's rule match otherRule?
        boolean matches(String otherRule) {
            return tokenize(otherRule).equals(rule);
        }
    }
    
    public class Pair {
		int line;
		String type;
		
		public Pair (int line, String type){
			this.line = line;
			this.type = type;
		}
		
		public int hashCode(){	
			return (line+type).hashCode();
		}
		
		public boolean equals(Object obj){
			Pair p = (Pair)(obj);
			return (p.type.equals(this.type) && p.line == (this.line));
		}
		
		public String toString(){
			return type;
			//return ("Variable:" + type + " " + line);
		}
	}

    // Divide a string into a list of tokens.
    List<String> tokenize(String line) {
        List<String> ret = new ArrayList<String>();
        Scanner sc = new Scanner(line);
        while (sc.hasNext()) {
            ret.add(sc.next());
        }
        return ret;
    }

    // Read and return wlppi parse tree
    Tree readParse(String lhs) {
        String line = in.nextLine();
        List<String> tokens = tokenize(line);
        Tree ret = new Tree();
        ret.rule = tokens;
        if (!terminals.contains(lhs)) {
            Scanner sc = new Scanner(line);
            sc.next(); // discard lhs
            while (sc.hasNext()) {
                String s = sc.next();
                ret.children.add(readParse(s));
            }
        }
        return ret;
    }

    // Compute symbols defined in t
    List<String> genSymbols(Tree t) {
        return null;
    }
 

    // Print an error message and exit the program.
    void bail(String msg) {
        System.err.println("ERROR: " + msg);
        System.exit(0);
    }

    // Generate the code for the parse tree t.
    String genCode(Tree t) {
        return null;
    }

    // Main program
    public static final void main(String args[]) {
        new WLPPGen().go();
    }
    
    void lis(int register){
    	System.out.println(String.format("lis $%d",register));

    }
    
    void word(int value){
    	System.out.println(String.format(".word %d",value));
    }
    
    void word(String label){
    	System.out.println(String.format(".word %s",label));
    }
    
    void add(int reg1, int reg2, int reg3){
    	System.out.println(String.format("add $%d, $%d, $%d",reg1, reg2, reg3));
    }
    
    void sub(int reg1, int reg2, int reg3){
    	System.out.println(String.format("sub $%d, $%d, $%d",reg1, reg2, reg3));
    }
    
    void mult(int reg1,int reg2){
    	System.out.println(String.format("mult $%d, $%d",reg1, reg2));
    }
    
    void div (int reg1,int reg2){
    	System.out.println(String.format("div $%d, $%d",reg1, reg2));
    }
    
    void mflo (int reg){
    	System.out.println(String.format("mflo $%d",reg));
    }
    
    void mfhi (int reg){
    	System.out.println(String.format("mfhi $%d",reg));
    }
    
    void sw(int reg1, int offset, int reg2){
    	System.out.println(String.format("sw $%d, %d($%d)",reg1, offset, reg2));
    }
    
    void lw(int reg1, int offset, int reg2){
    	System.out.println(String.format("lw $%d, %d($%d)",reg1, offset, reg2));
    }
    
    void slt(int reg1, int reg2, int reg3){
    	System.out.println(String.format("slt $%d, $%d, $%d",reg1, reg2, reg3));
    }
    void sltu(int reg1, int reg2, int reg3){
    	System.out.println(String.format("slt $%d, $%d, $%d",reg1, reg2, reg3));
    }
    
    void beq(int reg1, int reg2, String label){
    	System.out.println(String.format("beq $%d, $%d, %s",reg1, reg2, label));
    }
    
    void bne(int reg1, int reg2, String label){
    	System.out.println(String.format("bne $%d, $%d, %s",reg1, reg2, label));

    }
    
    void jr(int reg){
    	System.out.println(String.format("jr $%d",reg));
    }
    
    void jalr(int reg){
    	System.out.println(String.format("jalr $%d",reg));
    }
    
    
    void pop(int register){
    	add(30,30,4);
    	lw(register,-4,30);
    }
    
    void push(int register){
    	sw(register,-4,30);
    	sub(30,30,4);
    }
    
    void generateTree(Tree parseTree){
    	System.out.println(".import print");
    	System.out.println(".import init");
    	System.out.println(".import new");
    	System.out.println(".import delete");

    	lis(4);
    	word(4);
    	lis(11);
    	word (1);
    	add (29,30,0);
    	currentVariableCount = 1;
    	generateDCL(parseTree.children.get(3));
    	add(1,2,0);
    	generateDCL(parseTree.children.get(5));
    	generateDCLS(parseTree.children.get(8));
    	// Save return pointer and move stack up;
    	sw (31,currentVariableCount*-4,30);
    	lis(5);
    	word (currentVariableCount*-4);
    	add(30,30,5);
    	
    	lw(1,-4,29);
    	lw(2,-8,29);
    	
    	if (parseTree.children.get(3).children.get(0).children.size()==1){
    		add(2,0,0);
    	}
    	
    	lis (6);
    	word("init");
    	jalr(6);
    	generateStatements(parseTree.children.get(9));
    	generateExpr(parseTree.children.get(11));
    	generateReturn();
    }
    
    void generateReturn(){
    	lw(31,currentVariableCount *-4,29);
    	jr(31);
    }
    
    void generateDCL(Tree tree){
    	sw(1,currentVariableCount*-4,30);
    	currentVariableCount++;
    }
    void generateDCL2(Tree tree){
    	sw(2,currentVariableCount*-4,30);
    	currentVariableCount++;
    }
    
    void generateDCLS (Tree tree){
    	if (tree.children.size()==0){
    		return;
    	}
    	
    	generateDCLS(tree.children.get(0));
    	//String type = processDCL(parseTree.children.get(1));
    	
    	String becomes = tree.rule.get(4);
    	if (becomes.equals("NUM")){
			int numValue = Integer.parseInt(tree.children.get(3).rule.get(1));
			lis(1);
			word(numValue);
    		generateDCL(tree.children.get(1));
    	}    	
    	
    	if (becomes.equals("NULL")){
    		add(1,0,0);
    		generateDCL(tree.children.get(1));
    	}
    }

    
    void processTree(Tree parseTree){
    	for (int i = 0; i< parseTree.children.size(); i++){
    		String term = parseTree.rule.get(i+1);
    		int childrenIndex = i;
    		Tree child = parseTree.children.get(childrenIndex);
    		if (!terminals.contains(term)){
    			if (term.equals("dcl")){
    				processDCL(child);
    			}
    			else if (term.equals("dcls")){
    				processDCLS(child);
    			}
    			else if (term.equals("statements")){
    				processStatements(child);
    			}
    			else if (term.equals("expr")){
    		    	if (parseTree.children.size() == 14){
    		    		String returnType = getExprType(parseTree.children.get(11));
    		    		if (!returnType.equals(intString))
    		    			bail("Return value is wrong type");
    		    	}
    			}
    			else{
    				processTree(child);
    			}
    		}
    	}
    }
    
    void generateStatements(Tree tree){
    	if (tree.children.size()==0){
    		return;
    	}
    	generateStatements(tree.children.get(0));
		generateStatement(tree.children.get(1));
    }
    
    void generateStatement(Tree tree){
    	if (tree.rule.get(1).equals("PRINTLN")){
    		generateExpr(tree.children.get(2));
    		add(1,3,0);
    		lis(2);
    		word("print");
    		jalr(2);
    	}
    	else if (tree.rule.get(1).equals("lvalue")){
    		if (tree.children.get(0).children.size()==2)
    		{
    			int location = generateLValueLocation(tree.children.get(0));
    			push (3);
        		generateExpr(tree.children.get(2));
        		pop(5);
        		sw(3,0,5);
    		}
    		else
    		{
    			int location = generateLValueLocation(tree.children.get(0));
        		generateExpr(tree.children.get(2));
    			sw(3,-4*location,29);
    		}
    	}else if (tree.rule.get(1).equals("WHILE")){
    		int currentLoop = loopNumber;
    		loopNumber++;
    		String loopLabel = String.format("loop%d",currentLoop);
        	System.out.println(loopLabel+":");
        	generateTest(tree.children.get(2));
        	String doneLabel = String.format("done%d",currentLoop);
        	beq(3,0,doneLabel);
        	generateStatements(tree.children.get(5));
        	beq(0,0,loopLabel);
        	
        	System.out.println(doneLabel+":");
    	}
    	else if (tree.rule.get(1).equals("IF")){
    		int currentIf = ifNumber;
    		ifNumber++;
    		String elseifLabel = String.format("else%d",currentIf);
    		String endIfLabel = String.format("endif%d",currentIf);
    		generateTest(tree.children.get(2));
    		beq(3,0,elseifLabel);
    		generateStatements(tree.children.get(5));
    		beq (0,0,endIfLabel);
    		System.out.println(elseifLabel+":");
       		generateStatements(tree.children.get(9));
       		System.out.println(endIfLabel+":");
    	}
    	else if (tree.rule.get(1).equals("DELETE")){
    		generateExpr(tree.children.get(3));
    		add(1,3,0);
    		lis(6);
    		word("delete");
    		jalr(6);
    	}
    }
    
    void processStatements(Tree tree){
    	if (tree.children.size()==0)
    		return;
    	else
    	{
    		processStatements(tree.children.get(0));
    		processStatement(tree.children.get(1));
    	}
    }
    
    void processStatement(Tree tree) {
    	if (tree.rule.get(1).equals("lvalue")){
    		String lValueType = getLValueType(tree.children.get(0));
    		String exprType = getExprType(tree.children.get(2));
    		
    		if (!lValueType.equals(exprType))
    			bail("Becomes is trying to equal non equivalent types together");
    		return;
    	}
    	else if (tree.rule.get(1).equals("IF")){
    		processTest (tree.children.get(2));
    		processTree (tree.children.get(5));
    		processTree(tree.children.get(9));
    	}
    	else if (tree.rule.get(1).equals("WHILE")){
    		processTest (tree.children.get(2));
    	}
    	else if (tree.rule.get(1).equals("PRINTLN")){
    		String exprType = getExprType(tree.children.get(2));
    		if (!exprType.equals(intString))
    			bail("Trying to print a value other than int");
    	}
    	else if (tree.rule.get(1).equals("DELETE")){
    		String exprType = getExprType(tree.children.get(3));
    		if (!exprType.equals(intStarString))
    			bail("Trying to delete a value other than int*");
    	}
    	
    	
	}
    int generateLValueLocation(Tree tree){
    	int location = 0;
    	if(tree.rule.get(1).equals("ID")){
    		String varName = processID(tree.children.get(0));
			location = symbolTable.get(varName).line;
    	}
    	else if (tree.rule.get(1).equals("STAR")){
    		generateFactor(tree.children.get(1));
    	}
    	else if(tree.rule.get(1).equals("LPAREN")){
    		location = generateLValueLocation(tree.children.get(1));
    	}
    	return location;
    	
    }
    
    
    void generateFactor(Tree tree){
    	if (tree.children.size()==1){
    		if(tree.rule.get(1).equals("ID")){
    			String varName = processID(tree.children.get(0));
    			int location = symbolTable.get(varName).line;
    			lw (3,location*-4,29);
    			return;
    		}
    		else if (tree.rule.get(1).equals("NUM")){
    			int numValue = Integer.parseInt(tree.children.get(0).rule.get(1));
    			lis(3);
    			word(numValue);
    		}
    		else if (tree.rule.get(1).equals("NULL")){
    			add(3,0,0);
    		}
    	}
    	else if (tree.children.size()==2){
    		if (tree.rule.get(1).equals("AMP")){
    			Tree child = tree.children.get(1);
    			if (child.children.size()==2)
    				generateFactor(child.children.get(1));
    			else{
    				int offset = generateLValueLocation(child);
    				lis(5);
    				word(offset*-4);
    				add(3,5,29);
    			}
    		}
    		else if (tree.rule.get(1).equals("STAR")){
    			generateFactor(tree.children.get(1));
    			lw(3,0,3);
    		}
    	}
    	else if (tree.children.size()==3){
    		generateExpr(tree.children.get(1));
    	}
    	else if (tree.children.size()==5){
    		generateExpr(tree.children.get(3));
    		add(1,3,0);
    		lis(6);
    		word("new");
    		jalr(6);
    	}
    }
    


	String getFactorType(Tree parseTree){
    	
    	if (parseTree.children.size()==1){
    		if(parseTree.rule.get(1).equals("ID")){
    			String type = getIDType(parseTree.children.get(0));
    			parseTree.type = type;
    			return type;
    		}
    		else if(parseTree.rule.get(1).equals("NUM")){
    			parseTree.type = intString;
    			return intString;
    		}
    		else if (parseTree.rule.get(1).equals("NULL")){
    			parseTree.type = intStarString;
    			return intStarString;
    			
    		}
    	}
    	else if (parseTree.children.size() == 2){
    		if (parseTree.rule.get(1).equals("AMP")){
    			parseTree.type = intStarString;
    			String type = getLValueType(parseTree.children.get(1));
    			if (type.equals(intString))
    				return intStarString;
    			else 
    				bail("Cannot AMP an non int value");
    		}
    		else if (parseTree.rule.get(1).equals("STAR")){
    			parseTree.type = intString;
    			String type = getFactorType(parseTree.children.get(1));
    			if (type.equals(intStarString))
    				return intString;
    			else 
    				bail("Cannot Star an non int* value");
    		}
    	}
    	else if (parseTree.children.size() == 3){
    		String type =  getExprType(parseTree.children.get(1));
    		parseTree.type = type;
    		return type;
    	}
    	else if (parseTree.children.size() == 5){
    		String type = getExprType(parseTree.children.get(3));
    		parseTree.type = intStarString;
    		if (type.equals(intString)){
    			return intStarString;
    		}
    		else bail("Cannot New an non int value");
    		
    	}
    	return null;
    }
	
	void generateTest(Tree tree){
		String expr1 = getExprType(tree.children.get(0));
		//String expr2 = getExprType(tree.children.get(2));
		generateExpr(tree.children.get(0));
		push(3);
		generateExpr(tree.children.get(2));
		pop(5);
		if (expr1.equals(intStarString)){
			// 5 is the first expression, 3 is the second expression.
			if(tree.rule.get(2).equals("LT")){
				// Less than
				sltu(3,5,3);
			}
			else if(tree.rule.get(2).equals("GT")){
				// Greater than
				sltu(3,3,5);
			}
			else if(tree.rule.get(2).equals("GE")){
				// Greater than or Equal to is the negation of less than
				sltu(3,5,3);
				sub (3,11,3);
			}
			else if(tree.rule.get(2).equals("LE")){
				// Less than or equal to is the negation of Greater than
				sltu(3,3,5);
				sub (3,11,3);
			}
			else if(tree.rule.get(2).equals("NE")){
				sltu(7,3,5);
				sltu(6,5,3);
				add(3,6,7);
			}
			else if(tree.rule.get(2).equals("EQ")){
				sltu(7,3,5);
				sltu(6,5,3);
				add(3,6,7);
				sub (3,11,3);
			}
		}else{
			// 5 is the first expression, 3 is the second expression.
			if(tree.rule.get(2).equals("LT")){
				// Less than
				slt(3,5,3);
			}
			else if(tree.rule.get(2).equals("GT")){
				// Greater than
				slt(3,3,5);
			}
			else if(tree.rule.get(2).equals("GE")){
				// Greater than or Equal to is the negation of less than
				slt(3,5,3);
				sub (3,11,3);
			}
			else if(tree.rule.get(2).equals("LE")){
				// Less than or equal to is the negation of Greater than
				slt(3,3,5);
				sub (3,11,3);
			}
			else if(tree.rule.get(2).equals("NE")){
				slt(7,3,5);
				slt(6,5,3);
				add(3,6,7);
			}
			else if(tree.rule.get(2).equals("EQ")){
				slt(7,3,5);
				slt(6,5,3);
				add(3,6,7);
				sub (3,11,3);
			}	
		}

	}
	
	void generateExpr(Tree tree){
		if (tree.children.size() == 1){
			generateTerm(tree.children.get(0));
			return;
		}
		String exprType = getExprType((tree.children.get(0)));
		String termType = getTermType((tree.children.get(2)));
		
		if (tree.rule.get(2).equals("PLUS")){

	
			generateExpr(tree.children.get(0));
			push(3);
			generateTerm(tree.children.get(2));
			pop(5);
			if(exprType.equals(intStarString)){
				//System.err.println("WTF?1");
				mult(3,4);
				mflo(3);
			}
			else if (termType.equals(intStarString)){
				//System.err.println("WTF?2");

				mult(5,4);
				mflo(5);
			}
			add(3,5,3);
			return;
		}
		else if (tree.rule.get(2).equals("MINUS"))	{
			generateExpr(tree.children.get(0));
			push(3);
			generateTerm(tree.children.get(2));
			pop(5);
			if(exprType.equals(intStarString) &&
					termType.equals(intString)){
				mult(3,4);
				mflo(3);
				//System.err.println("WTF?3");
			}
			sub(3,5,3);
			
			if (exprType.equals(intStarString) &&
					termType.equals(intStarString)){
				//System.err.println("WTF?4");

				div(3,4);
				mflo(3);
			}
			return;
		}
	}
    
	String getExprType(Tree tree) {
		if (tree.children.size() == 1){
			String type = getTermType(tree.children.get(0));
			tree.type = type;
			return type;
		}
		else if (tree.children.size()==3){
			String exprType = getExprType(tree.children.get(0));
			String termType = getTermType(tree.children.get(2));
			if (tree.rule.get(2).equals("PLUS")){
				if (exprType.equals(intString) && termType.equals(intString)){
					tree.type = intString;
					return intString;
				}
				else if(exprType.equals(intStarString) && termType.equals(intString)){
					tree.type = intStarString;
					return intStarString;
				}
				else if (exprType.equals(intString) && termType.equals(intStarString)){
					tree.type = intStarString;
					return intStarString;
				}
				else 
					bail("Trying to PLUS in Expression With inproper terms");
			}
			else if (tree.rule.get(2).equals("MINUS")){
				if (exprType.equals(intString) && termType.equals(intString)){
					tree.type = intString;
					return intString;
				}
				else if(exprType.equals(intStarString) && termType.equals(intString)){
					tree.type = intStarString;
					return intStarString;
				}
				else if (exprType.equals(intStarString) && termType.equals(intStarString)){
					tree.type = intString;
					return intString;
				}
				else 
					bail("Trying to MINUS in Expression With inproper terms");
			}
			
		}
    	return null;
	}
    
    void generateTerm(Tree tree) {
    	if (tree.children.size()==1){
    		generateFactor(tree.children.get(0));
    		return;
    	}
    	if(tree.children.size()==3){
			generateTerm(tree.children.get(0));
			push(3);
			generateFactor(tree.children.get(2));
			pop(5);
    		if (tree.rule.get(2).equals("STAR")){
    			mult(5,3);
    			mflo (3);
    		}
    		else if (tree.rule.get(2).equals("SLASH")){
    			div (5,3);
    			mflo (3);
    		}
    		else if (tree.rule.get(2).equals("PCT")){
    			div (5,3);
    			mfhi(3);
    		}
    		
    	}
	}

	
    String getTermType (Tree tree){
    	
    	if (tree.children.size()==1){
    		String type = getFactorType(tree.children.get(0));
    		tree.type = type;
    		return type;
    	}
    	else if (tree.children.size()==3){
    		String termType = getTermType (tree.children.get(0));
    		String factorType = getFactorType(tree.children.get(2));
			tree.type = intString;		
    		
    		if (termType.equals(intString) && factorType.equals(intString))
    			return intString;
    		else
    			bail ("Term Type is trying to process pointers");
    		
    	}
    	return null;
    }

	String getLValueType(Tree tree) {

		if (tree.children.size() == 1){
			String type = getIDType(tree.children.get(0));
			tree.type = type;
			return type;
		}
		else if (tree.children.size() == 2){
			String type = getFactorType(tree.children.get(1));
			tree.type = intString;
			if (type.equals(intStarString))
				return intString;
			else 
				bail("Cannot Star an non int* value inside LValue");
		}
		else if (tree.children.size()==3){
			String type = getLValueType(tree.children.get(1));
			tree.type = type;
    		return type;
		}
    	 return null;
	}

	String getIDType(Tree parseTree){
       	String variableName = parseTree.rule.get(1);
    	if (!symbolTable.containsKey(variableName)){
    		bail("Variable: "+ variableName + " has not been declared yet");
    	}
    	String type = symbolTable.get(variableName).type;
    	parseTree.type = type;
    	return type;
    }
    
 
    String processID(Tree parseTree){
    	String variableName = parseTree.rule.get(1);
    	if (!symbolTable.containsKey(variableName)){
    		bail("Variable: "+ variableName + " has not been declared yet");
    	}
    	return variableName;
    }
    
    void processDCLS (Tree parseTree){
    	if (parseTree.children.size()==0){
    		return;
    	}
    	
    	processDCLS(parseTree.children.get(0));
    	String type = processDCL(parseTree.children.get(1));
    	
    	String becomes = parseTree.rule.get(4);
    	if (becomes.equals("NULL")){
    		if(!type.equals(intStarString)){
    			bail("Pointer Declaration of Wrong Type");
    		}
    	}if (becomes.equals("NUM")){
    		if(!type.equals(intString)){
    			bail("Integer Declaration of Wrong Type");
    		}
    	}
    	
    }
    
    void processTest(Tree tree){
    	String expr1Type = getExprType(tree.children.get(0));
    	String expr2Type = getExprType(tree.children.get(2));
    	
    	if (!expr1Type.equals(expr2Type))
    		bail("Comparison Test has Different Expr Types");
    }
    
   
    
    String processDCL(Tree parseTree){
    	
    	Tree typeChild = parseTree.children.get(0);
    	Tree idChild = parseTree.children.get(1);
    	String type ="";
    	if(typeChild.children.size() == 1){
    		type = intString;
    	}else if (typeChild.children.size() == 2){
    		type = intStarString;
    	}
    	
    	if (currentVariableCount==2 && type.equals(intStarString)){
    		bail ("Second Parameter cannot be a pointer");
    	}
    	
    	String idName = idChild.rule.get(1);
    	if(symbolTable.containsKey(idName)){
    		bail("Variable: "+ idName + " has been declared more than once");

    	}
    	
    	Pair p = new Pair (currentVariableCount,type);
    	symbolTable.put(idName, p);
    	currentVariableCount++;
    	
    	return type;
    }
  
    public void go() {
        Tree parseTree = readParse("S");
        processTree(parseTree);
        generateTree(parseTree.children.get(1));
    }
}

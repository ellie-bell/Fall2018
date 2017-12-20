package cs445.a2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.lang.Math;

/**
 * This class uses two stacks to evaluate an infix arithmetic expression from an
 * InputStream. It should not create a full postfix expression along the way; it
 * should convert and evaluate in a pipelined fashion, in a single pass.
 */
public class InfixExpressionEvaluator {
    // Tokenizer to break up our input into tokens
     boolean openRoundBracket = false;
     boolean openAngleBracket = false;
     int numOpenRoundBrackets =0;
     int numOpenAngleBrackets= 0;
    int numClosedRoundBrackets =0;
    int numClosedAngleBrackets =0;


    StreamTokenizer tokenizer;

    // Stacks for operators (for converting to postfix) and operands (for
    // evaluating)
    StackInterface<Character> operatorStack;
    StackInterface<Double> operandStack;

   
    public InfixExpressionEvaluator(InputStream input) {
        // Initialize the tokenizer to read from the given InputStream
        tokenizer = new StreamTokenizer(new BufferedReader(
                        new InputStreamReader(input)));

        // StreamTokenizer likes to consider - and / to have special meaning.
        // Tell it that these are regular characters, so that they can be parsed
        // as operators
        tokenizer.ordinaryChar('-');
        tokenizer.ordinaryChar('/');

        // Allow the tokenizer to recognize end-of-line, which marks the end of
        // the expression
        tokenizer.eolIsSignificant(true);

        // Initialize the stacks
        operatorStack = new ArrayStack<Character>();
        operandStack = new ArrayStack<Double>();
    }

    public Double evaluate() throws ExpressionError {
        // Get the first token. If an IO exception occurs, replace it with a
        // runtime exception, causing an immediate crash.
        try {
            tokenizer.nextToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Continue processing tokens until we find end-of-line
        while (tokenizer.ttype != StreamTokenizer.TT_EOL) {
            // Consider possible token types
            switch (tokenizer.ttype) {
                case StreamTokenizer.TT_NUMBER:
                    // If the token is a number, process it as a double-valued
                    // operand
                    handleOperand((double)tokenizer.nval);
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    // If the token is any of the above characters, process it
                    // is an operator
                    handleOperator((char)tokenizer.ttype);
                    break;
                case '(':
                case '<':
                    // If the token is open bracket, process it as such. Forms
                    // of bracket are interchangeable but must nest properly.
                    handleOpenBracket((char)tokenizer.ttype);
                    break;
                case ')':
                case '>':
                    // If the token is close bracket, process it as such. Forms
                    // of bracket are interchangeable but must nest properly.
                    handleCloseBracket((char)tokenizer.ttype);
                    break;
                case StreamTokenizer.TT_WORD:
                    // If the token is a "word", throw an expression error
                    throw new ExpressionError("Unrecognized token: " +
                                    tokenizer.sval);
                default:
                    // If the token is any other type or value, throw an
                    // expression error
                    throw new ExpressionError("Unrecognized token: " +
                                    String.valueOf((char)tokenizer.ttype));
            }

            // Read the next token, again converting any potential IO exception
            try {
                tokenizer.nextToken();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Almost done now, but we may have to process remaining operators in
        // the operators stack
        handleRemainingOperators();

        // Return the result of the evaluation
        // TODO: Fix this return statement
        /*if (operatorStack.isEmpty())
        {
            if ( operandStack.isEmpty())
                {
                    throw new ExpressionError(" Invalid Input: Empty Brackets ");
                }
        }
        */
        return operandStack.pop();
    }

    
    void handleOperand(double operand) {
        
        operandStack.push(operand); 
        
    }

  
    void handleOperator(char operator) {

        if (openRoundBracket)
        {
            while ( !operatorStack.isEmpty() && !precedence(operator) && !openAngleBracket )
        {
            char nextOperation = operatorStack.pop();
            double firstOperand = operandStack.pop();
            double secondOperand = operandStack.pop();
            if ( nextOperation == '*')
            {
                operandStack.push(secondOperand * firstOperand);
            }
            if ( nextOperation == '/')
            {
                operandStack.push(secondOperand / firstOperand);
            }
            if ( nextOperation == '+')
            {
                operandStack.push(secondOperand + firstOperand);
            }
            if ( nextOperation == '-')
            {
                operandStack.push(secondOperand - firstOperand);
            }
            if ( nextOperation == '^')
                {
                    operandStack.push(Math.pow(secondOperand,firstOperand));
                }

        }
            openRoundBracket=false;
        }
        while ( !operatorStack.isEmpty() && !precedence(operator) && (!openRoundBracket) && !openAngleBracket )
        {
            char nextOperation = operatorStack.pop();
            double firstOperand = operandStack.pop();
            double secondOperand;
            try
            {
                secondOperand = operandStack.pop();
            }
             catch(Exception e)
            {
                throw new ExpressionError("Invalid Input: Too many Operators");
            }
            if ( nextOperation == '*')
            {
                operandStack.push(secondOperand * firstOperand);
            }
            if ( nextOperation == '/')
            {
                operandStack.push(secondOperand / firstOperand);
            }
            if ( nextOperation == '+')
            {
                operandStack.push(secondOperand + firstOperand);
            }
            if ( nextOperation == '-')
            {
                operandStack.push(secondOperand - firstOperand);
            }
            if ( nextOperation == '^')
                {
                    operandStack.push(Math.pow(secondOperand,firstOperand));
                }

        }
        
        operatorStack.push(operator);
         
    }
    




   /*  boolean precedence(char operator)
    {
    if(operator=='^')
    {
    return true;
    }

    if(operator == '*'|| operator == '/')
    {
            if(operator=='*'&& operatorStack.peek()=='/')
            {
                return false;
            }
            if(operator=='/'&&operatorStack.peek()=='*')
            {
                return false;
            }
    if(operatorStack.peek()=='/'|| operatorStack.peek()=='*' || operatorStack.peek()=='+'|| operatorStack.peek()=='-'||operatorStack.peek()=='('||operatorStack.peek()=='<')
    {
    return  true;
    }
    else
    {
    return false;
    }
    }
    if(operator == '+'|| operator == '-')
    {
    if(operatorStack.peek()=='/'|| operatorStack.peek()=='*'||operatorStack.peek()=='^')
    {
    return false;
    }
            if(operator=='+'&&operatorStack.peek()=='-')
            {
                return false;
            }
            if(operator=='-'&&operatorStack.peek()=='+')
            {
                return false;
            }
    else
    {
    return true;
    }
    }



   
    else
    {
    throw new ExpressionError("Not valid Operator");
    }




    }
*/
   boolean precedence( char operator)
    {
        if ( operator == '*' || operator == '/')
        {
            if ( operatorStack.peek() == '*' || operatorStack.peek() == '/' || operatorStack.peek() == '+' || operatorStack.peek() == '-' || operatorStack.peek() == '(' || operatorStack.peek() == '<')
            {
                return true;
            }
             if ( operator == '*' && operatorStack.peek() == '/')
            {
                return false;
            }
            if ( operator == '/' && operatorStack.peek() == '*')
            {
                return false;
            }
            else 
            {
                return false;
            }
        }
        else if ( operator == '+' || operator == '-')
        {
             if (operatorStack.peek() == '*' || operatorStack.peek() == '/' || operatorStack.peek() == '^')
            {
                return false;
            }
            if ( operator == '+' && operatorStack.peek() == '-')
            {
                return false;
            }
             if ( operator == '-' && operatorStack.peek() == '+')
            {
                return false;
            }
            else
            {
                return true;
            }
            
        }
        else if( operator == '^')
        {
            return true;
        }
        else if( operator == '(' || operator == '<')
        {
            return true;
        }

        
            throw new ExpressionError("Unidentified Operator");
       
    }
        
   
    void handleOpenBracket(char openBracket) {
        if( openBracket == '(')
        { 
            numOpenRoundBrackets ++;
            openRoundBracket = true;
            // round bracket = 0
        }
        if( openBracket == '<')
        {
            numOpenAngleBrackets++;
            openAngleBracket = true;
            //angle operand =0
        }
        operatorStack.push(openBracket);
    }

   
    void handleCloseBracket(char closeBracket) {
        if( closeBracket == ')')
        {
            numClosedRoundBrackets++;
        }
        else if( closeBracket == '>')
        {
            numClosedAngleBrackets++;
        }
        if((numClosedAngleBrackets > numOpenAngleBrackets)||(numClosedRoundBrackets > numOpenRoundBrackets))
                 {
                    throw new ExpressionError("Invalid Input: Extra Closed Bracket");
                 }

        if( numClosedAngleBrackets <= numOpenRoundBrackets || numClosedAngleBrackets <= numOpenAngleBrackets)
            if ( closeBracket == ')')
            {
                while(!(operatorStack.peek() == '('))
            {
                double firstOperandReamining = operandStack.pop();
                double secondOperandRemaining = operandStack.pop();
                char operator = operatorStack.pop();
                if ( operator == '*')
                {
                    operandStack.push(secondOperandRemaining * firstOperandReamining);
                }
                if ( operator == '/')
                {
                    operandStack.push(secondOperandRemaining / firstOperandReamining);
                }
                if ( operator == '+')
                {
                    operandStack.push(secondOperandRemaining + firstOperandReamining);
                }
                if ( operator == '-')
                {
                    operandStack.push(secondOperandRemaining - firstOperandReamining);
                }
                if ( operator == '^')
                {
                    operandStack.push(Math.pow(secondOperandRemaining,firstOperandReamining));
                }
             }
            operatorStack.pop();
            
            openRoundBracket = false;
            }
            else if( closeBracket == '>')
            {
                while(!(operatorStack.peek() == '<'))
            {
                double firstOperandReamining = operandStack.pop();
                double secondOperandRemaining = operandStack.pop();
                char operator = operatorStack.pop();
                if ( operator == '*')
                {
                    operandStack.push(secondOperandRemaining * firstOperandReamining);
                }
                if ( operator == '/')
                {
                    operandStack.push(secondOperandRemaining / firstOperandReamining);
                }
                if ( operator == '+')
                {
                    operandStack.push(secondOperandRemaining + firstOperandReamining);
                }
                if ( operator == '-')
                {
                    operandStack.push(secondOperandRemaining - firstOperandReamining);
                }
                if ( operator == '^')
                {
                    operandStack.push(Math.pow(secondOperandRemaining,firstOperandReamining));
                }

            }
        }}
        
        
  
    public static void main(String[] args) {
        System.out.println("Infix expression:");
        InfixExpressionEvaluator evaluator =
                        new InfixExpressionEvaluator(System.in);
        Double value = null;
        try {
            value = evaluator.evaluate();
        } catch (ExpressionError e) {
            System.out.println("ExpressionError: " + e.getMessage());
        }
        if (value != null) {
            System.out.println(value);
        } else {
            System.out.println("Evaluator returned null");
        }
    }
    void handleRemainingOperators() {
        // TODO: Complete this method
        if((numClosedAngleBrackets!=numOpenAngleBrackets)||(numClosedRoundBrackets!=numOpenRoundBrackets))
        {
            throw new ExpressionError("Invalid Input: Extra Open Bracket");
        }
        
        while(!operatorStack.isEmpty())
        {
            char operator;
            double first;
            double second;
            try{
                first = operandStack.pop();
                second = operandStack.pop();
                operator = operatorStack.pop();
            }
            catch(Exception e)
            {
                throw new ExpressionError("Invalid Input: Too Many Operators");
            }
            if( operator =='*')
            {
                operandStack.push(first*second);
            }
            else if(operator=='/')
            {
                if(first==0)
                {
                    throw new ExpressionError("Cannot divide by 0");
                }
                operandStack.push(second/first);
            }
            else if(operator=='+')
            {
                operandStack.push(first+second);
            }
            else if(operator == '-')
            {
                operandStack.push(second-first);
            }
            else if(operator=='^')
            {
                operandStack.push(Math.pow(second,first));
            }
        }
        
        if ( !operatorStack.isEmpty())
        {
            throw new ExpressionError(" invalid Input: Too Many Operators");
        }
        
        if (operatorStack.isEmpty())
        {
            if ( operandStack.isEmpty())
                {
                    throw new ExpressionError(" Invalid Input: Empty Brackets ");
                }
        }
    }
    }
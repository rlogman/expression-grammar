package calculator;

import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Text.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

import org.javafp.parsecj.Parser;
import org.javafp.parsecj.Reply;
import org.javafp.parsecj.input.Input;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalculatorGrammar {
  protected static final String DIVISION_BY_ZERO_IS_INVALID = "Division by 0 is invalid";
  protected static final String GENERAL_PARSING_EXCEPTION = "General parsing exception";

  // Variable map
  private static final Map<String, Integer> variables = new HashMap<>();

  // Forward declare expr to allow for circular references.
  private static final Parser.Ref<Character, Integer> expr = Parser.ref();

  // Basic tokens
  private static final Parser<Character, String> open = token("(");
  private static final Parser<Character, String> close = token(")");
  private static final Parser<Character, String> comma = token(",");

  // Binary Operations
  private static final Parser<Character, BinaryOperator<Integer>> add = token("add").label("add keyword")
      .then(retn((l, r) -> l + r));
  private static final Parser<Character, BinaryOperator<Integer>> sub = token("sub").label("sub keyword")
      .then(retn((l, r) -> l - r));
  private static final Parser<Character, BinaryOperator<Integer>> mult = token("mult").label("mult keyword")
      .then(retn((l, r) -> l * r));
  private static final Parser<Character, BinaryOperator<Integer>> div = token("div").label("div keyword")
      .then(retn((l, r) -> l / r));

  // Let operation
  private static final Parser<Character, String> let = token("let").label("let keyword");

  // Identifiers
  private static final Parser<Character, String> varName = token(regex("(?=(?!add|sub|mult|div|let))[a-zA-Z]+"))
      .label("variable name");

  // Var reference
  private static final Parser<Character, Integer> varRef = varName.bind(var -> {
    Integer value = variables.get(var);
    log.debug("Getting value of {}, which is {}", var, value);
    return retn(value);
  });

  // binOpr ::= 'add' | 'sub' | 'mult' | 'div'
  private static final Parser<Character, BinaryOperator<Integer>> binOpr = choice(add, sub, mult, div);

  // binExpr ::= binOpr '(' expr ',' expr ')'
  private static final Parser<Character, Integer> binExpr = binOpr.bind(
      operation -> open.then(expr).bind(l -> comma.then(expr).bind(r -> close.then(retn(operation.apply(l, r))))));

  // letExpr ::= 'let' '(' identifier ',' expr ',' expr ')'
  private static final Parser<Character, Integer> letExpr = let.then(open).then(varName)
      .bind(var -> comma.then(expr).bind(val -> {
        log.debug("Setting variable {} with {}", var, val);
        variables.put(var, val);
        return comma.then(expr).bind(exp -> close.then(retn(exp)));
      }));

  // expr ::= integer | varRef | binExpr | letExpr
  static {
    expr.set(choice(intr, varRef, binExpr, letExpr));
  }

  private static final Parser<Character, Integer> parser = wspaces.then(expr)
      .bind(result -> wspaces.then(eof()).then(retn(result)));

  public static Integer parse(String str) throws ParsingException {
    log.trace("About to parse '{}'", str);
    try {
      Reply<Character, Integer> parseResult = parser.parse(Input.of(str));
      if (parseResult.isError()) {
        String errorMessage = parseResult.getMsg();
        log.error("Parsing finished with the following error(s): {}", errorMessage);
        throw new ParsingException(errorMessage);
      }
      log.info("Parsing finished");
      return parseResult.getResult();
    } catch (Exception e) {
      throw new ParsingException(GENERAL_PARSING_EXCEPTION, e);
    }
  }

  private static final <T> Parser<Character, T> token(Parser<Character, T> p) {
    return p.bind(x -> wspaces.then(retn(x)));
  }

  private static Parser<Character, String> token(String name) {
    return token(string(name));
  }
}

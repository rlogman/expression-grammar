package calculator;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CLI implements ApplicationRunner {

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.trace("Executing calculator. Arguments received: {}", args.getNonOptionArgs());
    try {
      List<String> nonOptionArgs = args.getNonOptionArgs();
      if (nonOptionArgs.size() != 1) {
        showUsage();
        System.exit(-1);
      }
      System.out.println(CalculatorGrammar.parse(nonOptionArgs.get(0)));
    } catch (ParsingException e) {
      log.error("Parsing error", e);
      Throwable cause = e.getCause();
      System.err.printf("Parsing failed with this message: %s%n",
          (cause != null) ? cause.getMessage() : e.getMessage());
    }
  }

  private void showUsage() {
    System.err.println("Usage: java calculator.Application \"<expression-to-evaluate>\"");
  }

}

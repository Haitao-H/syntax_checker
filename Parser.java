/*
recursive descent parser for the following grammar
    assignment -> identifier = exp
    exp -> term exp_prime
    exp_prime -> + term exp_prime | - term exp_prime | e
    term -> fact term_prime
    term_prime -> * fact term_prime | e
    fact -> (exp) | - fact | + fact | literal | identifier
    literal -> 0 | NonZeroDigit Digit*
    identifier -> Letter [Letter | Digit]*
   
 */

public class Parser {

  static char input_token;
  static int index;
  static String str;


  static void next_token() {
    if (index >= str.length()) {
      input_token = '$';
    } else {
      input_token = str.charAt(index++);
    }
  }

  static boolean match(char expected_token) {
    if (input_token != expected_token) {
      return false;
    }
    next_token();
    return true;
  }

  public static boolean parser(String target) {
    str = target;
    index = 0;
    next_token();
    if (!assignment()) {
      return false;
    }
    return match('$');
  }

  // assignment -> identifier = exp
  static boolean assignment() {
    if (!identifier()) {
      return false;
    }
    if (!match('=')) {
      return false;
    }
    if (!exp()) {
      return false;
    }
    return true;
  }

  // exp -> term exp_prime
  static boolean exp() {
    if (!term()) {
      return false;
    }
    if (!exp_prime()) {
      return false;
    }
    return true;
  }

  // exp_prime -> + term exp_prime | - term exp_prime | e
  static boolean exp_prime() {
    switch (input_token) {
      case '+':
      case '-':
        next_token();
        if (!term()) {
          return false;
        }
        if (!exp_prime()) {
          return false;
        }
        return true;
      case '$':
      case ')':
        return true;
      default:
        return false;
    }
  }

  // term -> fact term_prime
  static boolean term() {
    if (!fact()) {
      return false;
    }
    if (!term_prime()) {
      return false;
    }
    return true;
  }

  // term_prime -> * fact term_prime | e
  static boolean term_prime() {
    switch (input_token) {
      case '*':
        next_token();
        if (!fact()) {
          return false;
        }
        if (!term_prime()) {
          return false;
        }
        return true;
      case '+':
      case '-':
      case ')':
      case '$':
        return true;
      default:
        return false;
    }
  }

  // fact -> (exp) | - fact | + fact | literal | identifier
  static boolean fact() {
    if (input_token == '(') {
      next_token();
      if (!exp()) {
        return false;
      }
      return match(')');
    } else if (input_token == '+' || input_token == '-') {
      next_token();
      if (!fact()) {
        return false;
      }
      return true;
    } else if (!(literal() || identifier())) {
      return false;
    } else {
      return true;
    }
  }

  // literal -> 0 | NonZeroDigit Digit*
  static boolean literal() {
    if (input_token == '0') {
      next_token();
      return !Character.isDigit(input_token);
    } else if (Character.isDigit(input_token)) {
      while (Character.isDigit(input_token)) {
        next_token();
      }
      return true;
    }
    return false;
  }

  // identifier -> Letter [Letter | Digit]*
  static boolean identifier() {
    if (Character.isLetter(input_token) || input_token == '_') {
      do {
        next_token();
      } while (Character.isLetter(input_token)
          || input_token == '_'
          || Character.isDigit(input_token));
      return true;
    } else {
      return false;
    }
  }

}

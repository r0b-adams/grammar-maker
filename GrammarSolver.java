// The Grammar Solver keeps track of a defined grammar in Backus-Naur format,
// maintaining each non-terminal of the grammar and their associated rules.
// From a given symbol, the Grammar Solver will generate a String of
// terminals as described by the syntax of the language.

import java.util.*;

public class GrammarSolver {
   private Map<String, String[]> grammarInfo;   // stores grammar's nonterminals and their respective rules
   private Random r;

   // Param: rules: list of nonterminals and their rules (grammar)
   //   Pre: rules != null; rules length > 0;
   //           else throws IllegalArgumentException
   //        non-terminals in grammar contains no duplicates;
   //           else throws IllegalArgumentException
   //  Post: Constructs a new GrammarSolver
   public GrammarSolver(List<String> rules) {
      if (rules == null || rules.size() == 0) {
         throw new IllegalArgumentException();
      }
      this.r = new Random();
      this.grammarInfo = new TreeMap<String, String[]>();

      for (String current : rules) {
         String[] nonTerminals = current.split("::=");
         if (this.grammarInfo.containsKey(nonTerminals[0])) {
            throw new IllegalArgumentException();
         }
         String[] ruleArray = nonTerminals[1].split("[|]");
         this.grammarInfo.put(nonTerminals[0], ruleArray);
      }
   }

   // Param: symbol: non-terminal symbol to check
   //   Pre: symbol != null; symbol length > 0;
   //          else throws IllegalArgumentException
   //  Post: returns true if given symbol matches one of the grammar's
   //        non-terminal symbols (nonterminals are case- and character-sensitive)
   //          otherwise, returns false
   public boolean contains(String symbol) {
      if (symbol == null || symbol.length() == 0) {
         throw new IllegalArgumentException();
      }
      return this.grammarInfo.containsKey(symbol);
   }

   //  Post: returns an ordered set of the non-terminals in the grammar
   public Set<String> getSymbols() {
      return this.grammarInfo.keySet();
   }

   // Param: symbol: non-terminal symbol
   //                when calling recursively, may be a terminal
   //   Pre: passed symbol != null; symbol length > 0
   //  Post: returns a random terminal of the given non-terminal;
   //        for higher-level non-terminals that are described in terms of
   //        other non-terminals (such as a sentence), will generate
   //        a String of random terminals defined by the rules of the grammar.
   public String generate(String symbol) {
      if (symbol == null || symbol.length() == 0) {
         throw new IllegalArgumentException();
      }
      String sentence = "";

      // if the grammar does not contain given symbol, assumes it is a terminal and return it
      if (!this.grammarInfo.containsKey(symbol)) {
         return symbol;
      } else {

         // chooses a random expansion rule of the given non-terminal symbol
         String[] symbolRules = this.grammarInfo.get(symbol);
         String randomRule = symbolRules[this.r.nextInt(symbolRules.length)];
         randomRule = randomRule.trim();

         // splits the chosen expansion rules
         String[] ruleChain = randomRule.split("[ \t]+");

         // examines each expansion rule, recursively generating a terminal for each
         for (String rule : ruleChain) {
            sentence += generate(rule) + " ";
         }
      }
      return sentence.trim();
   }
}
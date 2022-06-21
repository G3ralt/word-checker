package com.alexosenov.projects;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        try {
            WordChecker wordChecker = new WordChecker();
            wordChecker.init();
            Set<String> result = wordChecker.dictionary.parallelStream()
                    .filter(s -> s.length() == 9)
                    .filter(wordChecker::checkWord)
                    .collect(Collectors.toSet());
            result.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @State(Scope.Benchmark)
    public static class WordChecker {

        private Set<String> dictionary;

        public WordChecker() {
        }

        private boolean checkWord(String word) {

            if (word.length() == 1) {
                return true;
            }
            for (int i = 0; i < word.length(); i++) {
                String[] split = word.split("");
                split[i] = "";
                String withoutChar = String.join("", split);
                if (dictionary.contains(withoutChar)) {
                    return checkWord(withoutChar);
                }
            }
            return false;
        }





        private Set<String> fetchDictionary() throws IOException {
            URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");
            Set<String> dictionary;

            try (BufferedReader bfr = new BufferedReader(new InputStreamReader(wordsUrl.openStream()))) {
                dictionary = bfr.lines().skip(2).collect(Collectors.toSet());
            }
            dictionary.add("A");
            dictionary.add("I");

            return dictionary;
        }

        @Setup
        public void init() throws IOException {
            dictionary = fetchDictionary();
        }

        @Benchmark
        public Set<String> bench() {
            return dictionary.parallelStream()
                    .filter(s -> s.length() == 9)
                    .filter(this::checkWord)
                    .collect(Collectors.toSet());
        }
    }
}

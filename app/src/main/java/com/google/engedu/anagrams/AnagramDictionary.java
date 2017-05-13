/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {
            //Building the dictionary
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            //Building the hashmap: composed of a series of arrays list that
            //Generate the same string when sorted alphabetically. This string
            //Is used as the key. i.e. a hashmap of available anagrams
            if(lettersToWord.containsKey(sortLetters(word))){
                lettersToWord.get(sortLetters(word)).add(word);
            }else{
                lettersToWord.put(sortLetters(word), new ArrayList<String>());
                lettersToWord.get(sortLetters(word)).add(word);
            }

            //Building the hashmap sorting words by their length as the integer key
            if(sizeToWords.containsKey(word.length())){
                sizeToWords.get(word.length()).add(word);
            }else{
                sizeToWords.put(word.length(), new ArrayList<String>());
                sizeToWords.get(word.length()).add(word);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.matches(".*" + base + ".*");
    }

    public List<String> getAnagrams(String targetWord) {
        return lettersToWord.get(sortLetters(targetWord));
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String mod = "";

        for(int i = 1; i <= 26; ++i){
            mod = word + (char)(i+'A'-1);

            if(lettersToWord.containsKey(sortLetters(mod))){
                result.addAll(lettersToWord.get(sortLetters(mod)));
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        int index = random.nextInt(sizeToWords.get(wordLength).size());
        int anagrams = 0;

        while(anagrams < MIN_NUM_ANAGRAMS){
            anagrams = lettersToWord.get(sortLetters(sizeToWords.get(wordLength).get(index))).size();

            if(index > sizeToWords.get(wordLength).size()){
                index = 0;
            }

            ++index;
        }

        if(wordLength < MAX_WORD_LENGTH - 1)
            ++wordLength;

        return sizeToWords.get(wordLength).get(--index);
    }

    public String sortLetters(String word){
        String[] test = word.toLowerCase().split("");
        ArrayList<String> tester = new ArrayList<>();

        for(int i = 0; i < test.length; ++i){
            tester.add(test[i]);
        }

        Collections.sort(tester);

        StringBuilder builder = new StringBuilder();
        for(String s : tester) {
            builder.append(s);
        }

        return builder.toString();
    }
}
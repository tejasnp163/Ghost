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

package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random random = new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
        System.out.println("words"+words.size());
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    public int searchPossibleWords(String prefix) {
        int lowIndex = 0, midIndex = 0;
        int highIndex = words.size() - 1;
        int check = 0;
        boolean flag = false;
        while (lowIndex <= highIndex) {
            midIndex = (lowIndex + highIndex) / 2;
            String temp = words.get(midIndex);
            System.out.println(lowIndex + " " + midIndex + " " + highIndex + " " + temp);
            //if(prefix.length()<=temp.length()){
            check = temp.startsWith(prefix) ? 0 : prefix.compareTo(temp); // .length
           /* else {
                return -1;
            }*/
            System.out.println("Check : " + check);
            if (check > 0) {
                lowIndex = midIndex + 1;
            } else if (check < 0) {
                highIndex = midIndex - 1;
            } else {
                System.out.println("flagin");
                flag = true;
                break;
            }

        }
        if (flag) {

            return midIndex;
        } else {
            return -1;
        }
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        System.out.println("Word" + prefix);
        //Collections.sort(words);
        int index;
        if(prefix.equals(""))
        {
            System.out.println("<-------------------Found-------->");
            index=random.nextInt(words.size());
            System.out.println("Index : " + words.get(index));
            return words.get(index);
        }
        else
        {
            index = searchPossibleWords(prefix);
            if(index!=-1) {
                return words.get(index);
            }
            else
            {
                String str="";
                return str;
            }
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix,int whoseturn)
    {
        System.out.println("pre" + prefix);
        String selected = null;
        ArrayList<String> temp_words = new ArrayList<String>();
        int index, upindex, downindex, temp;
        if (prefix.equals(""))
        {
            index = random.nextInt(words.size());
            String temp_string = words.get(index);
            while (temp_string.length() % 2 != 0)
            {
                index = random.nextInt(words.size());
                temp_string = words.get(index);
            }
            return words.get(index);
        }
        else
        {
            index = searchPossibleWords(prefix);
            if (index != -1)
            {
                System.out.println("Words : " + words.get(index));
                selected = words.get(index);
                temp_words.add(selected);
                upindex = downindex = index;
                while (true)
                {
                    upindex++;
                    if (upindex == words.size())
                    {
                        break;
                    }
                    selected = words.get(upindex);
                    temp = selected.startsWith(prefix) ? 0 : prefix.compareTo(selected);
                    if (temp != 0)
                    {
                        break;
                    }
                    else
                    {
                        temp_words.add(selected);
                    }
                }
                while (true)
                {
                    downindex--;
                    if (downindex == -1)
                    {
                        break;
                    }
                    selected = words.get(downindex);
                    temp = selected.startsWith(prefix) ? 0 : prefix.compareTo(selected);
                    if (temp != 0)
                    {
                        break;
                    }
                    else
                    {
                        temp_words.add(selected);
                    }
                }
                if (!temp_words.isEmpty())
                {
                    if (whoseturn == 0)
                    {
                        index = random.nextInt(temp_words.size());
                        String temp_string = temp_words.get(index);
                        while (temp_string.length() % 2 == 0)
                        {
                            index = random.nextInt(words.size());
                            temp_string = words.get(index);
                        }
                        System.out.println("Words User: " + temp_string);
                        return temp_string;
                    }
                    else
                    {
                        index = random.nextInt(temp_words.size());
                        String temp_string = temp_words.get(index);
                        while (temp_string.length() % 2 != 0)
                        {
                            index = random.nextInt(words.size());
                            temp_string = words.get(index);
                        }
                        System.out.println("Words Computer: " + temp_string);
                        return temp_string;
                    }
                }
                else
                {
                    return selected;
                }
            }
            else
            {
                System.out.println("null");
                return "";
            }
        }
    }
}

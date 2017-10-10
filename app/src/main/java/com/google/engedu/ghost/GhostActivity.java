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

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private int whoseturn = 0;
    private boolean userTurn = false;
    private Random random = new Random();
    private String wordFragment="";
    private SimpleDictionary simpleDictionary;
    TextView gameStatus;
    TextView word;
    Button rst;
    Button chl;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        final AssetManager assetManager = getAssets();
        gameStatus = (TextView)findViewById(R.id.gameStatus);
        word = (TextView)findViewById(R.id.ghostText);
        rst = (Button)findViewById(R.id.restart);
        chl = (Button)findViewById(R.id.challenge);

        handler = new Handler();
        try {
            simpleDictionary= new SimpleDictionary(assetManager.open("words.txt"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
        rst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }


        });

        chl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wordFragment.length()<4){
                    System.out.println("Toast!!");
                    Toast.makeText(getApplication(),"Not Yet!!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                gameStatus.setText(COMPUTER_TURN);
                if(wordFragment.length()>=4 && simpleDictionary.isWord(wordFragment))
                {
                    String temp=simpleDictionary.getGoodWordStartingWith(wordFragment,whoseturn);
                    if(temp.startsWith(wordFragment)==true && wordFragment.equals(temp))
                    Toast.makeText(getApplication(),"You Win!!!",Toast.LENGTH_SHORT).show();
                    //gameStatus.setText("You Win!!!");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resetGame();
                        }
                    },1000);

                    //rst.callOnClick();
                }
                else
                {
                    String temp = simpleDictionary.getAnyWordStartingWith(wordFragment);
                    if(temp.equals("")){
                        Toast.makeText(getApplication(),"You Win!!!",Toast.LENGTH_SHORT).show();
                        //gameStatus.setText("You Win!!!");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resetGame();
                            }
                        },1000);

                    }
                    else
                    {
                        Toast.makeText(getApplication(),"Computer Win!!!",Toast.LENGTH_SHORT).show();
                       // gameStatus.setText("Computer Win!!!");
                        word.setText(temp);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resetGame();
                            }
                        },1000);
                    }
                }
            }
        });
    }

    private void resetGame() {
        wordFragment="";
        onStart(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        whoseturn=userTurn?1:0;
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            //System.out.println("cfkjdkvkjenfk");
            label.setText(COMPUTER_TURN);
            System.out.println("ct");
            computerTurn();
        }
        return true;
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */


    private void computerTurn() {
        //System.out.println("In");

        final TextView label = (TextView) findViewById(R.id.gameStatus);

        //gameStatus.setText(COMPUTER_TURN);
        //System.out.println("wf"+wordFragment);
        // Do computer turn stuff then make it the user's turn again
        if(wordFragment.length() >= 4)
        {
            String temp=simpleDictionary.getGoodWordStartingWith(wordFragment,whoseturn);
            if(temp.startsWith(wordFragment)==true && wordFragment.equals(temp))
            {
                Toast.makeText(getApplication(),"Computer Win!!!",Toast.LENGTH_SHORT).show();
                //gameStatus.setText("Computer Wins!!");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetGame();
                    }
                },1000);
                //rst.callOnClick();
            }
        }

        String temp_word=simpleDictionary.getGoodWordStartingWith(wordFragment,whoseturn);
        System.out.println(temp_word);
        if(temp_word.equals(""))
        {
            Toast.makeText(getApplication(),"Computer Win!!!",Toast.LENGTH_SHORT).show();
           // gameStatus.setText("Computer Win!!!");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetGame();
                }
            },1000);

        }
        else
        {
            System.out.println("temp_word"+temp_word);
            //gameStatus.setText(temp_word);
            wordFragment+=temp_word.substring(wordFragment.length(),wordFragment.length()+1);
            word.setText(wordFragment);
            userTurn = true;
            gameStatus.setText(USER_TURN);


        }
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char temp = (char)event.getUnicodeChar();
        temp= Character.toLowerCase(temp);
        if(temp>='a' && temp<='z')
        {
            wordFragment+=temp;
            word.setText(wordFragment);
            if(simpleDictionary.isWord(wordFragment))
            {
                gameStatus.setText("Word Found!");
            }
            System.out.println("Word Fragment : " + wordFragment);
            gameStatus.setText(COMPUTER_TURN);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            },1000);
            return true;
        }
        else
        {
            return super.onKeyUp(keyCode, event);
        }
    }
}

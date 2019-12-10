 package com.example.dailyquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

 public class MainActivity extends AppCompatActivity {
    public TextView quoteTextView;
    public ImageView nextButtonImageview;
    public ImageView previousButtonImageview;
    public ImageView shareButtonImageview;
    public ImageView faveButtonImageview;
    public ArrayList<Quotes> quotesList;
    public Stack<Quotes> previousQuotes;
    public int index;
    public boolean isPrevious = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteTextView = (TextView) findViewById(R.id.quotes_textview);
        nextButtonImageview = (ImageView) findViewById(R.id.next_button);
        previousButtonImageview = (ImageView) findViewById(R.id.previous_button);
        shareButtonImageview = (ImageView) findViewById(R.id.share_button);
        faveButtonImageview = (ImageView) findViewById(R.id.fave_button_off);


        //importing qoutes from strings.xml
        Resources res = getResources();
        String[] allQuotes = res.getStringArray(R.array.quotes);
        String[] allAuthors = res.getStringArray(R.array.authors);
        quotesList = new ArrayList<>();
        addToQuoteList(allQuotes, allAuthors);
        previousQuotes = new Stack<>();



        //Generate a random quote from the quotes
        final int quoteLength = quotesList.size();
        index = getRandomQuote(quoteLength - 1);
        quoteTextView.setText(quotesList.get(index).toString());

        //genereate a random quote when next button is pressed
         nextButtonImageview.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 isPrevious = false;
                 index = getRandomQuote(quoteLength - 1);
                 quoteTextView.setText(quotesList.get(index).toString());
                 previousQuotes.push(quotesList.get(index));
             }
         });
        //recall previous quote when back button is pressed
        previousButtonImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPrevious && previousQuotes.size() > 0){
                    previousQuotes.pop();
                    isPrevious = true;
                }
                if(previousQuotes.size() > 0 && isPrevious){
                    quoteTextView.setText(previousQuotes.pop().toString());
                }else{
                    Toast.makeText(MainActivity.this,"Get A new Quote", Toast.LENGTH_SHORT).show();
                   // System.exit(0);
                }
//
//                index = getRandomQuote(quoteLength - 1);
//                quoteTextView.setText(quotesList.get(index).toString());
            }
        });
        //share quote on social media
        shareButtonImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent =  new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, quotesList.get(index).toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        //favourite quotes functions
         faveButtonImageview.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                if(quotesList.get(index).isFavourite()){
                    faveButtonImageview.setImageResource(R.mipmap.ic_heart_icon_off);
                    quotesList.get(index).setFavourite(false);
                }
                else{
                    faveButtonImageview.setImageResource(R.mipmap.ic_heart_fave_on);
                    quotesList.get(index).setFavourite(true);
                }
             }
         });


    }
    //adding all quotes to quote list array.
    public void  addToQuoteList(String[] allQuotes, String[] allAuthors){
        for (int i = 0; i < allQuotes.length-1; i++){
            String quote = allQuotes[i];
            String author = allAuthors[i];
            Quotes  newquote = new Quotes(quote, author);
            quotesList.add(newquote);
        }
    }
    // Generate random number for all quotes between 0 - length-1
    public int getRandomQuote( int length){
        return(int) (Math.random() * length) + 1;
    }
}

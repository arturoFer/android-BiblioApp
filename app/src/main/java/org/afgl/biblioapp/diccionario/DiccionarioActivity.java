package org.afgl.biblioapp.diccionario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.afgl.biblioapp.R;

public class DiccionarioActivity extends AppCompatActivity {

    public static final String WORD_EXTRA = "WORD_EXTRA";
    private String base_url;
    private int numWords;
    private String word;
    private TextView myTextView;
    private RadioGroup myRadioGroup;
    private Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diccionario);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CharSequence text = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            word = text.toString();
        } else{
            word = getIntent().getStringExtra(WORD_EXTRA);
        }

        myRadioGroup = findViewById(R.id.myRadioGroup);
        myButton = findViewById(R.id.myButton);
        myTextView = findViewById(R.id.mytextview);

        numWords = checkString(word);

        if(numWords == 1){
            writeTextViewSuccess();
            getSettings();
            setListeners();
        } else{
            writeTextViewError();
            hideControls();
        }

    }

    private void setListeners(){
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDiccionario();
            }
        });
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i){
                    case R.id.oxford:
                        base_url = "https://es.oxforddictionaries.com/definicion/";
                        writePreferences("0");
                        break;
                    case R.id.thefreediccionario:
                        base_url = "http://es.thefreedictionary.com/";
                        writePreferences("1");
                        break;
                    case R.id.wikcionario:
                        base_url = "https://es.wiktionary.org/wiki/";
                        writePreferences("2");
                        break;
                    case R.id.wordreference:
                        base_url = "http://www.wordreference.com/definicion/";
                        writePreferences("3");
                        break;
                }
            }
        });
    }

    private int checkString(String word) {
        String words[] = word.split(" ");
        return words.length;
    }

    private void getSettings(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String diccionario = sharedPreferences.getString(getResources().getString(R.string.diccionario_key), "0");
        switch(diccionario){
            case "0":
                base_url = "https://es.oxforddictionaries.com/definicion/";
                myRadioGroup.check(R.id.oxford);
                break;
            case "1":
                base_url = "http://es.thefreedictionary.com/";
                myRadioGroup.check(R.id.thefreediccionario);
                break;
            case "2":
                base_url = "https://es.wiktionary.org/wiki/";
                myRadioGroup.check(R.id.wikcionario);
                break;
            case "3":
                base_url = "http://www.wordreference.com/definicion/";
                myRadioGroup.check(R.id.wordreference);
                break;
        }
    }

    private void writeTextViewSuccess(){
        String texto;
        texto = String.format(getResources().getString(R.string.texto_diccionario), word);
        Spannable text = new SpannableString(texto);
        int start = texto.indexOf(word);
        int end = texto.length();
        text.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        myTextView.setText(text);
    }

    private void writeTextViewError(){
        String texto = String.format(getResources().getString(R.string.error_diccionario), numWords, word);
        Spannable text = new SpannableString(texto);
        text.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new UnderlineSpan(), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int start = texto.indexOf("una sola palabra,");
        int end = start + String.valueOf("una sola palabra,").length();
        text.setSpan(new StyleSpan(Typeface.BOLD), start,  end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        String auxiliar =Integer.toString(numWords) + " palabras.";
        start = texto.indexOf(auxiliar);
        end = start + String.valueOf(auxiliar).length();
        text.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = texto.indexOf("Su selección ha sido:");
        end = start + String.valueOf("Su selección ha sido:").length();
        text.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end +1;
        end = texto.length();
        text.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(0.9f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        myTextView.setText(text);
    }

    private void hideControls(){
        TextView selectedDiccionario = findViewById(R.id.select_diccionario);
        selectedDiccionario.setVisibility(View.GONE);
        myRadioGroup.setVisibility(View.GONE);
        myButton.setVisibility(View.GONE);
    }

    private void writePreferences(String s){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.diccionario_key), s);
        editor.apply();
    }

    private void launchDiccionario(){
        String url = base_url + word;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

}
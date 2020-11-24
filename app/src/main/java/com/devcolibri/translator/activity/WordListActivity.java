package com.devcolibri.translator.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.devcolibri.translator.R;
import com.devcolibri.translator.VocabularyApp;
import com.devcolibri.translator.adapter.WordAdapter;
import com.devcolibri.translator.db.AppDatabase;
import com.devcolibri.translator.entity.Word;

public class WordListActivity extends AppCompatActivity {
    private static final int ADD_WORD_CODE = 1001;

    private RecyclerView recyclerView;
    private FloatingActionButton addWordButton;
    private WordAdapter wordAdapter;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        database = VocabularyApp.getDatabase();

        loadWords();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadWords() {
        new AsyncTask<Void, Void, List<Word>>() {
            @Override
            protected List<Word> doInBackground(Void... voids) {
                return database.getPersonDao().getAllWords();
            }

            @Override
            protected void onPostExecute(List<Word> words) {
                wordAdapter.setWords(words);
            }
        }.execute();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.words_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wordAdapter = new WordAdapter();
        recyclerView.setAdapter(wordAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        addWordButton = findViewById(R.id.add_word_button);
        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordListActivity.this, AddWordActivity.class);
                startActivityForResult(intent, ADD_WORD_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_WORD_CODE && resultCode == RESULT_OK) {
            loadWords();
        }
    }
}

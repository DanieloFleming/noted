package com.example.fleming.crud;

import android.content.Intent;
import android.os.Bundle;

public class CreateNoteActivity extends NoteEditor {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if(Intent.ACTION_SEND.equals(action)){
            handleIntentAction(intent);
        }
    }

    protected void handleIntentAction(Intent intent) {
        String title = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        String content = intent.getStringExtra(Intent.EXTRA_TEXT);

        content = content.replaceFirst(title, "");


        textFieldTitle.setText(title);
        textFieldContent.setText(content);
    }
}

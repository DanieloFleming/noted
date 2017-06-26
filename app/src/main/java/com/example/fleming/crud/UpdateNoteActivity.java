package com.example.fleming.crud;

import android.content.Intent;
import android.os.Bundle;

public class UpdateNoteActivity extends NoteEditor {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        textFieldId.setText(intent.getStringExtra("id"));
        textFieldTitle.setText(intent.getStringExtra("title"));
        textFieldContent.setText(intent.getStringExtra("content"));
    }

}

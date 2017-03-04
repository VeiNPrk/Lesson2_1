package com.example.vnprk.l1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by VNPrk on 28.02.2017.
 */
public class NoteActivity extends AppCompatActivity {
    public static final String KEY_TITTLE = "key_tittle";
	public static final String KEY_NOTE = "key_note";
    public static final String KEY_TEXT = "key_text";
    public static final String KEY_CODE = "key_code";
	MenuItem editItem;
    EditText etTittle;
    EditText etText;
    Button btnSave;
	Button btnCancel;
	Button btnOk;
	String tittle = "";
	String text = "";
	NoteClass note = null;
	int requestCode = 0;

    public static void openActivity(Context context, NoteClass _note, int _reqCode){
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(KEY_NOTE, _note);
		intent.putExtra(KEY_CODE, _reqCode);
        if (!(context instanceof Activity))
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
		if(getIntent().hasExtra(KEY_NOTE))
			note = (NoteClass)getIntent().getSerializableExtra(KEY_NOTE);
		/*if(getIntent().hasExtra(KEY_TITTLE))
			tittle = getIntent().getStringExtra(KEY_TITTLE);
		if(getIntent().hasExtra(KEY_TEXT))
			text = getIntent().getStringExtra(KEY_TEXT);*/
		requestCode = getIntent().getIntExtra(KEY_CODE, 0);
        initViews();
        initData();
        setClickListener();
    }

    private void initViews(){
        etTittle = (EditText)findViewById(R.id.et_tittle_note);
        etText = (EditText)findViewById(R.id.et_text_note);
        btnSave = (Button)findViewById(R.id.btn_save_note);
        btnCancel = (Button)findViewById(R.id.btn_cancel_note);
        btnOk = (Button)findViewById(R.id.btn_ok_note);
		if(requestCode==2){
			btnSave.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnOk.setVisibility(View.VISIBLE);
			etTittle.setEnabled(false);
			etText.setEnabled(false);
		}
        else{
            btnSave.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.GONE);
            etTittle.setEnabled(true);
            etText.setEnabled(true);
        }
    }
	
	private void initData(){
        if(note!=null){
		    etTittle.setText(note.getTittle());
		    etText.setText(note.getText());
	    }
	}
	
	private void setClickListener(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNote();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okNote();
            }
        });
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
		MenuItem deleteItem = menu.findItem(R.id.menu_delete);
		editItem = menu.findItem(R.id.menu_edit);
		deleteItem.setVisible(false);
		if(requestCode!=2)
			editItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // обработка нажатий
        switch (item.getItemId()) {
            case R.id.menu_edit:
                toEditNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	private void saveNote(){
		Intent intent = new Intent();
		requestCode=3;
		/*intent.putExtra(KEY_TITTLE, etTittle.getText().toString());
		intent.putExtra(KEY_TEXT, etText.getText().toString());
		intent.putExtra(KEY_CODE, requestCode);*/
		if(note!=null){
			note.setTittle(etTittle.getText().toString());
			note.setText(etText.getText().toString());
		}
		else{
			note = new NoteClass(etTittle.getText().toString(), etText.getText().toString());
		}
		note.save();
        setResult(RESULT_OK, intent);
        finish();
	}
	
	private void cancelNote(){
		Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
	}
	
	private void okNote(){
		Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
	}
	//переход из режима просмотра в режим редактирования
	private void toEditNote(){
		editItem.setVisible(false);
		requestCode=3;
		btnSave.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.GONE);
		etTittle.setEnabled(true);
		etText.setEnabled(true);
	}
}

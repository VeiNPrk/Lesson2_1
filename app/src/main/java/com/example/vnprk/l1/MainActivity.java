package com.example.vnprk.l1;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> elements;
	List<Integer> selectedPositions;
    List<NoteClass> notes;
    ArrayAdapter<String> adapter;
    ListView listView;
	MenuItem editItem;
    MenuItem deleteItem;
	int requestCodeAdd = 1;
    int requestCodeView = 2;
	int requestCodeEdit = 3;
	int nowPositionList = -1;
	int countChecked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Создаем массив элементов для списка
        notes = new ArrayList<NoteClass>();
        elements = new ArrayList<String>();
		selectedPositions = new ArrayList<Integer>();
		initViews();
		initData();
		setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // обработка нажатий
        switch (item.getItemId()) {
            case R.id.menu_add:
                addElementView();
                return true;
            case R.id.menu_clear:
                clearList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	private void initViews(){
		listView = (ListView) findViewById(R.id.list);	
	}
	
	private void initData(){
		notes = new Select().from(NoteClass.class).queryList();
		setTittleList();
        /*for(int i = 0; i < 5; i++) {
            notes.add(new NoteClass("NOTE " + i, "Text Note " + i));
        }
        setTittleList();*/
	}
	private void setListeners(){
		adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_activated_1, elements);
        listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				nowPositionList=position;
                elementView(notes.get(position));
			}
		});
		
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                //обработчик выделения пунктов списка ActionMode
				/*if(checked){
					if(countChecked<elements.size())
						countChecked++;
					else
						countChecked=elements.size();
				}
				else{
					if(countChecked>=1)
						countChecked--;
					else
						countChecked=0;
                }*/
				refreshSelectedPositions();//nowPositionList = getLastCheckedNote();
				if(selectedPositions.size()>1) {
                    editItem.setVisible(false);
                    //deleteItem.setEnabled(false);
                }
                else {
                    editItem.setVisible(true);
                    //deleteItem.setEnabled(true);
                }
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // обработка нажатия на пункт ActionMode
                // в данном случае просто закрываем меню
                switch (item.getItemId()) {
                    case R.id.menu_edit:
                        editElementView(notes.get(selectedPositions.get(0)));
                        mode.finish();
                        return true;
                    case R.id.menu_delete:
                        deleteElement(selectedPositions);
                        mode.finish();
                        return true;
                    default:
                        mode.finish();
                        return true;
                }
                //return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Устанавливаем для ActionMode меню
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
				editItem = menu.findItem(R.id.menu_edit);
                deleteItem = menu.findItem(R.id.menu_delete);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // вызывается при закрытии ActionMode
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // вызывается при обновлении ActionMode
                // true если меню или ActionMode обновлено иначе false
                return false;
            }
        });
	}

    private void clearList() {
		notes.clear();
        elements.clear();
        adapter.notifyDataSetChanged();
    }
/*
    private void addElement(Intent data) {
		String new_tittle="";
		String new_text="";
		if(data!=null)
		{
			new_tittle = data.getStringExtra(NoteActivity.KEY_TITTLE);
			new_text = data.getStringExtra(NoteActivity.KEY_TEXT);
		}
		NoteClass note = new NoteClass(new_tittle, new_text);
		notes.add(note);
        //elements.add(new_tittle);
        setTittleList();
        adapter.notifyDataSetChanged();
    }

    private void editElement(Intent data, int position) {
		String new_tittle="";
		String new_text="";
		if(data!=null)
		{
			new_tittle = data.getStringExtra(NoteActivity.KEY_TITTLE);
			new_text = data.getStringExtra(NoteActivity.KEY_TEXT);
			
		}
		NoteClass note = new NoteClass(new_tittle, new_text);
		if(position>-1)
			notes.set(position, note);
        setTittleList();
		adapter.notifyDataSetChanged();
		nowPositionList=-1;
        countChecked=0;
    }
*/
    private void deleteElement(List<Integer> positions) {
		if(positions.size()>0)
		{
			for(int i:positions)
				notes.get(i).delete();
		refreshData();
		}
		/*notes.remove(position);
        setTittleList();
		adapter.notifyDataSetChanged();
		nowPositionList=-1;
        countChecked=0;*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	/*
        if(requestCode==requestCodeAdd && resultCode==RESULT_OK && data!=null)
			//refreshData();
            //addElement(data);
        if(requestCode==requestCodeEdit && resultCode==RESULT_OK && data!=null) {
			//refreshData();
			//editElement(data, nowPositionList);
        }
		if(requestCode==requestCodeView && resultCode==RESULT_OK && data!=null)
        {
			if(data.getIntExtra(NoteActivity.KEY_CODE, 0)==3)
				editElement(data, nowPositionList);
			else
				nowPositionList=-1;
				//refreshData();
		}*/
		if(resultCode==RESULT_OK)
			refreshData();
        //countChecked=0;
    }

	private void addElementView(){
		Intent intent = new Intent(MainActivity.this, NoteActivity.class);
		intent.putExtra(NoteActivity.KEY_CODE, requestCodeAdd);
        startActivityForResult(intent, requestCodeAdd);
	}
	
	private void editElementView(NoteClass _note){
		Intent intent = new Intent(MainActivity.this, NoteActivity.class);
		/*intent.putExtra(NoteActivity.KEY_TITTLE, note.getTittle());
		intent.putExtra(NoteActivity.KEY_TEXT, note.getText());*/
		intent.putExtra(NoteActivity.KEY_NOTE, _note);
		intent.putExtra(NoteActivity.KEY_CODE, requestCodeEdit);
        startActivityForResult(intent, requestCodeEdit);
	}
	
	private void elementView(NoteClass _note){
		Intent intent = new Intent(MainActivity.this, NoteActivity.class);
		/*intent.putExtra(NoteActivity.KEY_TITTLE, note.getTittle());
		intent.putExtra(NoteActivity.KEY_TEXT, note.getText());*/
		intent.putExtra(NoteActivity.KEY_NOTE, _note);
		intent.putExtra(NoteActivity.KEY_CODE, requestCodeView);
        startActivityForResult(intent, requestCodeView);
	}
	
    private void setTittleList(){
		elements.clear();
        for(NoteClass note : notes){
            elements.add(note.getTittle());
        }
    }
	
	/*private int getLastCheckedNote(){
		int lastPosition = -1;
		int cntChoice = listView.getCount();
		SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
		for (int i = 0; i < cntChoice; i++) {

			if (sparseBooleanArray.get(i) == true) {
				lastPosition=i;
			} 
		}
		return lastPosition;
	}*/
	
	private void refreshSelectedPositions(){
		selectedPositions.clear();
		int cntChoice = listView.getCount();
		SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
		for (int i = 0; i < cntChoice; i++) {

			if (sparseBooleanArray.get(i) == true) {
				selectedPositions.add(i);
			} 
		}
	}
	
	private void refreshData(){
		notes.clear();
		notes = new Select().from(NoteClass.class).queryList();
		setTittleList();
		adapter.notifyDataSetChanged();
		nowPositionList=-1;
        countChecked=0;
	}
}

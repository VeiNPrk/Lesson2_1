package com.example.vnprk.l1;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by VNPrk on 28.02.2017.
 */
@Table(database = AppDataBase.class)
public class NoteClass extends BaseModel implements Serializable {

	@PrimaryKey(autoincrement = true)
    public long id;
    @Column
    private String tittle;
    @Column
	private String text;

	public NoteClass(){
	}
	
    public NoteClass(String _tittle, String _text){
        tittle=_tittle;
        text=_text;
    }

    public String getTittle(){
        return tittle;
    }

    public String getText(){
        return text;
    }
	
	public void setTittle(String _tittle){
        tittle=_tittle;
    }

    public void setText(String _text){
        text=_text;
    }
}

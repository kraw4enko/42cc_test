package com.krawa.test42cc;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends Activity{

	private DB db;
	private Cursor cursor;
	private ListView lvData;
	private ArrayList<String> itemArray;
	private ArrayAdapter<String> itemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    // открываем подключение к БД
	    db = new DB(this);
	    db.open();
	    // получаем курсор
	    cursor = db.getAllData();
	    
	    if(cursor.getCount() == 0){
	    	fillData();
	    	cursor = db.getAllData();
	    }
	    startManagingCursor(cursor);

	    lvData = (ListView) findViewById(R.id.listView1);
	    
	    cursor.moveToFirst();
        
	    itemArray = new ArrayList<String>();
	    Bitmap bitmap = null;
	    for(int i =0; i<cursor.getColumnCount();i++){
	    	if(!cursor.getColumnName(i).equals(DB.COLUMN_FOTO)){
	    		itemArray.add(cursor.getString(i));
	    	} else{
	    		bitmap = byteToImage(cursor.getBlob(i));
	    	}
	    	
	    }
	    
        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemArray);

        
        db.close();
        
        ImageView ivFoto = new ImageView(this);
        if(bitmap != null){
        	ivFoto.setImageBitmap(bitmap);
        }
        
        lvData.addFooterView(ivFoto);
        
        lvData.setAdapter(itemAdapter);
	
	}



	private void fillData() {
		String name = getString(R.string.name);
		String surname = getString(R.string.surname);
		String birth = getString(R.string.birth);
		String bio = getString(R.string.bio);
		String contacts = getString(R.string.contacts);
		
		byte[] foto = imageToByte();
		
		db.addRec(name, surname, birth, bio, contacts, foto);		
	}

	private byte[] imageToByte() {
		byte[] bytes = null;
		try {
			InputStream imgStream = getAssets().open("foto.jpg");
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = imgStream.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}
			buffer.flush();			
			bytes = buffer.toByteArray();					
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bytes ;
	}

	private Bitmap byteToImage(byte[] blob) {
		InputStream is = new ByteArrayInputStream(blob);
		Bitmap image = BitmapFactory.decodeStream(is);
		Bitmap cropFoto = null;
    	if (image.getWidth() >= image.getHeight()){
    		cropFoto = Bitmap.createBitmap(
    		    image, 
    		    image.getWidth()/2 - image.getHeight()/2,
    		    0,
    		    image.getHeight(), 
    		    image.getHeight()
    		    );
    	}else{
    		cropFoto = Bitmap.createBitmap(
    			image,
    		    0, 
    		    image.getHeight()/2 - image.getWidth()/2,
    		    image.getWidth(),
    		    image.getWidth() 
    		    );
    	}
		Bitmap bmp = Bitmap.createScaledBitmap(cropFoto, 128, 128, true);
		return bmp;
	}
}

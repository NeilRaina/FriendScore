package com.prnk.friendscore;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class CreateActivity extends Activity {
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		listItems.add("Sample Team 1");
		listItems.add("Sample Team 2");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		getActionBar().setTitle(R.string.scoreboardNewGame);
		adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1,
	            listItems);
	    ListView teamListView = (ListView) findViewById(R.id.teamListView);
		teamListView.setAdapter(adapter);
	}
	
	public void onAddTeamClick(View v) {
		final Dialog dialog = new Dialog(this);
		 dialog.setContentView(R.layout.newteamdialog);
		 dialog.setTitle(R.string.newTeamDialogMessage);

		 Button button = (Button) dialog.findViewById(R.id.dialogCreateButton);
		 button.setOnClickListener(new Button.OnClickListener() {
		     public void onClick(View v) {
		            EditText editTeamName = (EditText)dialog.findViewById(R.id.editNewTeamName);
		            String newName = editTeamName.getText().toString();
		            dialog.dismiss();
		            if(newName != "") {
		            	listItems.add(newName);
		            	adapter.notifyDataSetChanged();
		            }
		    }
		 });   
		dialog.show();
	}
}

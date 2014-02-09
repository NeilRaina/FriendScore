package com.prnk.friendscore;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;


public class CreateActivity extends ListActivity {
    GameObject currentGame;
    TeamListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		currentGame = new GameObject();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		getActionBar().setTitle(R.string.scoreboardNewGame);
		setupListView();
		setupRemoveButton();
	}
	
	private void DisplayEditTeamDialog(final String currentName) {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.edit_team_dialog);
		dialog.setTitle(R.string.newTeamDialogMessage);

		EditText edit = (EditText) dialog.findViewById(R.id.editTeamName);
		edit.setText(currentName);
		
		Button doneButton = (Button) dialog.findViewById(R.id.dialogDoneButton);
		doneButton.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		    		EditText editTeamName = (EditText)dialog.findViewById(R.id.editTeamName);
		            String newName = editTeamName.getText().toString();
		            if(!newName.equals("")) {
		            	if(!currentGame.ContainsTeamName(newName)) {
		            		if(currentName.equals("")) {
		            			currentGame.teams.add(new TeamObject(newName));
		            		} else {
		            			currentGame.GetTeamByName(currentName).SetName(newName);
		            		}
				            dialog.dismiss();
		            		adapter.notifyDataSetChanged();
		            	}
		            }
		    }
		});   
		
		Button cancelButton = (Button) dialog.findViewById(R.id.dialogCancelButton);
		cancelButton.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		            dialog.dismiss();
		    }
		}); 
		dialog.show();
	}

	public void onAddTeamClick(View v) {
		DisplayEditTeamDialog("");
	}
	
	private void setupRemoveButton() {
		Button remove = (Button) findViewById(R.id.removeTeam);
		remove.setOnClickListener(new Button.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   currentGame.RemoveSelectedTeams();
			   adapter.notifyDataSetChanged();
		   }
		});	  
	}
	
	private void setupListView() {
		getListView().setItemsCanFocus(false);
		getListView().setTextFilterEnabled(true);
		adapter = new TeamListAdapter(this, R.layout.checkboxed_listview_item, currentGame);
		ListView teamListView = getListView();
		teamListView.setAdapter(adapter);
	}
	  
	private class TeamListAdapter extends ArrayAdapter<TeamObject> {
		private GameObject game;
		private Context context;
		
		public TeamListAdapter(Context context, int teamNameResourceId, GameObject game) {
			super(context, teamNameResourceId, game.teams);
			this.context = context;
			this.game = game;
		}
		
		@Override
		public int getCount() {
			if(game.teams.isEmpty()) {
				return 1;
			}
			return game.teams.size();
		}
		
		@Override
		public boolean isEmpty () {
			return game.teams.isEmpty();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(isEmpty()) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    convertView = inflater.inflate(R.layout.empty_team_list_item, parent, false);
			    return convertView;
			}
			
			CheckBox box;
			if(convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    convertView = inflater.inflate(R.layout.checkboxed_listview_item, parent, false);
			    box = (CheckBox) convertView.findViewById(R.id.teamCheckBox);
			    box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						TeamObject team = game.GetTeamByName(buttonView.getText().toString());
						team.SetSelected(isChecked);
					}
				});
			    box.setText(game.Team(position).Name());
			    convertView.setTag(box);
			    
			    Button edit = (Button) convertView.findViewById(R.id.editTeamName);
			    edit.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						CheckBox box = (CheckBox) ((View)v.getParent()).findViewById(R.id.teamCheckBox);
						DisplayEditTeamDialog(box.getText().toString());
					}
			    });
			} else {
				box = (CheckBox) convertView.getTag();
				if(box == null) {
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				    convertView = inflater.inflate(R.layout.checkboxed_listview_item, parent, false);
				    box = (CheckBox) convertView.findViewById(R.id.teamCheckBox);
				    box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							TeamObject team = game.GetTeamByName(buttonView.getText().toString());
							team.SetSelected(isChecked);
						}
					});
				    box.setText(game.Team(position).Name());
				    convertView.setTag(box);
				    
				    Button edit = (Button) convertView.findViewById(R.id.editTeamName);
				    edit.setOnClickListener(new Button.OnClickListener() {
						@Override
						public void onClick(View v) {
							CheckBox box = (CheckBox) ((View)v.getParent()).findViewById(R.id.teamCheckBox);
							DisplayEditTeamDialog(box.getText().toString());
						}
				    });
				}
			}

			TeamObject team = this.game.Team(position);
			if(team != null) {
				box.setText(team.Name());
				box.setChecked(team.Selected());
			}
		    return convertView;
		}
	}
}

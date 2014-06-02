package ime.pfc.CheckApp.Business;

import ime.pfc.CheckApp.Data.DBHelper;
import ime.pfc.CheckApp.Data.model.ActionBox;
import ime.pfc.CheckApp.Data.model.CalendarAdapter;
import ime.pfc.CheckApp.Data.model.CheckLine;
import ime.pfc.CheckApp.Presentation.activity.Lista;
import ime.pfc.CheckApp.Presentation.activity.Lista_Calendar;
import ime.pfc.CheckApp.Presentation.activity.OtherLists;
import ime.pfc.CheckApp.Presentation.otherListsView.OtherListAdapter;
import ime.pfc.CheckApp.Presentation.swipelistview.ItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.widget.ListView;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.CheckApp.R;

public class Controller extends Application {
	private ActionBox actBox;
	public SwipeListView swipelistview;
	private DBHelper dbHelper;
	private CalendarAdapter calAdp;
	private String strListName;
	public ItemAdapter itAdapter;
	public OtherListAdapter olAdapter;
	public List<CheckLine> itemData;
	public List<ActionBox> actBoxData;
	private Activity act;

	public void setActionBox(ActionBox actBox) {

		this.actBox = actBox;
	}

	public void setActionBox(int id) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(getApplicationContext());
		}
		this.actBox = dbHelper.getActionBoxById(id);

	}

	public void setActionBox(String strName) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(getApplicationContext());
		}

		this.actBox = dbHelper.getActionBoxByName(strName);

	}

	public void setActionBox(String strName, Activity actCurrent) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(getApplicationContext());
		}

		this.actBox = dbHelper.getActionBoxByName(strName);
		if (strName.equals(getResources().getString(R.string.dbCALENDAR))) {
			this.act = actCurrent;
			calAdp = new CalendarAdapter(actBox.getId(), this);
			actBox.setCheckLines(calAdp.getCheckLines(actCurrent));
		}
	}

	public void goToList(String strListName, String strActBoxName,
			Activity actCurrent) {
		setActionBox(strActBoxName);
		this.strListName = strListName;
		Intent iChamaLista = new Intent(actCurrent, Lista.class);
		actCurrent.startActivity(iChamaLista);
		actCurrent.overridePendingTransition(R.anim.slide2, R.anim.slide);

	}

	public String getActionBoxName() {
		return actBox.getName();
	}

	/**
	 * Loads by copying to the current List reference from the values from
	 * Database
	 */
	public void reloadActionBox() {
		this.actBox.getCheckLines().clear();
		this.actBox.getCheckLines().addAll(dbHelper.getAllToDosByTag(this.actBox.getId()));
	}

	/**
	 * It's supposed to return the lists made by the user. dbHelper NEEDs
	 * another method!
	 * 
	 * @return List<ActionBox>
	 */
	public List<ActionBox> getOtherLists() {
		if (dbHelper == null) {
			dbHelper = new DBHelper(getApplicationContext());
		}
		setOtherLists();
		return actBoxData;
	}

	public void setOtherLists() {
		if (dbHelper == null) {
			dbHelper = new DBHelper(getApplicationContext());
		}

		if (actBoxData == null) {
			actBoxData = new ArrayList<ActionBox>();
		}
		actBoxData.clear();
		actBoxData.addAll(dbHelper.getAllAdditionalActionBoxes());
	}

	public void forwardChecklineToAnotherList(int position,
			int actBoxDataPosition, Activity currActivity) {

		CheckLine chkLnNew = new CheckLine();
		CheckLine chkOrigin = itemData.get(position);
		ActionBox actBoxDestination = actBoxData.get(actBoxDataPosition);
		String calendarName = getResources().getString(R.string.dbCALENDAR);
		if (actBoxDestination.getName().equals(calendarName)) {
			chkLnNew.setText(chkOrigin.getText());
			addEvent(currActivity, chkLnNew);
		} else {

			// ----------------------setting values---------------------/

			// text

			chkLnNew.setText(chkOrigin.getText());

			// order
			if (actBoxDestination.getCheckLines() == null) {
				actBoxDestination.setCheckLines(getAllCheckLines());
			}
			chkLnNew.setOrder(actBoxDestination.size() + 1);

			// ActionBox
			chkLnNew.setActionbox_id(actBoxDestination.getId());

			dbHelper.createCheckLine(chkLnNew);
			reloadActionBox();
			itAdapter.notifyDataSetChanged();
		}

	}

	public List<ActionBox> getAllLists() {
		if (dbHelper == null) {
			dbHelper = new DBHelper(getApplicationContext());
		}

		setAllLists();
		return actBoxData;
	}

	public List<String> getAllListsName() {
		setAllLists();
		Iterator<ActionBox> i = actBoxData.iterator();
		ArrayList<String> lstName = new ArrayList<String>();
		while (i.hasNext()) {
			ActionBox actionBox = (ActionBox) i.next();
			lstName.add(actionBox.getName());
		}

		return lstName;
	}

	public void setAllLists() {
		if (dbHelper == null) {
			dbHelper = new DBHelper(getApplicationContext());
		}

		if (actBoxData == null) {
			actBoxData = new ArrayList<ActionBox>();
		}
		actBoxData.clear();
		actBoxData.addAll(dbHelper.getAllActionBoxes());
	}

	// public List<String> getAllListsNames(){
	// if(dbHelper == null){
	// dbHelper = new DBHelper(getApplicationContext());
	// }
	//
	//
	// }
	// private List<CheckLine> getAllChecklines(DBHelper dbHlpCurrent){
	//
	// return dbHlpCurrent.getAllToDosByTag(actBox.getId());
	//
	// }
	public List<CheckLine> getAllCheckLines() {
		if ((actBox.getCheckLines()) == null) {
			actBox.setCheckLines(dbHelper.getAllToDosByTag(actBox.getId()));
		}
		return actBox.getCheckLines();
	}

	public void setCheckline(List<CheckLine> lstChk) {
		actBox.setCheckLines(lstChk);
	}

	/**
	 * N‹o precisa dessa assinatura! Basta deixar tudo no addCheckline (String)
	 * ...
	 * 
	 * @param strText
	 * @param dbHlpCurrent
	 */
	private void addCheckLine(String strText, DBHelper dbHlpCurrent) {
		CheckLine chkLnNew = new CheckLine();

		// ----------------------setting values---------------------/

		// text
		chkLnNew.setText(strText);

		// order
		if (actBox.getCheckLines() == null) {
			actBox.setCheckLines(getAllCheckLines());
		}
		chkLnNew.setOrder(actBox.size() + 1);

		// ActionBox
		chkLnNew.setActionbox_id(actBox.getId());

		chkLnNew = dbHlpCurrent.createCheckLine(chkLnNew);

		actBox.addCheckLine(chkLnNew);

	}

	private void addCheckLine(String strText, DBHelper dbHlpCurrent,
			String strDateCreated) {

		CheckLine chkLnNew = new CheckLine();

		// ----------------------setting values---------------------//

		// text
		chkLnNew.setText(strText);

		// order
		if (actBox.getCheckLines() == null) {
			actBox.setCheckLines(getAllCheckLines());
		}
		chkLnNew.setOrder(actBox.size() + 1);

		// ActionBox
		chkLnNew.setActionbox_id(actBox.getId());

		// Created at..

		chkLnNew.setCreated_at(strDateCreated);

		// ------------inserting---------------------------------//
		dbHlpCurrent.createCheckLine(chkLnNew);

	}

	public void persist() {
		if (dbHelper == null) {
			dbHelper = new DBHelper(getApplicationContext());
		}

		// ---- atualiza informacao sobre ActionBox ----//
		dbHelper.updateActionBox(actBox);

		// ---- atualiza informacao sobre as Checkline contidas no ActionBox
		// ----//
		for (int i = 0; i < actBox.size(); i++) {
			dbHelper.updateCheckLine(actBox.getCheckLines().get(i));
		}

	}

	
	public void setupBD() {
		if (dbHelper == null) {
			dbHelper = new DBHelper(getApplicationContext());
		}
		dbHelper.populateActionBoxesTable();
	}

	

	public long getId() {
		return actBox.getId();
	}

	public void addCheckLine(String str) {
		addCheckLine(str, dbHelper);
		itAdapter.notifyDataSetChanged();
	}

	/**
	 * Makes the Controller share the actionBox's List<Checkline>
	 * 
	 * @param lstChk
	 */
	public void loadAndShareListWith(List<CheckLine> lstChk) {

		// --- verifica se a lista de Checklines foi carregada ---//
		if (actBox.getCheckLines() == null) {
			actBox.setCheckLines(dbHelper.getAllToDosByTag(actBox.getId()));
		}

		// --- agora faz o ActBox ter suas listas referenciando a lista ser
		// compartilhada
		lstChk.addAll(actBox.getCheckLines());
		actBox.setCheckLines(lstChk);
	}

	/**
	 * Removes the Checkline at the referenced position holded by the current
	 * ActionBox and holded by the dataBase. No need for loadActionBox()...
	 * 
	 * @param position
	 */
	public void deleteChecklineAt(int position) {
		// swipelistview.closeAnimate(position );
		Log.e("lol", "cl id: " + actBox.toS());
		Log.e("lol", "cl id: " + actBox.get(position).toS());
		dbHelper.deleteCheckLine(actBox.get(position).getId(), act);
		actBox.getCheckLines().remove(position);
		itAdapter.notifyDataSetChanged();
		swipelistview.closeAnimate(position);

	}

	public void setListView(ListView lv) {
		this.swipelistview = (SwipeListView) lv;
	}

	public void showOtherLists(Activity actCurrent) {

		Intent iOtherList = new Intent(actCurrent, OtherLists.class);
		actCurrent.startActivity(iOtherList);
	}

	public void setAdapter(Lista lista) {
		itemData = new ArrayList<CheckLine>();
		itAdapter = new ItemAdapter(lista, R.layout.custom_row, itemData);
		swipelistview.setAdapter(itAdapter);
		this.loadAndShareListWith(itemData);
	}

	public void setAdapter(OtherLists lista) {
		setOtherLists();
		olAdapter = new OtherListAdapter(lista, R.layout.custom_row_lists,
				actBoxData);
		swipelistview.setAdapter(olAdapter);
		olAdapter.notifyDataSetChanged();
	}

	public boolean deviceHasGoogleAccount() {
		AccountManager accMan = AccountManager.get(this);
		Account[] accArray = accMan.getAccountsByType("com.google");
		return accArray.length >= 1 ? true : false;
	}

	public void shareChecklineAt(int position, Activity actCurrent) {
		// TODO Auto-generated method stub
		String strShared = actBox.get(position).getText();
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, strShared);
		sharingIntent.setType("text/plain");
		actCurrent.startActivity(Intent.createChooser(sharingIntent,
				getResources().getText(R.string.share_using)));
	}

	public void goToList_Calendar(String strListName, String strActBoxName,
			Activity actCurrent) {
		setActionBox(strListName, actCurrent);
		this.strListName = strListName;
		Intent iChamaLista = new Intent(actCurrent, Lista_Calendar.class);
		actCurrent.startActivity(iChamaLista);
		actCurrent.overridePendingTransition(R.anim.slide2, R.anim.slide);
	}

	public void addActionBox(String result) {
		ActionBox tempActBox = new ActionBox();

		// --------------- setting values ------------------//

		// text

		tempActBox.setName(result);

		dbHelper.createActionBox(tempActBox);
		actBoxData.clear();
		actBoxData.addAll(dbHelper.getAllAdditionalActionBoxes());
		olAdapter.notifyDataSetChanged();

	}

	public void deleteActionBoxAt(int position) {

		dbHelper.deleteActionBox(actBoxData.get(position).getId());
		actBoxData.clear();
		actBoxData.addAll(dbHelper.getAllAdditionalActionBoxes());
		olAdapter.notifyDataSetChanged();
		swipelistview.closeAnimate(position);

	}

	public void setAdapter(Lista_Calendar lista_Calendar) {
		itemData = new ArrayList<CheckLine>();
		itAdapter = new ItemAdapter(lista_Calendar, R.layout.custom_row,
				itemData);
		swipelistview.setAdapter(itAdapter);
		this.loadAndShareListWith(itemData);
		itAdapter.notifyDataSetChanged();

	}

	public void addEvent(Activity crtActivity) {
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(2012, 0, 19, 7, 30);
		Calendar endTime = Calendar.getInstance();
		endTime.set(2012, 0, 19, 8, 30);
		Intent intent = new Intent(Intent.ACTION_INSERT)
				.setData(Events.CONTENT_URI).putExtra(Events.TITLE, "")
				.putExtra(Events.DESCRIPTION, "");
		crtActivity.startActivity(intent);
		// DialogFragment dFrag = new AddCheckLineDialog();
		// dFrag.show(getFragmentManager(), "Add");
	}

	public void addEvent(Activity crtActivity, CheckLine chkLine) {
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(2012, 0, 19, 7, 30);
		Calendar endTime = Calendar.getInstance();
		endTime.set(2012, 0, 19, 8, 30);
		Intent intent = new Intent(Intent.ACTION_INSERT)
				.setData(Events.CONTENT_URI).putExtra(Events.TITLE, "")
				.putExtra(Events.DESCRIPTION, chkLine.getText());
		crtActivity.startActivity(intent);

	}

}

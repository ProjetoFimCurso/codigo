package com.juntando_tudo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import pfc.ime.gtdmanager.helper.*;
import pfc.ime.gtdmanager.model.ActionBox;
import pfc.ime.gtdmanager.model.CheckLine;

public class Lista extends Activity {
	private final List<Integer> selecionados = new ArrayList<Integer>();

	private final List<String> ESTADOS = new ArrayList<String>();
	private List<CheckLine> lstCheckLine ;

	private String result;

	private ActionBox actBxInbox;

	private DBHelper dbTeste;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			codigoTeste();
		}
		catch(Exception erro){
			mostrarMSG(String.valueOf(erro), "erro");
		}
		loadList();

		setupActionBar();
		// Define o arquivo /layout/main.xml como layout principal da aplica��o
		setContentView(R.layout.lista);
		mostrarMSG("TOTAL  " +  String.valueOf(lstCheckLine.size()), "TOTAL");


		// Adapter para implementar o layout customizado de cada item


	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.add:
			add_method();
			
		}
		return super.onOptionsItemSelected(item);
	}
	public void loadList(){
		//	for(int i = 0; i< ESTADO.length ; i++ ) {
		//		ESTADOS.add(ESTADO[i] + i );
		Iterator<CheckLine> i = lstCheckLine.iterator();
		while (i.hasNext()) {

			ESTADOS.add(i.next().getText());
		}
	}





	public void loadList(long actbxId, DBHelper dbHelper){
		ActionBox actbxTested = dbHelper.getActionBoxById(actbxId);

		for(int i=0; i<5; i++){
			actbxTested.addCheckLine("Objeto no "+ i, dbHelper, new Date().toString());
		}


	}

	public void mostrarMSG(String strMSG, String strTitle){
		AlertDialog adMensagem = new AlertDialog.Builder(this).create();
		adMensagem.setMessage(strMSG);
		adMensagem.setTitle(strTitle);
		adMensagem.show();
	}

	private boolean add_method(){
		// get prompts.xml view
		LayoutInflater liAdd = LayoutInflater.from(this);
		View promptsView = liAdd.inflate(R.layout.add, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);
		final EditText userInput =  (EditText) promptsView.findViewById(R.id.etItem_new);
		userInput.requestFocus();
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// get user input and set it to result
				// edit text
				result = String.valueOf(userInput.getText());
				mostrarMSG(result, "Teste");
				actBxInbox.addCheckLine(result, dbTeste);
				onResume();
			}
		});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});



		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// settin position
		//WindowManager.LayoutParams alDlgParemters = alertDialog.getWindow().getAttributes();
		//	alDlgParemters.gravity = Gravity.TOP;
		//	alDlgParemters.y = 100;


		// forcing showing soft input
		alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		// show it
		alertDialog.show();
		return true;
	}

	@Override
	public void onResume(){
		super.onResume();
		//ListView
		ListView lsvEstados = (ListView) findViewById(R.id.lsvEstados);
		actBxInbox.loadAllCheckLines(dbTeste);
		ArrayAdapter<String> lsvEstadosAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// Recuperando o Estado selecionado de acordo com a sua posi��o no ListView
				CheckLine chkLine = actBxInbox.get(position);


				// Se o ConvertView for diferente de null o layout j� foi "inflado"
				View v = convertView;

				if(v==null) {
					// "Inflando" o layout do item caso o isso ainda n�o tenha sido feito
					LayoutInflater inflater = getLayoutInflater();
					v = (View) inflater.inflate(R.layout.item_estado, null);
				}

				// Recuperando o checkbox
				CheckBox chk = (CheckBox) v.findViewById(R.id.chkEstados);

				// Definindo um "valor" para o checkbox
				chk.setTag(chkLine);

				/** Definindo uma a��o ao clicar no checkbox. Aqui poderiamos armazenar um valor chave
				 * que identifique o objeto selecionado para que o mesmo possa ser, por exemplo, exclu�do
				 * mais tarde.
				 */
				chk.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						CheckBox chk = (CheckBox) v;
						LinearLayout vParent =  (LinearLayout) v.getParent();
						TextView tbTxt =  (TextView) vParent.findViewById(R.id.txvEstados);
						CheckLine chlnTemp =  (CheckLine) chk.getTag();
//						CheckLine chlnTemp = actBxInbox.getCheckLineByID(chlnID);
						if(chk.isChecked()) {
							
							tbTxt.setTextColor(Color.BLUE);
							Toast.makeText(getApplicationContext(), "Checbox de ID " + String.valueOf(chlnTemp.getId()) + " marcado!", Toast.LENGTH_SHORT).show();
							actBxInbox.setChecked(chlnTemp);
//							if(!selecionados.contains(chlnID))
//								selecionados.add(chlnID);
						} else {
							tbTxt.setTextColor(Color.BLACK);
							Toast.makeText(getApplicationContext(), "Checbox de ID " + String.valueOf(chlnTemp.getId()) + " desmarcado!", Toast.LENGTH_SHORT).show();
							actBxInbox.setUnCheked(chlnTemp);
//							if(selecionados.contains(chlnID))
//								selecionados.remove(Integer.valueOf(chlnID));
						}
						actBxInbox.persistCheckLine(chlnTemp, dbTeste);
					}
				});

				// Preenche o TextView do layout com o nome do Estado
				TextView txv = (TextView) v.findViewById(R.id.txvEstados);
				txv.setText(chkLine.getText());
				if(chkLine.isChecked() ) {
					chk.setChecked(true);
					txv.setTextColor(Color.BLUE);

				} else {
					chk.setChecked(false);
					txv.setTextColor(Color.BLACK);
				}

				return v;
			}

			@Override
			public long getItemId(int position) {
				CheckLine clTemp = actBxInbox.get(position);
				return clTemp.getId();

			}

			@Override
			public int getCount() {
				return actBxInbox.size();
			}
		};

		lsvEstados.setAdapter(lsvEstadosAdapter);


	}
	public void codigoTeste(){
		dbTeste = new DBHelper(this);
		//    ActionBox listaTeste = new ActionBox("bla");
		//    dbTeste.populateActionBoxesTable();
		// getAllActionBoxes
		//    List<ActionBox> lAB = new 	ArrayList<ActionBox>();
		//    lAB = dbTeste.getAllActionBoxes();
		//    Iterator<ActionBox> iAB = lAB.iterator();
		//    String strActionBox = "";
		//    while (iAB.hasNext()) {
		//		ActionBox actionBox = (ActionBox) iAB.next();
		//		strActionBox +=  "" + actionBox.getName() + "ID: "+ actionBox.getId() +  " \n";
		//		}
		//    loadList(1, dbTeste);
		actBxInbox = new ActionBox(1, dbTeste);
		mostrarMSG(String.valueOf(actBxInbox.getId()) , "ActionBox");
		lstCheckLine = actBxInbox.getAllChecklines(dbTeste);
		actBxInbox.loadAllCheckLines(dbTeste);

	}
}


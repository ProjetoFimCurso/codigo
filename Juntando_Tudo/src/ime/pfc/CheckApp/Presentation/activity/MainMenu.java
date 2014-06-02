package ime.pfc.CheckApp.Presentation.activity;

import com.CheckApp.R;

import ime.pfc.CheckApp.Business.Controller;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainMenu extends Activity {

	Controller aController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		aController = (Controller) getApplicationContext();
		if (aController.deviceHasGoogleAccount()) {
			mostrarMSG("Você tem uma conta Google!");

		} else {
			Intent addAccountIntent = new Intent(
					android.provider.Settings.ACTION_ADD_ACCOUNT)
					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			addAccountIntent.putExtra(Settings.EXTRA_ACCOUNT_TYPES,
					new String[] { "com.google" });
			this.startActivity(addAccountIntent);
		}
		aController.setupBD();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()) {
		case R.id.addGoogleUser:
			Intent addAccountIntent = new Intent(
					android.provider.Settings.ACTION_ADD_ACCOUNT)
					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			addAccountIntent.putExtra(Settings.EXTRA_ACCOUNT_TYPES,
					new String[] { "com.google" });
			this.startActivity(addAccountIntent);
			break;

		default:
			break;
		}
		return true;
	}

	public void listar(View v) {
		String sNome = getResources().getString(v.getId());
		// mostrarMSG(sNome, "Info Button" );
		aController.goToList((String) v.getTag(), sNome, this);

	}

	/**
	 * A (temporary) method to call a list of itens for Calendar. Requires
	 * refactory. Controller should be responsible for calling the activity,
	 * probably using a different method...
	 * 
	 * @param v
	 */
	public void listar_calendar(View v) {
		String sNome = getResources().getString(v.getId());
		// mostrarMSG(sNome, "Info Button" );
		aController.goToList_Calendar((String) v.getTag(), sNome, this);

	}

	public void showOtherLists(View v) {
		aController.showOtherLists(this);
	}

	public void mostrarMSG(String strMSG) {
		Toast t = Toast.makeText(this, strMSG, Toast.LENGTH_SHORT);
		t.show();
	}

}

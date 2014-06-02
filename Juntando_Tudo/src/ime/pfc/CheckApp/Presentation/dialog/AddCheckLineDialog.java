package ime.pfc.CheckApp.Presentation.dialog;

import ime.pfc.CheckApp.Business.Controller;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.CheckApp.R;

public class AddCheckLineDialog extends DialogFragment {

	private Controller aController;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		aController = (Controller) (getActivity()).getApplicationContext();
		LayoutInflater liAdd = LayoutInflater.from(getActivity());
		View promptsView = liAdd.inflate(R.layout.add, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);
		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.etItem_new);
		userInput.requestFocus();
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// get user input and set it to result
						// edit text
						String result = String.valueOf(userInput.getText());
//						Toast.makeText(
//								(Controller) (getActivity())
//										.getApplicationContext(), result,
//								Toast.LENGTH_SHORT).show();
						aController.addCheckLine(result);
						// aController.persist();
						// aController.loadActionBox();

					}
				});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		// forcing showing soft input
		AlertDialog aDialog = alertDialogBuilder.create();
		aDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		return aDialog;

	}
}

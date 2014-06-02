package ime.pfc.CheckApp.Presentation.swipelistview;

import ime.pfc.CheckApp.Business.Controller;
import ime.pfc.CheckApp.Data.model.CheckLine;
import ime.pfc.CheckApp.Presentation.dialog.ChangeDialog;

import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import com.CheckApp.R;

public class ItemAdapter extends ArrayAdapter<CheckLine> {

	List<CheckLine> data;
	Context context;
	int layoutResID;
	Controller aController;
	private CheckLine itemdata;

	public ItemAdapter(Context context, int layoutResourceId,
			List<CheckLine> data) {
		super(context, layoutResourceId, data);

		this.data = data;
		this.context = context;
		this.layoutResID = layoutResourceId;
		aController = (Controller) context.getApplicationContext();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		NewsHolder holder;// =null;
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResID, parent, false);

			holder = new NewsHolder();

			holder.itemText = (TextView) row
					.findViewById(R.id.example_itemname);
			holder.itemText.setTextAppearance(context, R.style.MyListTitle);
			holder.chk = (CheckBox) row.findViewById(R.id.example_image);
			holder.button1 = (ImageButton) row.findViewById(R.id.swipe_delete);
			holder.button2 = (ImageButton) row.findViewById(R.id.swipe_share);
			holder.button3 = (ImageButton) row.findViewById(R.id.swipe_forward);
			row.setTag(holder);
		} else {
			holder = (NewsHolder) row.getTag();
		}

		itemdata = data.get(position);

		holder.chk.setTag(position);
		holder.button1.setTag(position);
		holder.button2.setTag(position);
		holder.button3.setTag(position);

		holder.itemText.setText(itemdata.getText());
		holder.chk.setOnClickListener(chkClickListener);
		if (itemdata.isChecked()) {
			holder.chk.setChecked(true);
			holder.itemText.setTextAppearance(context,
					R.style.MyListTitleChecked);
		} else {
			holder.chk.setChecked(false);
			holder.itemText.setTextAppearance(context, R.style.MyListTitle);
		}

		// holder.icon.setImageDrawable(itemdata.getIcon());

		holder.button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				Toast.makeText(context, "Button 1 Clicked", Toast.LENGTH_SHORT)
//						.show();
				int position = (Integer) v.getTag();
				aController.deleteChecklineAt(position);

			}
		});

		holder.button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				Toast.makeText(context, "Button 2 Clicked", Toast.LENGTH_SHORT)
//						.show();
				int position = (Integer) v.getTag();
				aController.shareChecklineAt(position, (Activity) getContext());
			}
		});

		holder.button3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				Toast.makeText(context, "Button 3 Clicked", Toast.LENGTH_SHORT);
				int position = (Integer) v.getTag();

				DialogFragment dfChange = new ChangeDialog();
				Bundle args = new Bundle();
				args.putInt("position", position);
				dfChange.setArguments(args);

				dfChange.show(((Activity) getContext()).getFragmentManager(),
						"change");
			}
		});

		return row;

	}

	private OnClickListener chkClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			CheckBox chk = (CheckBox) v;
			View vParent = (View) v.getParent();
			TextView tbTxt = (TextView) vParent
					.findViewById(R.id.example_itemname);
			int position = (Integer) v.getTag();
			// CheckLine chlnTemp = (CheckLine) chk.getTag();
			// CheckLine chlnTemp = actBxInbox.getCheckLineByID(chlnID);
			if (chk.isChecked()) {

				// tbTxt.setTextAppearance(context, R.style.MyListTitleChecked);
//				Toast.makeText(
//						context,
//						"Checbox de ID "
//								+ String.valueOf(data.get(position).getId())
//								+ " marcado! Seu texto Ž: " + tbTxt.getText(),
//						Toast.LENGTH_SHORT).show();
				data.get(position).setChecked(true);
				// if(!selecionados.contains(chlnID))
				// selecionados.add(chlnID);
			} else {
				tbTxt.setTextAppearance(context, R.style.MyListTitle);
				// tbTxt.setTextColor(Color.BLACK);
//				Toast.makeText(
//						context,
//						"Checbox de ID "
//								+ String.valueOf(data.get(position).getId())
//								+ " desmarcado!Seu texto Ž: " + tbTxt.getText(),
//						Toast.LENGTH_SHORT).show();
				data.get(position).setChecked(false);
				// if(selecionados.contains(chlnID))
				// selecionados.remove(Integer.valueOf(chlnID));
			}
			// actBxInbox.persistCheckLine(chlnTemp, dbTeste);
		}

	};

	static class NewsHolder {

		TextView itemText;
		CheckBox chk;
		ImageButton button1;
		ImageButton button2;
		ImageButton button3;

	}

}

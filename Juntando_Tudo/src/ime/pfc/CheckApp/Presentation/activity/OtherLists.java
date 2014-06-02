package ime.pfc.CheckApp.Presentation.activity;

import ime.pfc.CheckApp.Business.Controller;
import ime.pfc.CheckApp.Data.model.ActionBox;
import ime.pfc.CheckApp.Presentation.dialog.AddActionBoxDialog;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.CheckApp.R;

public class OtherLists extends Activity {

	public static SwipeListView swipelistview;
	Controller aController;
	private OtherLists tempActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);
		tempActivity = this;

		aController = (Controller) getApplicationContext();
		setSwipeListView();

		aController.setListView(swipelistview);

		// Controller.LoadActionBox();
		// aController.populaActionBoxTeste();
		// Toast.makeText(this , String.valueOf(aController.getId() ),
		// Toast.LENGTH_SHORT).show();
		aController.setAdapter(this);

	}

	public int convertDpToPixel(float dp) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista, menu);
		getActionBar().setTitle(R.string.title_activity_other_list);
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	private boolean add_method() {
		DialogFragment dFrag = new AddActionBoxDialog();
		dFrag.show(getFragmentManager(), "AddActBox");
		return true;

	}

	public void setSwipeListView() {
		swipelistview = (SwipeListView) findViewById(R.id.example_swipe_lv_list);

		swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onOpened(int position, boolean toRight) {
			}

			@Override
			public void onClosed(int position, boolean fromRight) {
			}

			@Override
			public void onListChanged() {
			}

			@Override
			public void onMove(int position, float x) {
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				Log.d("swipe", String.format("onStartOpen %d - action %d",
						position, action));
			}

			@Override
			public void onStartClose(int position, boolean right) {
				Log.d("swipe", String.format("onStartClose %d", position));
			}

			@Override
			public void onClickFrontView(int position) {
				Log.d("swipe", String.format("onClickFrontView %d", position));
				ActionBox tempActBox = new ActionBox();
				tempActBox = (ActionBox) swipelistview.getAdapter().getItem(
						position);
				String othListName = tempActBox.getName();
				aController.goToList(othListName, othListName, tempActivity);
				// swipelistview.openAnimate(position); //when you touch front
				// view it will open

			}

			@Override
			public void onClickBackView(int position) {
				Log.d("swipe", String.format("onClickBackView %d", position));

				swipelistview.closeAnimate(position);// when you touch back view
														// it will close
			}

			@Override
			public void onDismiss(int[] reverseSortedPositions) {

			}

		});

		// These are the swipe listview settings. you can change these
		// setting as your requirement
		swipelistview.setSwipeMode(SwipeListView.SWIPE_ACTION_NONE); // there
																		// are
																		// five
																		// swiping
																		// modes
		swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_MODE_LEFT); // there
																			// are
																			// four
																			// swipe
																			// actions
		swipelistview.setSwipeActionRight(SwipeListView.SWIPE_MODE_NONE);
		swipelistview.setOffsetLeft(convertDpToPixel(80f)); // left side offset
		swipelistview.setOffsetRight(convertDpToPixel(0f)); // right side offset
		swipelistview.setAnimationTime(64); // Animation time
		swipelistview.setSwipeOpenOnLongPress(true); // enable or disable
														// SwipeOpenOnLongPress

	}
}

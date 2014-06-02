package ime.pfc.CheckApp.Data.model;

import ime.pfc.CheckApp.Business.Controller;
import ime.pfc.CheckApp.Data.DBHelper;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.text.format.Time;

public class CalendarAdapter {

	private int CALENDAR_ID;
	private List<CheckLine> lstChkLn;
	private StringBuilder calendar_ids = new StringBuilder("(");
	private Cursor mCursor = null;
	private static final String[] COLS = new String[] {
			CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART };
	private Controller aController;
	private Activity currAct;

	public List<CheckLine> getCheckLines(Activity curAct) {
		this.currAct = curAct;
		Format df = DateFormat.getDateFormat(aController
				.getApplicationContext());
		Format tf = DateFormat.getTimeFormat(aController
				.getApplicationContext());
		String title = "N/A";
		Long start = 0L;
		long id = 2L;
		mCursor = query5();

		// if(mCursor == null) return lstChkLn;

		if (mCursor.moveToFirst()) {
			do {
				// adding to checklines list
				CheckLine chkLnNew = new CheckLine();

				try {
					title = mCursor.getString(1);
					start = mCursor.getLong(3);
					id = mCursor.getLong(0);
				} catch (Exception e) {
					// ignore

				}
				chkLnNew.setText(title + " -------------- " + df.format(start) + " "
						+ tf.format(start) );

				// order

				chkLnNew.setOrder(mCursor.getPosition());

				// ActionBox
				chkLnNew.setActionbox_id(CALENDAR_ID);
				chkLnNew.setCalendarId(id);

				chkLnNew = (new DBHelper(aController.getApplicationContext()))
						.updateCalendarCheckLine(chkLnNew);

				calendar_ids.append(chkLnNew.getId());
				calendar_ids.append(", ");

				lstChkLn.add(chkLnNew);

			} while (mCursor.moveToNext());
		}
		calendar_ids.delete(calendar_ids.length() - 2, calendar_ids.length());
		calendar_ids.append(")");
		(new DBHelper(aController.getApplicationContext()))
				.deleteCalendarCheckLines(calendar_ids.toString());
		return lstChkLn;

	}

	public Cursor query5() {
		Time t = new Time(Time.getCurrentTimezone());
		t.setToNow();
		t.set(0, 0, 0, t.monthDay, t.month, t.year);
		long dtStart = t.toMillis(true);
		t.set(59, 59, 23, 31, t.month, t.year);
		long dtEnd = t.toMillis(false);

		mCursor = currAct.getContentResolver().query(
				CalendarContract.Events.CONTENT_URI,
				new String[] { "_id", "title", "description", "dtstart" },
				"( dtstart > " + dtStart + " and dtend < " + dtEnd + " )",
				null, "dtstart ASC");
		return mCursor;
	}

	public Cursor query4() {

		Time t = new Time(Time.getCurrentTimezone());
		t.setToNow();
		// long start = t.toMillis(false);
		String dtStart = Long.toString(t.toMillis(false));
		t.set(59, 59, 23, t.monthDay, t.month, t.year);
		// long end = t.toMillis(false);
		String dtEnd = Long.toString(t.toMillis(false));
		Cursor cursor = currAct.getContentResolver().query(
				CalendarContract.Events.CONTENT_URI,
				new String[] { "_id", "title", "description", "dtstart" },
				"( dtstart > " + dtStart + " and + )", null, "dtstart ASC");
		return mCursor;
	}

	public Cursor query2() {
		mCursor = currAct.getContentResolver().query(

		CalendarContract.Events.CONTENT_URI, COLS, null, null, null

		);
		return mCursor;
	}

	public Cursor query1() {
		Cursor cur = null;
		String selection = "((" + CalendarContract.Events.DTSTART
				+ " <= ?) AND (" + CalendarContract.Events.DTEND + " >= ?))";

		Time t = new Time();
		t.setToNow();
		String dtStart = Long.toString(t.toMillis(false));
		t.set(59, 59, 23, t.monthDay, t.month, t.year);
		String dtEnd = Long.toString(t.toMillis(false));
		t.set(00, 00, 00, t.monthDay, t.month, t.year);
		String[] selectionArgs = new String[] { dtStart, dtEnd };
		cur = currAct.getContentResolver().query(
				CalendarContract.Events.CONTENT_URI, COLS, selection,
				selectionArgs, null);
		return cur;

	}

	public Cursor query3() {
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
				.buildUpon();
		Time t = new Time(Time.getCurrentTimezone());
		t.setToNow();
		String dtStart = Long.toString(t.toMillis(false));
		t.set(59, 59, 23, t.monthDay, t.month, t.year);
		String dtEnd = Long.toString(t.toMillis(false));
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true));
		t.set(59, 59, 23, t.monthDay, t.month, t.year);
		ContentUris.appendId(eventsUriBuilder, t.toMillis(true));
		Uri eventsUri = eventsUriBuilder.build();
		Cursor cursor = null;
		cursor = aController
				.getApplicationContext()
				.getContentResolver()
				.query(eventsUri, COLS, null, null,
						CalendarContract.Instances.DTSTART + " ASC");
		return cursor;
	}

	public CalendarAdapter(int actBoxID, Controller aController) {
		this.CALENDAR_ID = actBoxID;
		lstChkLn = new ArrayList<CheckLine>();
		this.aController = aController;
	}
}
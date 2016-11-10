package fj.swsk.cn.eqapp.subs.collect.Common;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseMulSelAdapter extends BaseAdapter {

	public Set<Long> selectedIds;



	public boolean isMultiChoiceMode = false;

	public Context context;

	public BaseMulSelAdapter(Context context) {
		super();
		this.context = context;
		this.selectedIds = new HashSet<Long>();
	}


	public void setMode(boolean isMultiChoiceMode){
		if (isMultiChoiceMode)
			multiChoiceMode();

		else
			normalMode();

		onModeChange();
	}
	public boolean toggleChoiceMode() {
		if (isMultiChoiceMode)
			normalMode();
		else
			multiChoiceMode();
		onModeChange();
		return isMultiChoiceMode;
	}

	public void multiChoiceMode() {
		selectedIds.clear();
		isMultiChoiceMode = true;


	}

	public void normalMode() {
		selectedIds.clear();
		isMultiChoiceMode = false;

	}
	abstract  public void onModeChange();






}

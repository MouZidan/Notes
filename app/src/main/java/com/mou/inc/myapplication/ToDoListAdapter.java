package com.mou.inc.myapplication;



        import android.app.Activity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.TextView;

        import java.util.ArrayList;


public class ToDoListAdapter extends ArrayAdapter<String> {

    private final Activity mContext;
    private final String[] mItemname;
    private final ArrayList<Boolean> mB;


    public ToDoListAdapter(Activity context, String[] itemname , ArrayList<Boolean> b) {
        super(context, R.layout.view_list_dialog_create, itemname);
        // TODO Auto-generated constructor stub

        this.mContext=context;
        this.mItemname=itemname;
        this.mB =b;

    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=mContext.getLayoutInflater();

        View rowView=inflater.inflate(R.layout.activity_checklist_listview_adapter, null,true);

        final CheckBox cb = (CheckBox) rowView.findViewById(R.id.adapter_todoList_cp_itemname);

        cb.setText(mItemname[position]);
        cb.setChecked(mB.get(position));
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ArrayList<Boolean> activityBooleanArray = ToDoActivity.b;

                Log.d("P!P", "position="+position+" cbState="+cb.isChecked()+" BooleanArraylength="+activityBooleanArray.size());

                activityBooleanArray.set(position,cb.isChecked());




            }
        });

        return rowView;

    }






}
package com.mou.inc.myapplication;



        import android.app.Activity;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;



public class CreateListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public CreateListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.view_list_dialog_create, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.view_list_dialog_create, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.text_list_create_dialog);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image_list_create_dialog);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;

    };
}
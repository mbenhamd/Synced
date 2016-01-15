package com.synced.DirectoryPackage;

/**
 * Created by PC on 22/08/2015.
 */
import java.util.List;
import com.synced.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.template_directory, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.directory_name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.directory_icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());
        return convertView;
    }
}

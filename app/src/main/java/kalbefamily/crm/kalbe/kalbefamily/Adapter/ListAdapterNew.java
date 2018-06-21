package kalbefamily.crm.kalbe.kalbefamily.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.R;

/**
 * Created by User on 2/27/2018.
 */

public class ListAdapterNew extends BaseAdapter {

    Context context;
    private final List<String> values;
    private final List<String> strDate;
    private final List<Integer> images;
    private final List<String> time;

    public ListAdapterNew(Context context, List<String> ltdt, List<String> strDate, List<Integer> images, List<String> time){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.values = ltdt;
        this.strDate = strDate;
        this.images = images;
        this.time = time;
        //tess
        String a = "aaa";

    }

    @Override
    public int getCount() {
        return values.size();
        // return values.length
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.subTitle = (TextView) convertView.findViewById(R.id.subTitle);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.list_image);
            viewHolder.time = (TextView) convertView.findViewById(R.id.jam);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.title.setText(values.get(position));
        viewHolder.subTitle.setText(strDate.get(position));
        viewHolder.icon.setImageResource(images.get(position));
        viewHolder.time.setText(time.get(position));
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView subTitle;
        ImageView icon;
        TextView time;

    }

}

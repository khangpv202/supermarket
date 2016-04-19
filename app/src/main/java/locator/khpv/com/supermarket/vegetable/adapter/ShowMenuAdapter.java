package locator.khpv.com.supermarket.vegetable.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import locator.khpv.com.supermarket.R;
import locator.khpv.com.supermarket.vegetable.model.Vegetable;

/**
 * Created by Administrator on 4/19/2016.
 */
public class ShowMenuAdapter extends ArrayAdapter<Vegetable> {

    public ShowMenuAdapter(Context context, int resource, List<Vegetable> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.vegetable_header_child_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvCalo = (TextView) convertView.findViewById(R.id.tvCalo);
            viewHolder.tvCost = (TextView) convertView.findViewById(R.id.tvCost);
            viewHolder.tvNameOfMenu = (TextView) convertView.findViewById(R.id.tvNameOfMenu);
            viewHolder.tvNameOfVegetable = (TextView) convertView.findViewById(R.id.tvNameOfVegetable);
            convertView.setTag(viewHolder);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        Vegetable vegetable = getItem(position);
        viewHolder.tvCost.setText(vegetable.getCost());
        viewHolder.tvCalo.setText(vegetable.getCalo());
        viewHolder.tvNameOfMenu.setText("will have menu soon ^^");
        viewHolder.tvNameOfVegetable.setText(vegetable.getDisplayName());
        return convertView;
    }


    private static class ViewHolder {
        TextView tvCost;
        TextView tvCalo;
        TextView tvNameOfMenu;
        TextView tvNameOfVegetable;
    }
}

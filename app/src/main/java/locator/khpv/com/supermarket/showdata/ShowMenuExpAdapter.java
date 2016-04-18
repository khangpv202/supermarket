package locator.khpv.com.supermarket.showdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.firebase.client.ValueEventListener;

import java.util.List;
import java.util.Map;

import locator.khpv.com.supermarket.R;
import locator.khpv.com.supermarket.showdata.model.Vegetable;

/**
 * Created by Administrator on 4/15/2016.
 */
public class ShowMenuExpAdapter extends BaseExpandableListAdapter {
    List<String> headerKey;
    Map<String, List<Vegetable>> stringListMap;
    Context context;

    public ShowMenuExpAdapter(Context context, List<String> headerKey, Map<String, List<Vegetable>> stringListMap) {
        this.context = context;
        this.headerKey = headerKey;
        this.stringListMap = stringListMap;
    }

    @Override
    public int getGroupCount() {
        return headerKey.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return stringListMap.get(headerKey.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerKey.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return stringListMap.get(headerKey.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.vegetable_header_group_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.tvContent);
        textView.setText(headerKey.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.vegetable_header_child_item, null);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

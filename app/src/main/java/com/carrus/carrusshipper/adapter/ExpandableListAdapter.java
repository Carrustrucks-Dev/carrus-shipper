package com.carrus.carrusshipper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrus.carrusshipper.R;
import com.carrus.carrusshipper.model.ExpandableChildItem;
import com.carrus.carrusshipper.model.Header;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Saurbhv on 10/30/15.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter{

    private Context _context;
    private List<Header> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Header, List<ExpandableChildItem>> _listDataChild;

    public ExpandableListAdapter(Context context,List<Header> listDataHeader,
                                 HashMap<Header, List<ExpandableChildItem>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ExpandableChildItem expandableChildItem = (ExpandableChildItem) getChild(groupPosition, childPosition);
        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView typeCargoTxtView, weightTxtView;
        switch (expandableChildItem.getType()) {
            case 0:
                convertView = infalInflater.inflate(R.layout.itemview_cargodetails, null);
                typeCargoTxtView = (TextView) convertView.findViewById(R.id.typeCargoTxtView);
                weightTxtView = (TextView) convertView.findViewById(R.id.weightTxtView);
                typeCargoTxtView.setText(expandableChildItem.getName());
                weightTxtView.setText(expandableChildItem.getDetail()+" Ton");


                break;
            case 1:
                convertView = infalInflater.inflate(R.layout.itemview_desc, null);
                TextView mDescTxtView=(TextView)convertView.findViewById(R.id.descTxtView);
                if(expandableChildItem.getDetail()!=null)
                mDescTxtView.setText(expandableChildItem.getDetail());
                break;

        }
//        if (expandableChildItem.getType() == 1) {
////            TextView name = (TextView) convertView.findViewById(R.id.name);
////            TextView details = (TextView) convertView.findViewById(R.id.details);
////            name.setText(expandableChildItem.getName());
////            details.setText(expandableChildItem.getDetail());
//        } else if (expandableChildItem.getType() == 2) {
//
//        } else if (expandableChildItem.getType() == 3) {
//
//        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final Header mHeader = (Header) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header, null);
        }

        final TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.header_title);
        lblListHeader.setText(mHeader.getName());

        final ImageView btn_expand_toggle=(ImageView) convertView.findViewById(R.id.btn_expand_toggle);

//        if (mHeader.isVisible()) {
//            btn_expand_toggle.setImageResource(R.mipmap.circle_minus);
//        } else {
//            btn_expand_toggle.setImageResource(R.mipmap.circle_plus);
//        }

        int imageResourceId = isExpanded ? R.mipmap.circle_minus
                : R.mipmap.circle_plus;
        btn_expand_toggle.setImageResource(imageResourceId);



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

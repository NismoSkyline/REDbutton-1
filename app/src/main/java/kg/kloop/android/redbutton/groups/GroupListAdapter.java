package kg.kloop.android.redbutton.groups;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexwalker.sendsmsapp.R;

import java.util.ArrayList;

/**
 * Created by erlan on 28.04.2017.
 */

public class GroupListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GroupMembership> groupList;
    //private Tab1 fragment;
    private Fragment fragment;

    //public GroupListAdapter (Context context, ArrayList<GroupMembership> list, Tab1 tab1Fragment){
    public GroupListAdapter (Context context, ArrayList<GroupMembership> list, Fragment fragment){
        this.context = context;
        this.groupList = list;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_list_item, parent, false);
        }

        TextView groupName = (TextView) convertView.findViewById(R.id.group_item_groupname);
        final TextView membershipInfo = (TextView) convertView.findViewById(R.id.group_item_membership);
        final ImageView image = (ImageView) convertView.findViewById(R.id.group_item_image);

        final GroupMembership thisGroup = groupList.get(position);

        groupName.setText(thisGroup.getGroupName());
        boolean isMember = thisGroup.isMember();
        boolean isPending = thisGroup.isPending();

        if (isMember){
            image.setImageResource(R.drawable.member);
            membershipInfo.setText("вы состоите в этой группе");
        } else if (isPending){
            image.setImageResource(R.drawable.pending);
            membershipInfo.setText("запрос отправлен");
        } else {
            image.setImageResource(R.drawable.send_request);
        }

        final boolean finalIsPending = isPending;
        final boolean finalIsMember = isMember;
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (context instanceof  SlidingGroupsActivity){
                if (fragment instanceof  Tab1){
                    if (!finalIsMember && !finalIsPending) {
                        ((Tab1) fragment).sendRequest(thisGroup.getGroupName());
                        image.setImageResource(R.drawable.pending);
                        membershipInfo.setText("запрос отправлен");
                        Toast.makeText(context, "Request sended", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return convertView;
    }
}

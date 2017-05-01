package kg.kloop.android.redbutton.groups;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erlan on 26.04.2017.
 */

public class GroupRoom {
    private String name;
    private int requiredAmountOfApprovals;
    Map <String, Boolean> moderators;
    Map <String, Boolean> members;



    public int getRequiredAmountOfApprovals() {
        return requiredAmountOfApprovals;
    }

    public void setRequiredAmountOfApprovals(int requiredAmountOfApprovals) {
        this.requiredAmountOfApprovals = requiredAmountOfApprovals;
    }


    public GroupRoom(){
        this("");

    }

    public GroupRoom(String name) {
        this.name = name;
        members = new HashMap<>();
        moderators = new HashMap<>();
        requiredAmountOfApprovals = 2;
    }

    public GroupRoom(String name, String groupCreator) {
        this.name = name;
        this.members = new HashMap<>();
        this.moderators = new HashMap<>();
        this.members.put(groupCreator, true);
        this.moderators.put(groupCreator, true);
        requiredAmountOfApprovals = 2;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

}

package kg.kloop.android.redbutton.groups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by erlan on 26.04.2017.
 */

public class GroupRoom {
    private String name;
    private String id;
    private int requiredAmountOfApprovals;
    Map <String, Boolean> moderators;

    public int getRequiredAmountOfApprovals() {
        return requiredAmountOfApprovals;
    }

    public void setRequiredAmountOfApprovals(int requiredAmountOfApprovals) {
        this.requiredAmountOfApprovals = requiredAmountOfApprovals;
    }

    private ArrayList<String> members;
    private ArrayList<String> requests;

    public ArrayList<String> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<String> requests) {
        this.requests = requests;
    }

    public GroupRoom(){
        this("");

    }

    public GroupRoom(String name) {
        this.name = name;
        members = new ArrayList<>();
        requests = new ArrayList<>();
        moderators = new HashMap<>();
        requiredAmountOfApprovals = 2;
    }

    public GroupRoom(String name, String moderator) {
        this.name = name;
        members = new ArrayList<>();
        requests = new ArrayList<>();
        this.moderators = new HashMap<>();
        this.moderators.put(moderator, true);
        requiredAmountOfApprovals = 2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setMembers(ArrayList<String> members){
        this.members = members;
    }

    public ArrayList<String> getMembers() {
        return members;
    }
}

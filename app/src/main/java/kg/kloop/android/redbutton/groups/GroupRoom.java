package kg.kloop.android.redbutton.groups;

import java.util.ArrayList;

/**
 * Created by erlan on 26.04.2017.
 */

public class GroupRoom {
    private String name;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private ArrayList<String> members;
    private ArrayList<String> requests;

    public GroupRoom(){
        this("");

    }

    public GroupRoom(String name) {
        this.name = name;
        members = new ArrayList<>();
        requests = new ArrayList<>();
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

package kg.kloop.android.redbutton.groups;

/**
 * Created by erlan on 28.04.2017.
 */

public class GroupMembership {
    private String groupName;
    private boolean isMember;
    private boolean isPending;

    public GroupMembership(){

    }

    public GroupMembership(String groupName, boolean isMember, boolean isPending){
        this.groupName = groupName;
        this.isMember = isMember;
        this.isPending = isPending;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }
}

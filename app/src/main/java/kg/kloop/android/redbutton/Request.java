package kg.kloop.android.redbutton;

/**
 * Created by erlan on 28.04.2017.
 */

public class Request {
    private int agreeCount;
    private String userId;

    public Request(){

    }

    public Request(String userId){
        this(0, userId);
    }

    public Request(int agreeCount, String userId) {
        this.agreeCount = agreeCount;
        this.userId = userId;
    }

    public int getAgreeCount() {
        return agreeCount;
    }

    public void setAgreeCount(int agreeCount) {
        this.agreeCount = agreeCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

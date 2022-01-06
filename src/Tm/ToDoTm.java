package Tm;

public class ToDoTm {
    private String id;
    private String Discription;
    private String User_ID;

    public ToDoTm() {
    }

    public ToDoTm(String id, String discription, String user_ID) {
        this.id = id;
        Discription = discription;
        User_ID = user_ID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;

    }

    @Override
    public String toString() {
        return Discription;
    }
}

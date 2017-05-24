package kz.ikar.openstrmap.classes;

/**
 * Created by User on 24.05.2017.
 */

public class Comment {
    private int id;
    private int ball;
    private String content;
    private Institute institute;

    public Comment(int id, int ball, String content, Institute institute) {
        this.id = id;
        this.ball = ball;
        this.content = content;
        this.institute = institute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBall() {
        return ball;
    }

    public void setBall(int ball) {
        this.ball = ball;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }
}

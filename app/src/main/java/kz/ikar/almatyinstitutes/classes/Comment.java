package kz.ikar.almatyinstitutes.classes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 24.05.2017.
 */

public class Comment {
    private int id;
    private int ball;
    private String content;
    private Date time;
    //private Institute institute;

    public Comment(int id, int ball, String content, Date time/*, Institute institute*/) {
        this.id = id;
        this.ball = ball;
        this.content = content;
        this.time = time;
        //this.institute = institute;
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

    /*public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }*/

    public Date getTime() {
        return time;
    }

    public String getTimeText() {
        String dayMonth = new SimpleDateFormat("d MMM").format(this.time),
               time = new SimpleDateFormat("H:mm").format(this.time),
               year = new SimpleDateFormat("yyyy").format(this.time);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar yesterday = Calendar.getInstance();
        yesterday.set(Calendar.DATE, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        yesterday.set(Calendar.MILLISECOND, 0);

        String result = "";
        if (this.time.after(today.getTime())) {
            result += "Сегодня в ";
        } else if (this.time.after(yesterday.getTime())) {
            result += "Вчера в ";
        } else {
            result += dayMonth;
            if (Calendar.YEAR != Integer.parseInt(year)) {
                result += " " + (Integer.parseInt(year)%100);
            }
        }
        return result + time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

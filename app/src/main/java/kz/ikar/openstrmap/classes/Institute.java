package kz.ikar.openstrmap.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24.05.2017.
 */
public class Institute {
    private int id;
    private String name;
    private String address;
    private String phone;
    private Point point;
    private String head;
    private Type type;
    private Category category;
    private boolean isGov;

    public Institute(){};

    public Institute(int id,
                     String name,
                     String address,
                     String phone,
                     Point point,
                     String head,
                     Type type,
                     Category category,
                     boolean isGov) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.point = point;
        this.head = head;
        this.type = type;
        this.category = category;
        this.isGov = isGov;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isGov() {
        return isGov;
    }

    public void setGov(boolean gov) {
        isGov = gov;
    }

    public static List<Institute> getFakeInstitutes() {
        List<Institute> institutes = new ArrayList<>();
        institutes.add(
                new Institute(
                        1,
                        "Общеобразовательная школа №14",
                        "ул. Усть - Каменогорская, 1",
                        "8-727-2-556-678",
                        new Point(43.260495, 76.885785),
                        "Мамбетеев Думан Алимбаевич",
                        null,
                        null,
                        true));
        institutes.add(
                new Institute(
                        1,
                        "Школа-гимназия №152",
                        "мкр. \"Улжан\", 1/81",
                        "87011269065",
                        new Point(43.300097, 76.872211),
                        "Абдикаримова.Р.С",
                        null,
                        null,
                        true));
        institutes.add(
                new Institute(
                        1,
                        "Общеобразовательная школа №69",
                        "ул. Габдуллина, 67",
                        "87272748464",
                        new Point(43.228584, 76.907861),
                        "Турекулова Сауле Каллиоловна",
                        null,
                        null,
                        true));
        institutes.add(
                new Institute(
                        1,
                        "Общеобразовательная школа №70",
                        "мкр. Казахфильм, д.15а",
                        "87272992402",
                        new Point(43.194074, 76.906260),
                        "Досаева Гульжан Досаевна",
                        null,
                        null,
                        true));
        institutes.add(
                new Institute(
                        1,
                        "Школа-гимназия №73",
                        "ул. Басенова, 14",
                        "87272748499",
                        new Point(43.221372, 76.893743),
                        "Бокишева Гаухар Маутхановна",
                        null,
                        null,
                        true));
        return institutes;
    }
}

package hb;

/**
 * Created by IntelliJ IDEA.
 * User: zhou_xiaodan
 * Date: 2004-4-26
 * Time: 11:06:44
 * To change this template use File | Settings | File Templates.
 */
public class Mouse {
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

    private int id;
    private String name;

    public Cat getEatenBy() {
        return eatenBy;
    }

    public void setEatenBy(Cat eatenBy) {
        this.eatenBy = eatenBy;
    }

    private Cat eatenBy;
}

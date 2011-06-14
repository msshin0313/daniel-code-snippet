package hb;

/**
 * Created by IntelliJ IDEA.
 * User: zhou_xiaodan
 * Date: 2004-4-25
 * Time: 18:38:29
 * To change this template use File | Settings | File Templates.
 */
public class WildCat extends Cat {
    public WildCat() {}

    public Boolean getAdopted() {
        return adopted;
    }

    public void setAdopted(Boolean adopted) {
        this.adopted = adopted;
    }

    private Boolean adopted;

}

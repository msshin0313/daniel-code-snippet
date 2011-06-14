package edu.umich.si.cms.pivots.lucene;

/**
 * The simple version of node object
 */
public class SimpleNode {
    private int nid;
    private String title;
    private String link;

    public SimpleNode(int nid, String title, String link) {
        this.nid = nid;
        this.title = title;
        this.link = link;
    }

    public int getNid() {
        return nid;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}

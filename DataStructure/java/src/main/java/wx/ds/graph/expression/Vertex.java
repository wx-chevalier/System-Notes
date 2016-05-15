package wx.ds.graph.expression;

/**
 * Created by apple on 16/5/15.
 */
class Vertex {
    public char label;        // label (e.g. 'A')
    public int id;
    public boolean wasVisited;

    /**
     * @param lab
     * @function 以标签方式构造一个节点
     */
    public Vertex(char lab)   // constructor
    {
        label = lab;
        wasVisited = false;
    }

    /**
     * @param id
     * @function 以编号初始化节点
     */
    public Vertex(int id)   // constructor
    {
        id = id;
        wasVisited = false;
    }

}  // end class Vertex
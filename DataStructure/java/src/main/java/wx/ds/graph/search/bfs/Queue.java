package wx.ds.graph.search.bfs;

/**
 * Created by apple on 16/5/15.
 */

/**
 * 自定义的队列结构体
 */
class Queue {
    private final int SIZE = 20;
    private int[] queArray;
    private int front;
    private int rear;

    // -------------------------------------------------------------
    public Queue()            // constructor
    {
        queArray = new int[SIZE];
        front = 0;
        rear = -1;
    }

    // -------------------------------------------------------------
    public void insert(int j) // put item at rear of queue
    {
        if (rear == SIZE - 1)
            rear = -1;
        queArray[++rear] = j;
    }

    // -------------------------------------------------------------
    public int remove()       // take item from front of queue
    {
        int temp = queArray[front++];
        if (front == SIZE)
            front = 0;
        return temp;
    }

    // -------------------------------------------------------------
    public boolean isEmpty()  // true if queue is empty
    {
        return (rear + 1 == front || (front + SIZE - 1 == rear));
    }
// -------------------------------------------------------------
}  // end class Queue
////////////////////////////////////////////////////////////////
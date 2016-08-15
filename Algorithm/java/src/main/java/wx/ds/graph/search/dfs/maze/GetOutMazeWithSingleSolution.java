package wx.ds.graph.search.dfs.maze;

import java.util.*;

/**
 * Created by apple on 16/8/12.
 */

/**
 * @function 迷宫问题,寻找所有可行解
 * @OJ http://www.nowcoder.com/practice/cf24906056f4488c9ddb132f317e03bc?tpId=37&tqId=21266&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 */
public class GetOutMazeWithSingleSolution {

    /**
     * @function 存放地图中每一个点的数据
     */
    static class Point {

        public int x;
        public int y;
        public int value;

        public Point(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }

    /**
     * @param n    迷宫宽
     * @param m    迷宫长
     * @param maze 迷宫的矩阵
     * @return
     * @function 使用深度搜索寻找迷宫中可能的路径
     */
    public static Stack<Point> dfs(int n, int m, int[][] maze, int dir[][]) {

        //用于存放深度遍历过程中的点信息
        Stack<Point> stack = new Stack<>();

        //存放节点是否访问过的信息
        //迷宫不存在重复访问的问题
        int[][] visited = new int[n][m];

        //将根节点压入栈中
        Point root = new Point(0, 0, 0);

        //将根节点压入栈中
        stack.push(root);

        while (true) {


            //判断是否走到了右下角或者栈是否为空
            if (stack.size() == 0 || isOut(n, m, stack.peek())) {

                //跳出循环
                break;
            }

            //弹出当前点
            Point currentPoint = stack.peek();

            //分别寻找该点的下一个点或者右边的点
            //判断是否寻找到下或者右可以走通
            boolean flag = false;

            //寻找某个可行的方向
            for (int i = 0; i < dir.length; i++) {

                int x_step = dir[i][0];

                int y_step = dir[i][1];

                if (currentPoint.x + x_step < n //判断是否向下可行
                        && currentPoint.y + y_step < m //判断是否向右可行
                        && maze[currentPoint.x + x_step][currentPoint.y + y_step] == 0 //判断下一个节点是否为可行节点
                        && visited[currentPoint.x + x_step][currentPoint.y + y_step] == 0 //判断下一个节点是否被访问过
                        ) {
                    //如果右边的节点可走
                    stack.push(new Point(currentPoint.x + x_step, currentPoint.y + y_step, 0));

                    visited[currentPoint.x + x_step][currentPoint.y + y_step] = 1;

                    flag = true;

                    //注意,在深度遍历中,当我们找到一个可行路径的时候就跳出
                    break;
                }


            }

            //判断是否找到可行的路,如果没有的话,则弹出当前节点
            if (!flag) {
                stack.pop();
            }

        }


        return stack;

    }

    /**
     * @param n
     * @param m
     * @param point
     * @return
     * @function 判断玩家是否已经走到了出口点
     */
    public static boolean isOut(int n, int m, Point point) {
        if (point.x == (n - 1) && point.y == (m - 1)) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String args[]) {

//        int[][] maze = new int[][]{
//
//                {0, 1, 0, 0, 0},
//                {0, 1, 0, 1, 0},
//                {0, 0, 0, 0, 0},
//                {0, 1, 1, 1, 0},
//                {0, 0, 0, 1, 0}
//        };

//        int[][] maze = new int[][]{
//                {0, 1, 0},
//                {0, 0, 0}
//        };

        int[][] dir = new int[][]{
                {0, 1},
                {1, 0}
        };


        //读取输入的数据
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {

            int n = scanner.nextInt();

            int m = scanner.nextInt();

            int[][] maze = new int[n][m];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    maze[i][j] = scanner.nextInt();
                }
            }

            Stack<Point> road = dfs(n, m, maze, dir);

            Stack<Point> roadReverse = new Stack<Point>();//将路径反过来，因为栈中输出的路径是反的

            while (road.size() > 0) {
                roadReverse.push(road.pop());
            }

            while (roadReverse.size() > 0) {

                Point point = roadReverse.pop();

                System.out.println("(" + point.x + "," + point.y + ")");

            }

        }


    }

}

package wx.ds.indexed.set;

/**
 * Created by apple on 16/8/14.
 */

import java.util.*;

/**
 * @function 开发一个简单错误记录功能小模块，能够记录出错的代码所在的文件名称和行号。
 * @OJ http://www.nowcoder.com/practice/67df1d7889cf4c529576383c2e647c48?tpId=49&tqId=29276&rp=4&ru=/ta/2016test&qru=/ta/2016test/question-ranking
 */
public class SimpleErrorRecorder {

    public static void process(String[] errorRecorders) {

        LinkedHashMap<String, Integer> hashMap = new LinkedHashMap<>();

        for (int i = 0; i < errorRecorders.length; i++) {

            //分割单行记录
            String[] errorRecordSplit = errorRecorders[i].split(" ");

            //获取文件路径名
            String path = errorRecordSplit[0];

            //获取行号
            String line = errorRecordSplit[1];

            String fileName;

            if (path.contains("\\")) {

                //获取文件名
                String[] segments = path.split("\\\\");

                fileName = segments[segments.length - 1];
            } else {
                fileName = path;
            }


            //判断是否超过16个字符
            if (fileName.length() > 16) {
                fileName = fileName.substring(fileName.length() - 17, fileName.length());
            }

            String key = fileName + " " + line;

            //判断HashMap中是否含有该Key
            if (hashMap.containsKey(key)) {
                hashMap.put(key, hashMap.get(key) + 1);
            } else {
                hashMap.put(key, 1);
            }


        }

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(hashMap.entrySet());


        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue() > o2.getValue() ? -1 : 1;
            }
        });

        for (int i = 0; i < entries.size(); i++) {

            System.out.print(entries.get(i).getKey() + " " + entries.get(i).getValue() + " ");

        }

    }


    public static void main(String args[]) {

        TreeMap<Integer,String> treeMap = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 0;
            }
        });

        treeMap.put(2,"1");

        treeMap.put(1,"3");

        treeMap.put(3,"2");

        System.out.println(treeMap);




        Scanner scanner = new Scanner(System.in);

        ArrayList<String> errorRecorders = new ArrayList<>();


        while (scanner.hasNext()) {

            String str = scanner.next();

            if (str.equals("q")) {
                break;
            }

            Integer line = scanner.nextInt();



            //获取文件待路径名
            errorRecorders.add(str + " " + line);


        }

        String[] errorRecordersStr = new String[errorRecorders.size()];

        process(errorRecorders.toArray(errorRecordersStr));

//        String[] testErrorRecorders = new String[]{
//
//                "E:\\V1R2\\product\\aedddddddddddddd.c 1325",
//                "E:\\V1R2\\product\\fpgadrivf.c 1325",
//                "E:\\V1R2\\product\\fpgadrive.c 1335",
//                "E:\\V1R2\\product\\fpgadrive.c 1325",
//                "E:\\V1R2\\product\\fpgadrive.c 1325"
//        };

//        process(testErrorRecorders);
    }
}

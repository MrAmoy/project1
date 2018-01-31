package cn.edu.ncist.model;

import cn.edu.ncist.bean.Corp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* 
 *
 * Author: WYT
*/
public class DataUtil {
    private static DataUtil instance = null;
    private Map<String, Integer> mTypes;
    private int mTypeCount;

    private int inputNumber;
    private int hiddenNumber;
    private int outputNumber;

    private DataUtil() {
        mTypes = new HashMap<String, Integer>();
        mTypeCount = 0;
    }

    public static synchronized DataUtil getInstance() {
        if (instance == null)
            instance = new DataUtil();
        return instance;

    }

    public Map<String, Integer> getTypeMap() {
        return mTypes;
    }

    public int getTypeCount() {
        return mTypeCount;
    }

    public String getTypeName(int type) {
        if (type == -1)
            return new String("无法判断");
        Iterator<String> keys = mTypes.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            if (mTypes.get(key) == type)
                return key;
        }
        return null;
    }

    public int getInputNumber() {
        return inputNumber;
    }

    public int getHiddenNumber() {
        return hiddenNumber;
    }

    public int getOutputNumber() {
        return outputNumber;
    }

    public List getInputHiddenWeight(String fileName, String sep) throws Exception {

        List<Float> lineData = null;
        List mData = new ArrayList();

        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        String line = null;

        int inputNumber = 0;
        int hiddenNumber = 0;

        while ((line = br.readLine()) != null) {
            String splits[] = line.split(sep);

            if (hiddenNumber == 0) {
                hiddenNumber = splits.length;
            }

            lineData = new ArrayList<Float>();
            if (splits.length > 0) {
                for (int i = 0; i < splits.length; i++) {
                    try {
                        lineData.add(Float.valueOf(splits[i]));
                    } catch (NumberFormatException e) {
                        // 非数字，报错
                        System.out.println("getInputHiddenWeight数字格式错误");
                    }
                }
                mData.add(lineData);
                inputNumber++;
            }
        }

        this.inputNumber = inputNumber;
        this.hiddenNumber = hiddenNumber;

        return mData;
    }

    public List getHiddenOutputWeight(String fileName, String sep) throws Exception {

        List<Float> lineData = null;
        List mData = new ArrayList();

        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        String line = null;

        int hiddenNumber = 0;
        int outputNumber = 0;

        while ((line = br.readLine()) != null) {
            String splits[] = line.split(sep);

            if (outputNumber == 0) {
                outputNumber = splits.length;
            }

            lineData = new ArrayList<Float>();
            if (splits.length > 0) {
                for (int i = 0; i < splits.length; i++) {
                    try {
                        lineData.add(Float.valueOf(splits[i]));
                    } catch (NumberFormatException e) {
                        // 非数字，报错
                        System.out.println("getHiddenOutputWeight数字格式错误");
                    }
                }
                mData.add(lineData);
                hiddenNumber++;
            }
        }

        this.outputNumber = outputNumber;
        if (hiddenNumber != getHiddenNumber()) {
            System.out.println("Matrix lenght error!");
        }

        return mData;
    }


    /*
     * generate train data
     * column 1 is total of hidden trouble
     * column 2 is number of yibanweizhenggai
     * column 3 is number of
     * xianqizhenggai
     * column 4 is number of xianchangzhanggai
     * column 5 is number of zhongdaweizhenggai
     * column 6 is number of zhongdayizhenggain
     * column 7 is result:-1--not; 0--proposal;1--training
     *
     * @param fileName
     * @param sep
     * @return
     * @throws Exception
     */
    public List<DataNode> getDataList(Corp corp)
            throws Exception {
        List<DataNode> list = new ArrayList<DataNode>();

        DataNode node = new DataNode();
        String a1 = String.valueOf(corp.getYearcheckcount());
        String a2 = String.valueOf(corp.getYearybwzg());
        String a3 = String.valueOf(corp.getYearybxqzg());
        String a4 = String.valueOf(corp.getYearybxczg());
        String a5 = String.valueOf(corp.getYearzdyh());
        String a6 = String.valueOf(corp.getAjjcyh());
        String[] splits = {a1, a2, a3, a4, a5, a6};

        for (int i = 0; i < splits.length; i++) {
            try {
                double para = 0.9;
                if (i == 1) {
                    para = 0.7;
                } else if (i == 2) {
                    para = 0.8;
                } else if (i == 3) {
                    para = 0.7;
                } else if (i == 4) {
                    para = 0.9;
                } else if (i == 5) {
                    para = 0.8;
                }
                node.addAttrib(Normalization.getResult(para, Integer.valueOf(splits[i])));
            } catch (NumberFormatException e) {
                // 非数字，报错
                System.out.println("数字格式错误");
            }
        }
        list.add(node);
        return list;
    }
    /*public List<DataNode> getDataList(String fileName, String sep)
            throws Exception {
        List<DataNode> list = new ArrayList<DataNode>();
        BufferedReader br = new BufferedReader(new FileReader(
                new File(fileName)));
        String line = null;
        while ((line = br.readLine()) != null) {
            String splits[] = line.split(sep);
            DataNode node = new DataNode();

            //训练文件数据列数
            int colNum = 7;

            if (splits.length == colNum) {
                node.addAttrib(Normalization.getValue(Integer.valueOf(splits[0])));

                for (int i = 1; i < splits.length - 1; i++) {
                    try {
                        double para = 0.9;
                        if (i == 1) {
                            para = 0.7;
                        } else if (i == 2) {
                            para = 0.8;
                        } else if (i == 3) {
                            para = 0.7;
                        } else if (i == 4) {
                            para = 0.9;
                        } else if (i == 5) {
                            para = 0.8;
                        }
                        node.addAttrib(Normalization.getResult(para, Integer.valueOf(splits[i])));
                    } catch (NumberFormatException e) {
                        // 非数字，报错
                        System.out.println("数字格式错误");
                    }
                }

                int result = Integer.valueOf(splits[6]);
                node.addResult(result);
            }

            list.add(node);
        }
        return list;
    }*/

    public void display(List<DataNode> lst) {

        for (int index = 0; index < lst.size(); index++) {
            List<Float> mAttribList = lst.get(index).getAttribList();
            for (int i = 0; i < mAttribList.size(); i++) {
                System.out.print(mAttribList.get(i).toString() + ",   "
                );
            }

            List<Integer> resultList = lst.get(index).getResultList();
            for (int i = 0; i < resultList.size(); i++) {
                Integer tmp = (Integer) resultList.get(i);
                System.out.print("i=" + tmp.toString() + "===");
            }

            System.out.println();
        }
    }
}

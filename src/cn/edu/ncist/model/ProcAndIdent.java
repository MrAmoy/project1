package cn.edu.ncist.model;

import cn.edu.ncist.bean.Corp;
import cn.edu.ncist.bean.Parameter;
import java.util.List;

public class ProcAndIdent {

    private static Parameter param = new Parameter();
    public static int needResultNum = param.getTheEndNum();

    private static DataUtil util;
    private static AnnClassifier annClassifier;
    //类初始化块
    static {
        util = DataUtil.getInstance();
        try {
            List ihm = util.getInputHiddenWeight("src/cn/edu/ncist/conf/ih.txt", ",");
            List hom = util.getHiddenOutputWeight("src/cn/edu/ncist/conf/ho.txt", ",");
            annClassifier = new AnnClassifier(util.getInputNumber(),
                    util.getHiddenNumber(), util.getOutputNumber());

            annClassifier.reset();

            annClassifier.setInputHiddenWeight(ihm);
            annClassifier.setHiddenOutputWeight(hom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //方法：把放入的企业走模型，得出结果
    public static int ProcAndIdent(Corp corp) throws Exception {

        List<DataNode> testList = util.getDataList(corp);
        //给type随便取一个除了1，0，-1的值，防止type值空指针
        int type = 2;
        for (int i = 0; i < testList.size(); i++) {
            DataNode test = testList.get(i);
            type = annClassifier.test(test);
        }
        return type;
    }
}

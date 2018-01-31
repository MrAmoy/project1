package cn.edu.ncist.model;//AnnClassifier.java
//package com.jingchen.ann;

import java.util.ArrayList;
import java.util.List;

public class AnnClassifier
{
    private int mInputCount;
    private int mHiddenCount;
    private int mOutputCount;

    private List<NetworkNode> mInputNodes;
    private List<NetworkNode> mHiddenNodes;
    private List<NetworkNode> mOutputNodes;

    private float[][] mInputHiddenWeight;
    private float[][] mHiddenOutputWeight;

    private List<DataNode> trainNodes;

    public void setTrainNodes(List<DataNode> trainNodes)
    {
        this.trainNodes = trainNodes;
    }

    public void setInputHiddenWeight(List ihm){
        for(int i=0; i<ihm.size(); i++){
            List<Float> lineData = (List)ihm.get(i);
            for(int j=0; j<lineData.size(); j++){
                mInputHiddenWeight[i][j] = (Float)lineData.get(j);
            }
        }
    }

    public void setHiddenOutputWeight(List hom){
        for(int i=0; i<hom.size(); i++){
            List<Float> lineData = (List)hom.get(i);
            for(int j=0; j<lineData.size(); j++){
                mHiddenOutputWeight[i][j] = (Float)lineData.get(j);
            }
        }
    }

    public AnnClassifier(int inputCount, int hiddenCount, int outputCount)
    {
        trainNodes = new ArrayList<DataNode>();
        mInputCount = inputCount;
        mHiddenCount = hiddenCount;
        mOutputCount = outputCount;
        mInputNodes = new ArrayList<NetworkNode>();
        mHiddenNodes = new ArrayList<NetworkNode>();
        mOutputNodes = new ArrayList<NetworkNode>();
        mInputHiddenWeight = new float[inputCount][hiddenCount];
        mHiddenOutputWeight = new float[mHiddenCount][mOutputCount];
    }

    /**
     * 更新权重，每个权重的梯度都等于与其相连的前一层节点的输出乘以与其相连的后一层的反向传播的输出
     */
    private void updateWeights(float eta)
    {
        //更新输入层到隐层的权重矩阵
        for (int i = 0; i < mInputCount; i++)
            for (int j = 0; j < mHiddenCount; j++)
                mInputHiddenWeight[i][j] -= eta
                        * mInputNodes.get(i).getForwardOutputValue()
                        * mHiddenNodes.get(j).getBackwardOutputValue();
        //更新隐层到输出层的权重矩阵
        for (int i = 0; i < mHiddenCount; i++)
            for (int j = 0; j < mOutputCount; j++)
                mHiddenOutputWeight[i][j] -= eta
                        * mHiddenNodes.get(i).getForwardOutputValue()
                        * mOutputNodes.get(j).getBackwardOutputValue();
    }


    /**
     * 前向传播
     * 参数list是每个输入数据节点的4个数据
     */
    private void forward(List<Float> lst)
    {
        // 输入层
        //给神经网络输入层的每个节点赋值，使用参数的节点来赋值
        //同时，对应计算输入层每个节点的输出值，使用函数forwardSigmoid
        for (int k = 0; k < lst.size(); k++){
            mInputNodes.get(k).setForwardInputValue(lst.get(k));
        }
        // 隐层
        //计算隐层输入向量，输入层输出，乘上输入隐层权重矩阵
        //使用函数forwardSigmoid，计算隐层输出量
        for (int j = 0; j < mHiddenCount; j++)
        {
            float temp = 0;
            for (int k = 0; k < mInputCount; k++)
                temp += mInputHiddenWeight[k][j]
                        * mInputNodes.get(k).getForwardOutputValue();
            mHiddenNodes.get(j).setForwardInputValue(temp);
        }
        // 输出层
        //同上，使用隐层输出矩阵计算输出层输入，
        //使用函数forwardSigmoid计算输出
        for (int j = 0; j < mOutputCount; j++)
        {
            float temp = 0;
            for (int k = 0; k < mHiddenCount; k++)
                temp += mHiddenOutputWeight[k][j]
                        * mHiddenNodes.get(k).getForwardOutputValue();
            mOutputNodes.get(j).setForwardInputValue(temp);
        }
    }


    /**
     * 初始化
     */
    public void reset()
    {
        mInputNodes.clear();
        mHiddenNodes.clear();
        mOutputNodes.clear();
        for (int i = 0; i < mInputCount; i++)
            mInputNodes.add(new NetworkNode(NetworkNode.TYPE_INPUT));
        for (int i = 0; i < mHiddenCount; i++)
            mHiddenNodes.add(new NetworkNode(NetworkNode.TYPE_HIDDEN));
        for (int i = 0; i < mOutputCount; i++)
            mOutputNodes.add(new NetworkNode(NetworkNode.TYPE_OUTPUT));
        for (int i = 0; i < mInputCount; i++)
            for (int j = 0; j < mHiddenCount; j++)
                mInputHiddenWeight[i][j] = (float) (Math.random() * 0.1);
        for (int i = 0; i < mHiddenCount; i++)
            for (int j = 0; j < mOutputCount; j++)
                mHiddenOutputWeight[i][j] = (float) (Math.random() * 0.1);
    }

    public int test(DataNode dn)
    {
        List<Float> ldn = dn.getAttribList();

        forward(dn.getAttribList());
        float result = 2;
        int type = 0;

        //取最接近1的
        for (int i = 0; i < mOutputCount; i++){
            if ((1 - mOutputNodes.get(i).getForwardOutputValue()) < result)
            {
                result = 1 - mOutputNodes.get(i).getForwardOutputValue();
                type = i;
            }
        }

        return 1- type;
    }
}

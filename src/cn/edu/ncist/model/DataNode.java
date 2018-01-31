package cn.edu.ncist.model;

import java.util.ArrayList;
import java.util.List;
/*
 *功能：节点的数据，保存到List中
 *
 *Author: WYT
*/

public class DataNode
{
	private List<Float> mAttribList;
	private List<Integer> resultList;

	public List<Integer> getResultList()
	{
		return resultList;
	}

	public void setResultList(List<Integer> resultList)
	{
		this.resultList = resultList;
	}

	public List<Float> getAttribList()
	{
		return mAttribList;
	}

	public void addAttrib(Float value)
	{
		mAttribList.add(value);
	}

	public void addResult(Integer result)
	{
		if( 1== result){
			resultList.add(1);
			resultList.add(-1);
			resultList.add(-1);
		}
		else if(0 == result){
			resultList.add(-1);
			resultList.add(1);
			resultList.add(-1);
		}
		else{
			resultList.add(-1);
			resultList.add(-1);
			resultList.add(1);
		}
	}

	public DataNode()
	{
		mAttribList = new ArrayList<Float>();
		resultList = new ArrayList<Integer>();
	}

}

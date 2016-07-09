package com.sheepyang.mobilesafe.entity;

public class BlackNumInfo {
	/**
	 * 黑名单号码
	 */
	private String blacknum;
	/**
	 * 拦截模式
	 */
	private int mode;
	public String getBlacknum() {
		return blacknum;
	}
	public void setBlacknum(String blacknum) {
		this.blacknum = blacknum;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		//0,1,2
		//对mode进行判断,在这里判断,之后在调用这个方法的时候就不用再去对mode进行判断
		if (mode >=0 && mode <= 2) {
			this.mode = mode;
		}else{
			this.mode = 0;
		}
	}
	public BlackNumInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	//方便添加数据用
	public BlackNumInfo(String blacknum, int mode) {
		super();
		this.blacknum = blacknum;
		if (mode >=0 && mode <= 2) {
			this.mode = mode;
		}else{
			this.mode = 0;
		}
	}
	//方便我们输出测试数据
	@Override
	public String toString() {
		return "BlackNumInfo [blacknum=" + blacknum + ", mode=" + mode + "]";
	}
	
}

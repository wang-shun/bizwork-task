package com.sogou.bizwork.task.api.core.score.bo;

/**
 * 积分状态：0:待领取、1:已领取待给分人确认、2:给分人同意给分、3:给分人拒绝给分、-1:删除
 * @author linxionghui
 *
 */
public interface ScoreStatus {    
	public final int WAIT_OBTAIN = 0;  
	public final int WAIT_CONFIRM = 1;
	public final int AGREE_SCORE = 2;
	public final int DISAGREE_SCORE = 3;
	public final int DELETED = -1;
}

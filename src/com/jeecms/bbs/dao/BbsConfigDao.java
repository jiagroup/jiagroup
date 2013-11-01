package com.jeecms.bbs.dao;

import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.core.entity.CmsSite;

public interface BbsConfigDao {
	/**
	 * 清理当日数据
	 */
	public void clearTodayData();
	
	public BbsConfig findById(Integer id);
	
	public BbsConfig findBySite(CmsSite sit);

	public BbsConfig save(BbsConfig bean);

	public BbsConfig updateByUpdater(Updater<BbsConfig> updater);

	public BbsConfig deleteById(Integer id);
}
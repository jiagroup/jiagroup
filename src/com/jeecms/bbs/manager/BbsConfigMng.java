package com.jeecms.bbs.manager;

import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.core.entity.CmsSite;

public interface BbsConfigMng {
	public BbsConfig findById(Integer id);

	public BbsConfig findBySite(CmsSite site);

	public BbsConfig updateConfigForDay(Integer siteId);

	public BbsConfig update(BbsConfig bean);
}
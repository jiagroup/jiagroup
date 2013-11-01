package com.jeecms.bbs.dao;


import java.util.List;

import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsMemberMagic;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsMemberMagicDao {
	
	public List<BbsCommonMagic> getUserMagicList(Integer userId);

	public Pagination getPage(Integer userId, int pageNo, int pageSize);

	public BbsMemberMagic findById(Integer id);

	public BbsMemberMagic save(BbsMemberMagic bean);

	public BbsMemberMagic updateByUpdater(Updater<BbsMemberMagic> bean);

	public BbsMemberMagic deleteById(Integer id);

}
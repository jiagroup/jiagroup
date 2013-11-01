package com.jeecms.bbs.manager;

import java.util.List;

import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsMemberMagic;
import com.jeecms.common.page.Pagination;

public interface BbsMemberMagicMng {

	public Pagination getPage(Integer userId,int pageNo, int pageSize);

	public BbsMemberMagic findById(Integer id);

	public BbsMemberMagic save(BbsMemberMagic bean);

	public BbsMemberMagic update(BbsMemberMagic bean);

	public BbsMemberMagic deleteById(Integer id);

	public BbsMemberMagic[] deleteByIds(Integer[] ids);
	
	public List<BbsCommonMagic> getUserMagicList(Integer userId);
}
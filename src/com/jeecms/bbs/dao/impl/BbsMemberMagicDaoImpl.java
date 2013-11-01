package com.jeecms.bbs.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsMemberMagicDao;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsMemberMagic;
import com.jeecms.common.hibernate3.Finder;
import com.jeecms.common.hibernate3.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsMemberMagicDaoImpl extends
		HibernateBaseDao<BbsMemberMagic, Integer> implements BbsMemberMagicDao {
	
	@Override
	public List<BbsCommonMagic> getUserMagicList(Integer userId) {
		List<BbsCommonMagic> list = new ArrayList<BbsCommonMagic>();
		String hql = "SELECT magic.name, magic.description, magic.num sumNum, magic.price, magic.weight, magic.magicId, member.num, member.uid, member.id FROM bbs_common_magic magic LEFT JOIN bbs_member_magic member ON magic.magicid = member.magicid and member.uid = ?";
		PreparedStatement ps = null;
		try {
			ps = sessionFactory.getCurrentSession().connection().prepareStatement(hql);
			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();
			if(rs != null){
				while(rs.next()){
					BbsCommonMagic magic = new BbsCommonMagic();
					magic.setName(rs.getString("name"));
					magic.setDescription(rs.getString("description"));
					magic.setId(rs.getInt("id"));
					magic.setUsenum(rs.getInt("num"));
					magic.setPrice(rs.getInt("price"));
					magic.setWeight(rs.getInt("weight"));
					magic.setNum(rs.getInt("sumNum"));
					magic.setIdentifier(rs.getString("magicId"));
					
					list.add(magic);
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
		
	}
	
	public Pagination getPage(Integer userId, int pageNo, int pageSize) {
		String hql = "from BbsMemberMagic magic ";
		Finder finder = Finder.create(hql);
		Pagination page = find(finder, pageNo, pageSize);
		return page;
	}

	public BbsMemberMagic findById(Integer id) {
		BbsMemberMagic entity = get(id);
		return entity;
	}

	public BbsMemberMagic save(BbsMemberMagic bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsMemberMagic deleteById(Integer id) {
		BbsMemberMagic entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	protected Class<BbsMemberMagic> getEntityClass() {
		return BbsMemberMagic.class;
	}
}
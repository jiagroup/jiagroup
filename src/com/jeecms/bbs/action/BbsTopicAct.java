package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsCategory;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.manager.BbsCategoryMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;

@Controller
public class BbsTopicAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsTopicAct.class);

	@RequestMapping("/topic/v_list.do")
	public String list(HttpServletRequest request, ModelMap model, Integer forumId, Integer categoryId, Integer pageNo, Integer pageSize, String keyword) {
		List<BbsCategory>  categoryList = bbsCategoryMng.getList(1);
		Pagination pagination = null;
		if(categoryId == null){
			pagination = bbsForumMng.getPage(cpn(null), CookieUtils.getPageSize(request));
			model.addAttribute("forumList", pagination.getList());
		}
		else{
			model.addAttribute("forumList", bbsForumMng.getList(1, categoryId));
		}
		Pagination paginatio = bbsTopicMng.getListByForumIdAndKeyword(forumId, pageNo, pageSize, keyword);

		model.put("pagination", paginatio);
		model.put("forumId", forumId);
		model.put("categoryId", categoryId);
		model.put("categoryList", categoryList);
		model.put("keyword", keyword);
		return "topic/listForAdmin";
	}

	@RequestMapping("/topic/v_forumList.do")
	public String add(HttpServletRequest request, ModelMap model, Integer categoryId) {
		Pagination pagination = null;
		if(categoryId == null){
			pagination = bbsForumMng.getPage(cpn(null), CookieUtils.getPageSize(request));
			model.addAttribute("forumList", pagination.getList());
		}
		else{
			model.addAttribute("forumList", bbsForumMng.getList(1, categoryId));
		}
		return "topic/forumList";
	}

	@RequestMapping("/topic/o_delete.do")
	public String delete(Integer[] ids, HttpServletRequest request, ModelMap model) {
		bbsTopicMng.deleteByIds(ids);
		
		return "msg";
	}
	
	@RequestMapping("/topic/o_update.do")
	public String update(Integer[] ids, Integer targetForumId, HttpServletRequest request, ModelMap model) {
		BbsForum forum = bbsForumMng.findById(targetForumId);
		for (Integer id : ids) {
			BbsTopic topic = bbsTopicMng.findById(id);
			topic.setForum(forum);
			bbsTopicMng.update(topic);
		}
		
		return "msg";
	}

	@Autowired
	private BbsForumMng bbsForumMng;
	@Autowired
	private BbsCategoryMng bbsCategoryMng;
	@Autowired
	private BbsTopicMng bbsTopicMng;
}

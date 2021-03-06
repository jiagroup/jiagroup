package com.jeecms.bbs.action.directive;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_LIST;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.core.entity.CmsSite;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ForumDirective implements TemplateDirectiveModel {

	public static final String CATEGORY_ID = "categoryId";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSite site = FrontUtils.getSite(env);
		Integer categoryId = DirectiveUtils.getInt(CATEGORY_ID, params);
		List forumList = manager.getList(site.getId(), categoryId);
		
		//最新主题、最新回复、今日热门话题
		List<BbsTopic> newPosts = topicManage.getNewList(8);
		List<BbsTopic> latestReplyPosts = (List<BbsTopic>) topicManage.getTopicByTime(1, 1, 8).getList();
		List<BbsTopic> todayHotPosts = topicManage.getTodayHotPost(8);
		
		
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_LIST, DEFAULT_WRAPPER.wrap(forumList));
		paramWrap.put("newPosts", DEFAULT_WRAPPER.wrap(newPosts));
		paramWrap.put("latestReplyPosts", DEFAULT_WRAPPER.wrap(latestReplyPosts));
		paramWrap.put("todayHotPosts", DEFAULT_WRAPPER.wrap(todayHotPosts));
		
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private BbsTopicMng topicManage;
	
	@Autowired
	private BbsForumMng manager;
//	@Autowired
//	private BbsTopicMng topicMng;
}

package com.jeecms.bbs.schedule;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.schedule.pojo.TargetPJ;

public class FetchTopicJob {

	public void executeByDay(){
		
		Document doc;
		try {
			for (int i = 4; i < TargetPJ.targetUrl.length; i++) {
				
				String targetUrl = TargetPJ.targetUrl[i];
				doc = Jsoup.connect(targetUrl).userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)") 
		          .timeout(10000).get();
				
				Elements elements = doc.getElementsByClass(TargetPJ.hrefs[i]).get(0).getElementsByTag("a");
				if(!elements.isEmpty()){
					Iterator<Element> it = elements.iterator();
					while(it.hasNext()){
						try{
							Element element = it.next();
							
							String fetchUrl = element.attr("href");
							if(!element.attr("href").contains("http://")){
								fetchUrl = targetUrl.substring(0, targetUrl.lastIndexOf("/")) + "/" + element.attr("href");
							}
							Document topicDoc = Jsoup.connect(fetchUrl).userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)") 
					          .timeout(10000).get();
							
							BbsTopic bean = null;
							switch(TargetPJ.typeIds[i]){
							case(1):
								if(topicDoc.getElementsByClass("name").get(0).text().equals("论坛公告")){
									continue;
								}
								
								bean = topicMng.postTopic(TargetPJ.userId, TargetPJ.siteId, TargetPJ.forumIds[i], TargetPJ.postTypeIds[i], topicDoc.getElementsByTag("h2").text().substring(topicDoc.getElementsByTag("h2").text().indexOf("]")+1, topicDoc.getElementsByTag("h2").text().indexOf(" 只看楼主")), 
										replaceImgTag(topicDoc.getElementsByClass("message").get(0).html(), targetUrl.substring(0, targetUrl.lastIndexOf("/")) + "/" ), TargetPJ.ip, TargetPJ.categoryIds[i], null, null, null);
								
								break;
								
							case(2):
								if(topicDoc.getElementsByClass("breadcrumb") == null || topicDoc.getElementsByClass("breadcrumb").size() <= 0){
									continue;
								}
							
								bean = topicMng.postTopic(TargetPJ.userId, TargetPJ.siteId, TargetPJ.forumIds[i], TargetPJ.postTypeIds[i], topicDoc.getElementsByTag("title").get(0).text().substring(0, topicDoc.getElementsByTag("title").get(0).text().indexOf("-")), 
										replaceImgTag(topicDoc.getElementsByClass("itcrcomment").get(0).html(), targetUrl.substring(0, targetUrl.lastIndexOf("/")) + "/" ), TargetPJ.ip, TargetPJ.categoryIds[i], null, null, null);
								
								break;
							}
							
							log.info(bean.toString());
						}
						catch(Exception e){
							//e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
		}
	}
	
	private String replaceImgTag(String message, String targetUrl){
		/*message = "<H2>52论坛上转过来的</H2><h1>"+
		"<br />"+ 
		"<a href=\"forum.php?mod=viewthread&tid=289024&aid=724188&from=album&page=1&mobile=2\"/></a><lsdklsd><br/>"+
		"<a href=\"forum.php?mod=viewthread&tid=289024&aid=724188&from=album&page=1&mobile=2\" class=\"orange\"><br/>" +
		"<img id=\"aimg_724188\" src=\"forum.php?mod=image&aid=724188&size=140x140&key=4d7a12e1f47e23df&type=fixnone\" alt=\"171340do40ao5vv0o0yuqo.jpg\" title=\"171340do40ao5vv0o0yuqo.jpg\" /></a>";
		*/		
		 Pattern pattern = Pattern.compile("(<\\s*img\\s*[^>]*\\s*/>)");
		 Matcher matcher = pattern.matcher(message); 
		 while (matcher.find()) {
			 String temp = matcher.group(1);
			 if(!matcher.group(1).contains("http://")){
				 message = message.replace(matcher.group(1), matcher.group(1).replaceAll("<img\\s*([\\w\\W]*)src\\s*=\\s*\"", "[img]" + targetUrl).substring(0, matcher.group(1).replaceAll("<img\\s*([\\w\\W]*)src\\s*=\\s*\"", "[img]").indexOf("\""))+"[/img]");
			 }
			 else{
				 message = message.replace(matcher.group(1), matcher.group(1).replaceAll("<img\\s*([\\w\\W]*)src\\s*=\\s*\"", "[img]").substring(0, matcher.group(1).replaceAll("<img\\s*([\\w\\W]*)src\\s*=\\s*\"", "[img]").indexOf("\""))+"[/img]");
			 }
		 }
		 
		return message.replaceAll("</a>", "[/url]").replaceAll("<br\\s*/>", "\r\n").replaceAll("<a\\s*href\\s*=\\s*\"", "[url=").replaceAll("\"\\s*[^>]*>", "]").replaceAll("<[^>]*>", "");
	}
	
	/*public void excute(){
	    DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();  
	    WebDriver driver = new HtmlUnitDriver(capabilities)  
	    {  
	        @Override  
	        protected WebClient modifyWebClient(WebClient client)  
	        {  
	            DefaultCredentialsProvider creds = new DefaultCredentialsProvider();  
	            creds.addCredentials("用户名", "密码");  
	            client.setCredentialsProvider(creds);  
	            return client;  
	        }  
	    };  
	    driver.get("http://www.baidu.com");  
	    System.out.println("[" + driver.getTitle() + "]");  
	}*/
	
	
	
	public static void main(String[] args){
		 // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
       /* WebDriver driver = new FirefoxDriver();

        // And now use this to visit Google
        driver.get("http://www.google.com");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.name("q"));

        // Enter something to search for
        element.sendKeys("Cheese!");

        // Now submit the form. WebDriver will find the form for us from the element
        element.submit();

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());
       
        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("cheese!");
            }
        });

        // Should see: "cheese! - Google Search"
        System.out.println("Page title is: " + driver.getTitle());
       
        //Close the browser
        driver.quit();*/
	}
	
	@Autowired
	private BbsTopicMng topicMng;
	
	private static final Logger log = LoggerFactory.getLogger(FetchTopicJob.class);
}

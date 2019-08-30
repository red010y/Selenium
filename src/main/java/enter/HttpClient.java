package enter;

import MysqlConnection.MysqlDAOFactory;
import MysqlConnection.MysqlHelper;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class HttpClient {//还有个麻烦事：我的homepage要写year month day的正则
    public static String newsPage_link;
    //二次跳转直接输入二次跳转的页面就行
    public static String[] names = {"jiang"};
    public static String[] uri = {"http://www.cn0917.com/portal.php"};
    public static String[] homePage_regex = {"http://www.cn0917.com/portal.php\\?mod=view&aid=\\d+"};
    public static String[] home = {"http://www.cn0917.com/portal.php\\\\?mod=view&aid=\\\\d+"};
    public static String[] title_selector = {".h.hm > h1"};
    public static String[] content_selector = {"#article_content"};

    public static void main(String[] args) throws IOException, URISyntaxException {
        MysqlHelper mysqlHelper=new MysqlHelper();
        String homePage = sendGet(uri[0]);
        Document doc_homePage = Jsoup.parse(homePage);
        Elements links = doc_homePage.select("a");
        boolean have_newsPage = checkUrl(links, homePage_regex[0]);
        if (have_newsPage) {
            String newsPage = sendGet(newsPage_link);
            Document doc_newsPage = Jsoup.parse(newsPage);
            String have_title = doc_newsPage.select(title_selector[0]).toString();
            if (have_title != "" && have_title != null) {
                String have_content = doc_newsPage.select(content_selector[0]).toString();
                if (have_content != "" && have_content != null) {
                    //INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
                    //'百度','https://www.baidu.com/','4','CN'
                    mysqlHelper.executeNonQuery("insert into frm_23133 " +
                            "(name,fromType,syncSys,linkUrl,cycle,status,site_type,role_type,hub_role,detailTitle,detailDate,beforeDate,detailFrom,detailContent) " +
                            "values ('"+names[0]+"','稿源','1','"+uri[0]+"','30','4','html','css','"+home[0]+"','"+title_selector[0]+"','auto','2','auto','"+content_selector[0]+"')");
                }
            }
        }

//        System.out.println(jiang);
    }

    public static boolean checkUrl(Elements links, String regex) {
        for (Element link : links) {
            if (Pattern.matches(regex, link.attr("abs:href"))) {
                newsPage_link = link.attr("abs:href");
                return true;
            }
        }
        return false;
    }

    public static String sendGet(String uri) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)");
        // 响应模型
        CloseableHttpResponse response = null;
        String content = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                content = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
                return content;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "无法访问网页";
    }
}

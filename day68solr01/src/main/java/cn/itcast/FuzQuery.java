package cn.itcast;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class FuzQuery {
    /**
     * 需求:查询索引库,复杂查询
     */
    @Test
    public void fuzQuery() throws SolrServerException {
        String url = "http://localhost:9080/solr/item";
        SolrServer solrServer = new HttpSolrServer(url);

        SolrQuery solrQuery = new SolrQuery();

        //封装查询条件
        //1.设置简单查询条件
        //solrQuery.set("q","*:*");
        solrQuery.setQuery("家天下");

        //2.fq,过滤查询
        //1) 过滤查询, 过滤商品分类  时尚卫浴
        solrQuery.addFilterQuery("product_catalog_name:时尚卫浴");

        //2) 过滤商品价格在20以下的
        solrQuery.addFilterQuery("product_price:[0 TO 20]");

        //3) 排序查询
        solrQuery.setSort("product_price", SolrQuery.ORDER.desc);

        //4) 分页查询 start rows
        solrQuery.setStart(0);
        solrQuery.setStart(10);

        //5) 字段过滤映射查询
        //solrQuery.setFields("product_name product_price");

        //6) 设置默认查询域字段
        solrQuery.set("df","product_keywords");

        //7) 高亮设置
        //第一步:开启高亮
        solrQuery.setHighlight(true);
        //第二步:添加高亮字段
        solrQuery.addHighlightField("product_name");
        //第三步:设置高亮前后缀
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        //使用solr服务对象查询索引库
        QueryResponse response = solrServer.query(solrQuery);
        //得到命中的文档集合
        SolrDocumentList results = response.getResults();
        System.out.println("命中总记录数:"+results.getNumFound());

        //获取文档对象数据
        for (SolrDocument sdoc : results) {
            //根据域字段名称，获取域字段的值
            String id = (String) sdoc.get("id");
            System.out.println("文档域字段："+id);

            String product_name = (String) sdoc.get("product_name");

            //获取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //第一个map的key是id
            Map<String, List<String>> stringListMap = highlighting.get(id);
            //第二个map的key是高亮字段名称
            List<String> list = stringListMap.get("product_name");

            //判断高亮是否存在
            if(list!=null && list.size()>0) {
                product_name =    list.get(0);
            }

            System.out.println("商品名称："+product_name);

            Float product_price = (Float) sdoc.get("product_price");
            System.out.println("商品价格："+product_price);

            String product_description = (String) sdoc.get("product_description");
            System.out.println("商品描述："+product_description);

            String product_picture = (String) sdoc.get("product_picture");
            System.out.println("商品图片："+product_picture);


            String product_catalog_name = (String) sdoc.get("product_catalog_name");
            System.out.println("商品分类："+product_catalog_name);

        }


    }
}

package cn.itcast;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class SimpleQuery {

    @Test
    public void simpleQuery() throws SolrServerException {
        //指定solr远程服务器连接地址
        //如果索引仓库的名字没改,还是collection1,则不需要指定路径
        String url = "http://localhost:8080/solr/item";
        SolrServer solrServer = new HttpSolrServer(url);

        //创建solrQuery对象,封装所有查询参数
        SolrQuery solrQuery = new SolrQuery();

        /**
         * 简单查询
         */
        solrQuery.set("q","*:*");

        //使用solr服务对象,查询索引库
        QueryResponse response = solrServer.query(solrQuery);

        //从响应对象获取文档集合
        SolrDocumentList results = response.getResults();

        //从文档集合中得到命中总记录
        long numFound = results.getNumFound();
        System.out.println("命中总记录数:"+numFound);

        //循环文档集合.获取文档对象中的数据
        for (SolrDocument sdoc : results) {
            //根据域字段名称，获取域字段的值
            String id = (String) sdoc.get("id");
            System.out.println("文档域字段："+id);

            String product_name = (String) sdoc.get("product_name");
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

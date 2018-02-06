package com.mr.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mr.entity.User;
import com.mr.service.ISolrUserService;
import com.mr.util.QueryResult;
import com.mr.util.SearchPage;
import com.mr.util.SearchUserDTO;

public class SolrUserServiceImpl implements ISolrUserService{  
	  
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrUserServiceImpl.class);  
    private static HttpSolrServer httpSolrServer;  
    /** httpServer 是用来连接solr服务器，这里采用单例模式设计 */  
    private static HttpSolrServer getHttpSolrServer() {  
        if (httpSolrServer == null) {  
            /** 用户（User）数据solr服务地址 */  
            httpSolrServer = new HttpSolrServer("http://127.0.0.1:8080/solr/mycore");  
            /** 设置solr查询超时时间 */  ;
            httpSolrServer.setSoTimeout(1000);  
            /** 设置solr连接超时时间 */  
            httpSolrServer.setConnectionTimeout(1000);  
            /** solr最大连接数 */  
            httpSolrServer.setDefaultMaxConnectionsPerHost(1000);  
            /** solr最大重试次数 */  
            httpSolrServer.setMaxRetries(1);  
            /** solr所有最大连接数 */  
            httpSolrServer.setMaxTotalConnections(100);  
            /** solr是否允许压缩 */  
            httpSolrServer.setAllowCompression(false);  
            /** solr是否followRedirects */  
            httpSolrServer.setFollowRedirects(true);  
        }  
        return httpSolrServer;  
    }  
    public boolean createUserIndex(User user) {  
    	LOGGER.debug("进入新增方法！");
        // 获取solr服务  
        SolrServer solrServer = getHttpSolrServer();  
        try {  
            // 创建索引,因为solr创建索引的时候，在参数类中的属性上面需要注解@Field,  
            //所以，要将user类转换成可以创建索引的类,我单独创建了一个类，对应User，  
            // SearchUserDTO.java,跟User类属性一样，只是在各个属性上面添加@Field注解  
            solrServer.addBean(toSearchUser(user));  
            // 提交创建。就相当于DB中的commit  
            solrServer.commit();  
            LOGGER.debug("新增成功!");
            return true;  
        } catch (IOException e) {  
            LOGGER.error("", e);  
        } catch (SolrServerException e) {  
            LOGGER.error("", e);  
        }  
        return false;  
    }  
    private SearchUserDTO toSearchUser(User user) {  
        SearchUserDTO userDTO = new SearchUserDTO();  
        // 此方法是将user中属性的值复制到userDTO属性，这个方法是复制类中属性名一样的属性值  
        BeanUtils.copyProperties(user, userDTO);  
        return userDTO;  
    }  
    @Override  
    public boolean createUserIndex(List<User> users) {  
        if (users != null && users.size() > 0) {  
            List<SearchUserDTO> datas = new ArrayList<SearchUserDTO>(users.size());  
            for (User user : users) {  
                datas.add(toSearchUser(user));  
            }  
            SolrServer solrServer = getHttpSolrServer();  
            try {  
                // 批量创建评价回复索引数据  
                solrServer.addBeans(datas);  
                solrServer.commit();  
                return true;  
            } catch (IOException e) {  
                // 如果创建失败的话，可以回滚  
                // solrServer.collback();  
                LOGGER.error("", e);  
            } catch (SolrServerException e) {  
                LOGGER.error("", e);  
            }  
        }  
        return false;  
    }  
    @Override  
    public boolean deleteUserIndex(List<String> custNos) {  
        SolrServer solrServer = getHttpSolrServer();  
        try {  
            // 根据唯一性标识删除索引  
            solrServer.deleteById(custNos);  
            // 删除该核下所有索引  
            // solrServer.delete("*:*");  
            solrServer.commit();  
            return true;  
        } catch (IOException e) {  
            LOGGER.error("", e);  
        } catch (SolrServerException e) {  
            LOGGER.error("", e);  
        }  
        return false;  
    }  
    @Override  
    public QueryResult<SearchUserDTO> queryUser(SearchPage pager) {  
        QueryResult<SearchUserDTO> queryResult = new QueryResult<SearchUserDTO>();  
        QueryResponse response = null;  
        // 设置默认查询条件，格式为：field:keyword,比如："custNo:1234"  
        String searchParam = pager.getField() + ":" + pager.getKeyword();  
        SolrServer server = getHttpSolrServer();  
        SolrQuery query = new SolrQuery(searchParam);  
        // 设置限制条件查询
        // query.setFilterQueries("username:zhangsan");  
        query.setFilterQueries(pager.getSelectParam());  
        query.setStart(pager.getStart()); // 起始位置，用于分页,solrj中默认是每页10条数据  
        query.setRows(pager.getPageSize()); // 每页文档数  
        try {  
            response = server.query(query);  
        } catch (SolrServerException e) {  
            LOGGER.error("查询索引出现问题", e);  
        }  
        if (response != null) {  
            SolrDocumentList list = response.getResults();  
            List<SearchUserDTO> datas = new ArrayList<SearchUserDTO>();  
            setSearchUserDTOData(datas, list);  
            queryResult.setTotalDataCount(new Long(list.getNumFound()).intValue());  
            queryResult.setPageNumber(pager.getPageNumber());  
            queryResult.setPageSize(pager.getPageSize());  
            queryResult.setDatas(datas);  
        }  
        return queryResult;  
    }  
    private void setSearchUserDTOData(List<SearchUserDTO> datas, SolrDocumentList list) {  
        for (SolrDocument solrDocument : list) {  
            SearchUserDTO userDTO = new SearchUserDTO();  
            // 根据属性名称从返回结果中取得数据，并且封装到返回对象中  
            userDTO.setUserName(solrDocument.getFieldValue("username").toString());  
            userDTO.setEmail(solrDocument.getFieldValue("email").toString());  
            userDTO.setCustNo(solrDocument.getFieldValue("custNo").toString());  
            userDTO.setAger((Integer)solrDocument.getFieldValue("age"));  
            datas.add(userDTO);  
        }  
    }

}  

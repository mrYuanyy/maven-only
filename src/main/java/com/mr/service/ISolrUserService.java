package com.mr.service;

import java.util.List;

import com.mr.entity.User;
import com.mr.util.QueryResult;
import com.mr.util.SearchPage;
import com.mr.util.SearchUserDTO;

public interface ISolrUserService {

	 /** 
     *  一句话功能描述：创建用户索引数据 
     */  
    public boolean createUserIndex(User user);  
    /** 
     * 一句话功能描述：批量创建用户索引数据 
     */  
    public boolean createUserIndex(List<User> userLists);  
    /** 
     * 一句话功能描述：批量删除用户索引数据 
     */  
    public boolean deleteUserIndex(List<String> custNos);  
    /** 
     * 一句话功能描述：查询用户数据 
     */  
    public QueryResult<SearchUserDTO> queryUser(SearchPage page);
    
}

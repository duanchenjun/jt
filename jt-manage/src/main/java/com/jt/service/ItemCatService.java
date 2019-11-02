package com.jt.service;

import java.util.List;

import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;

public interface ItemCatService {

	ItemCat findItemCatById(Long itemCatId);

	List<EasyUITree> findEasyUIByParentId(Long parentId);

	List<EasyUITree> findEasyUITreeCache(Long parentId) ;

	
}

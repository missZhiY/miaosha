package org.example.miaosha.dao;

import org.apache.ibatis.annotations.Param;
import org.example.miaosha.dataObject.ItemDO;

import java.util.List;

public interface ItemDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    List<ItemDO> listItem();

    ItemDO selectByPrimaryKey(Integer id);

    int increaseSales(@Param("id")Integer id, @Param("amount")Integer amount);

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);
}
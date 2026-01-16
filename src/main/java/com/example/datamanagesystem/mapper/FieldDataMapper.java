package com.example.datamanagesystem.mapper;

import com.example.datamanagesystem.entity.FieldData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FieldDataMapper {
	/**
	 * 级联删除字段关联的所有数据（移除static，补充SQL注解）
	 */
	@Delete("DELETE FROM field_data WHERE field_id = #{fieldId}")
	int deleteByFieldId(@Param("fieldId") Long fieldId);

	/**
	 * 批量新增字段数据（补充SQL注解）
	 */
	@Insert("<script>" +
			"INSERT INTO field_data (main_id, field_id, field_value) VALUES " +
			"<foreach collection='list' item='item' separator=','> " +
			"(#{item.mainId}, #{item.fieldId}, #{item.fieldValue}) " +
			"</foreach>" +
			"</script>")
	int batchInsert(@Param("list") List<FieldData> fieldDataList);

	/**
	 * 根据mainId查询字段数据（简化SQL，仅查需要的字段）
	 */
	@Select("SELECT id, main_id, field_id, field_value FROM field_data WHERE main_id = #{mainId}")
	List<FieldData> selectByMainId(@Param("mainId") Long mainId);

	/**
	 * 根据mainId删除字段数据（补充SQL注解）
	 */
	@Delete("DELETE FROM field_data WHERE main_id = #{mainId}")
	int deleteByMainId(@Param("mainId") Long mainId);

	/**
	 * 根据mainId和fieldId更新字段值（补充SQL注解）
	 */
	@Update("UPDATE field_data SET field_value = #{fieldValue} " +
			"WHERE main_id = #{mainId} AND field_id = #{fieldId}")
	int updateByMainIdAndFieldId(FieldData fieldData);
}
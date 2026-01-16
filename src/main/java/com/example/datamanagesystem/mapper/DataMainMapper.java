package com.example.datamanagesystem.mapper;

import com.example.datamanagesystem.entity.DataMain;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DataMainMapper {
	/**
	 * 新增基础数据
	 */
	@Insert("INSERT INTO data_main (table_code) VALUES (#{tableCode})")
	@Options(useGeneratedKeys = true, keyProperty = "id") // 回显自增主键
	int insert(DataMain dataMain);

	/**
	 * 根据tableCode查询基础数据列表
	 */
	@Select("SELECT id, table_code, create_time FROM data_main WHERE table_code = #{tableCode} ORDER BY create_time DESC")
	List<DataMain> selectByTableCode(@Param("tableCode") String tableCode);

	/**
	 * 根据ID查询基础数据
	 */
	@Select("SELECT id, table_code FROM data_main WHERE id = #{mainId}")
	DataMain selectById(@Param("mainId") Long mainId);

	/**
	 * 根据ID删除基础数据
	 */
	@Delete("DELETE FROM data_main WHERE id = #{mainId}")
	int deleteById(@Param("mainId") Long mainId);
}
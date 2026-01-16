package com.example.datamanagesystem.mapper;

import com.example.datamanagesystem.entity.FieldDefine;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FieldDefineMapper {
	// 新增字段（无field_length）
	@Insert("INSERT INTO field_define (table_code, field_name, field_code, field_type, is_required, sort_num) " +
			"VALUES (#{tableCode}, #{fieldName}, #{fieldCode}, #{fieldType}, #{isRequired}, #{sortNum})")
	int insert(FieldDefine fieldDefine);

	// 查询字段列表（补充@Param，显式标注参数名）
	@Select("SELECT * FROM field_define WHERE table_code = #{tableCode} ORDER BY sort_num ASC")
	List<FieldDefine> selectByTableCode(@Param("tableCode") String tableCode);

	// 查询单个字段
	@Select("SELECT * FROM field_define WHERE table_code = #{tableCode} AND field_code = #{fieldCode}")
	FieldDefine selectByCode(@Param("tableCode") String tableCode, @Param("fieldCode") String fieldCode);

	// 删除字段（实例方法，无static）
	@Delete("DELETE FROM field_define WHERE id = #{fieldId}")
	int deleteById(@Param("fieldId") Long fieldId);
}
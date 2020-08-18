package com.homework.web.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
@ApiModel(description = "小说分类")
public class Category extends AbstractAuditingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键，自增")
	Integer id;
	@Column
	@ApiModelProperty("名称")
	String name;
	@Column
	@ApiModelProperty("分类级别，1是一级分类，2是二级分类")
	Integer rank;
	@Column
	@ApiModelProperty("父分类的id，若无父分类，则为0")
	Integer parent_id;
}

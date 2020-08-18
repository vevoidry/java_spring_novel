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
@ApiModel(description = "小说")
public class Novel extends AbstractAuditingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键，自增")
	Integer id;
	@Column
	@ApiModelProperty("作者的id")
	Integer user_id;
	@Column
	@ApiModelProperty("分类的id")
	Integer category_id;
	@Column
	@ApiModelProperty("名称")
	String name;
	@Column
	@ApiModelProperty("概述")
	String summary;
	@Column
	@ApiModelProperty("图片")
	String image;
	@Column
	@ApiModelProperty("是否已完结")
	Boolean is_complete;
	@Column
	@ApiModelProperty("乘数，用于推荐排序，默认值为10000")
	Integer multiplier;
	@Column
	@ApiModelProperty("加数，用于推荐排序，默认值为0。排序值为：推荐数*乘数+加数")
	Integer addend;
}

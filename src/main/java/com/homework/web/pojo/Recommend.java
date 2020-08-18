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
@ApiModel(description = "推荐")
public class Recommend  extends AbstractAuditingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键，自增")
	Integer id;
	@Column
	@ApiModelProperty("用户的id")
	Integer user_id;
	@Column
	@ApiModelProperty("小说的id")
	Integer novel_id;
}

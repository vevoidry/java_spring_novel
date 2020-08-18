package com.homework.web.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@ApiModel(description = "所有实体类的抽象父类，包括插入者，插入时间，更新者，更新时间等属性")
public abstract class AbstractAuditingEntity {
	@CreatedBy
	@Column
	@JsonIgnore
	@ApiModelProperty(value = "执行插入的用户的用户名")
	String insert_user_username;

	@CreatedDate
	@Column
	@JsonIgnore
	@ApiModelProperty(value = "插入时间")
	Date insert_date;

	@LastModifiedBy
	@Column
	@JsonIgnore
	@ApiModelProperty(value = "执行最后一次更新的用户的用户名")
	String update_user_username;

	@LastModifiedDate
	@Column
	@JsonIgnore
	@ApiModelProperty(value = "执行最后一次更新的时间")
	Date update_date;
}

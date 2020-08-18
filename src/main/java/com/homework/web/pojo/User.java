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
@ApiModel(description = "用户")
public class User  extends AbstractAuditingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@ApiModelProperty("主键，自增")
	Integer id;
	@Column
	@ApiModelProperty("角色，ADMIN为管理员，VIP1为普通会员")
	String role;
	@Column
	@ApiModelProperty("用户名")
	String username;
	@Column
	@ApiModelProperty("密码")
	String password;
	@Column
	@ApiModelProperty("昵称")
	String nickname;
	@Column
	@ApiModelProperty("简介")
	String profile;
	@Column
	@ApiModelProperty("头像")
	String image;
	@Column
	@ApiModelProperty("手机号")
	String phone;
	@Column
	@ApiModelProperty("邮箱")
	String email;
//	@Column
//	@ApiModelProperty("是否锁定（一般可恢复）")
//	Boolean is_locked;
//	@Column
//	@ApiModelProperty("是否可用（一般不可恢复）")
//	Boolean is_enabled;
}

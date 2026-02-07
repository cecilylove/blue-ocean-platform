package com.cecilylove.blueocean.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Wang Li Hong
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseVersionEntity extends BaseEntity {

    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}

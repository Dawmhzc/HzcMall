package com.hzc.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCategory {

    private Integer categoryId;
    private Integer categoryLevel;
    private Integer parentId;
    private String categoryName;
    /*@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Integer createUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    private Integer updateUser;*/
    public boolean hasIllegalField() {
        return !StringUtils.hasLength(categoryName) || (categoryLevel==null || categoryLevel<1 || categoryLevel>3) ||
                (parentId==null && categoryLevel>1) || (parentId!=null && categoryLevel!=1);
    }
}

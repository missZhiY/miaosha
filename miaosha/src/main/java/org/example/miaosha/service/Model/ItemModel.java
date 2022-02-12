package org.example.miaosha.service.Model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemModel {
    private Integer id;

    //商品名称
    @NotBlank(message = "请输入商品名称")
    private String title;

    //商品价格
    @NotNull(message = "请输入商品价格")
    @Min(value = 0, message = "价格不能为0")
    private BigDecimal price;

    //商品库存
    @NotNull(message = "请输入库存")
    private Integer stock;

    //商品描述
    @NotBlank(message = "请输入描述信息")
    private String description;

    //商品销量
    private Integer sales;

    //商品图片url
    @NotBlank(message = "请输入图片信息")
    private String imgUrl;

    //当一个商品被赋予了一个秒杀活动后就相当于聚合了一个秒杀活动商品的属性
    //使用聚合模型，即java类的嵌套，将PromoModel冗余进来
    //若promoModel不为空则表示其具有还未结束的秒杀活动
    private PromoModel promoModel;

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
